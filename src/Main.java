import ui.ConsoleApp;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Modelo de domínio: Startup
 * - mantém estado (caixa, receitaBase, reputacao, moral, rodadaAtual)
 * - registra histórico
 * - calcula receita da rodada (aplicando bônus)
 * - calcula score final
 */
class Startup {
    private String nome;
    private double caixa;             // dinheiro em caixa
    private double receitaBase;       // receita "de referência" por rodada
    private int reputacao;            // 0..100
    private int moral;                // 0..100
    private int rodadaAtual = 1;

    // bônus que afeta a próxima rodada (ex.: +0.07 = +7% de receita)
    private double bonusPercentReceitaProx = 0.0;

    private final List<String> historico = new ArrayList<>();

    public Startup(String nome, double caixa, double receitaBase, int reputacao, int moral) {
        this.nome = nome;
        this.caixa = caixa;
        this.receitaBase = receitaBase;
        this.reputacao = reputacao;
        this.moral = moral;
    }

    public void registrar(String linha) {
        historico.add("R" + rodadaAtual + " - " + linha);
    }

    /** Receita desta rodada, aplicando o bônus acumulado e zerando-o em seguida. */
    public double receitaRodada() {
        double receita = receitaBase * (1.0 + bonusPercentReceitaProx);
        bonusPercentReceitaProx = 0.0; // bônus vale só para uma rodada
        return receita;
    }

    /** Garante que reputação e moral fiquem entre 0 e 100. */
    public void clamparHumor() {
        reputacao = Math.max(0, Math.min(100, reputacao));
        moral = Math.max(0, Math.min(100, moral));
    }

    /** Score final (ajuste pesos como quiser). */
    public double scoreFinal() {
        return reputacao * 0.35
             + moral * 0.25
             + (caixa / 1000.0) * 0.15
             + (receitaBase / 1000.0) * 0.25;
    }

    // Getters/Setters essenciais
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getCaixa() { return caixa; }
    public void setCaixa(double caixa) { this.caixa = caixa; }

    public double getReceitaBase() { return receitaBase; }
    public void setReceitaBase(double receitaBase) { this.receitaBase = receitaBase; }

    public int getReputacao() { return reputacao; }
    public void setReputacao(int reputacao) { this.reputacao = reputacao; }

    public int getMoral() { return moral; }
    public void setMoral(int moral) { this.moral = moral; }

    public int getRodadaAtual() { return rodadaAtual; }
    public void setRodadaAtual(int rodadaAtual) { this.rodadaAtual = rodadaAtual; }

    public double getBonusPercentReceitaProx() { return bonusPercentReceitaProx; }
    public void addBonusPercentReceitaProx(double delta) { this.bonusPercentReceitaProx += delta; }

    public List<String> getHistorico() { return historico; }

    @Override
    public String toString() {
        return String.format(Locale.US,
            "%s | Caixa: R$%.2f | ReceitaBase: R$%.2f | Rep: %d | Moral: %d",
            nome, caixa, receitaBase, reputacao, moral);
    }
}


/**
 * Loop do jogo (console) + regras/efeitos.
 * Compile: javac Main.java
 * Rode:    java -cp . Main
 */
public class Main {

    // Configurações do jogo
    static final int TOTAL_RODADAS = 2;
    static final int MAX_DECISOES_POR_RODADA = 3;

    // Entrada padrão
    static final Scanner IN = new Scanner(System.in);

    // Tipos de decisão disponíveis
    enum TipoDecisao {
        MARKETING,      // +reputação; bônus % receita na próxima rodada; -caixa
        EQUIPE,         // +moral; -caixa
        PRODUTO,        // bônus % receita na próxima rodada; -caixa
        INVESTIDORES,   // chance de +caixa e +reputação; senão -reputação
        CORTAR_CUSTOS   // +caixa; -moral
    }

    public static void main(String[] args) {
        System.out.println("=== Startup Game (Console) ===");

        // 1) Cria as startups iniciais (pode ler do teclado se preferir)
        List<Startup> startups = criarStartupsIniciais();

        // 2) Executa as rodadas
        for (int rodada = 1; rodada <= TOTAL_RODADAS; rodada++) {
            System.out.println("\n====== RODADA " + rodada + " / " + TOTAL_RODADAS + " ======");

            int idx = 1;
            for (Startup s : startups) {
                s.setRodadaAtual(rodada);
                System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                System.out.println(s);

                // Seleciona decisões (até 3) de forma robusta
                List<TipoDecisao> escolhidas = escolherDecisoesNoConsole(s);

                // Aplica cada decisão
                for (TipoDecisao d : escolhidas) {
                    aplicarDecisao(s, d);
                }

                // Fecha a rodada desta startup (receita + pequeno crescimento da base)
                fecharRodada(s);

                // Resumo
                System.out.println("Resumo pós-rodada: " + s);
            }
        }

        // 3) Ranking final
        System.out.println("\n====== RELATÓRIO FINAL ======");
        startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());
        int pos = 1;
        for (Startup s : startups) {
            System.out.printf(Locale.US, "%d) %s | SCORE: %.2f | %s%n",
                    pos++, s.getNome(), round2(s.scoreFinal()), s.toString());
        }

        // (Opcional) Histórico detalhado
        System.out.print("\nDeseja ver histórico (s/n)? ");
        if (IN.nextLine().trim().equalsIgnoreCase("s")) {
            for (Startup s : startups) {
                System.out.println("\n-- Histórico: " + s.getNome() + " --");
                s.getHistorico().forEach(System.out::println);
            }
        }

        System.out.println("\nFim. Obrigado por jogar!");
    }

    // ====== Regras do jogo ======

    static void aplicarDecisao(Startup s, TipoDecisao d) {
        switch (d) {
            case MARKETING -> {
                s.setCaixa(s.getCaixa() - 10_000);
                s.setReputacao(s.getReputacao() + 5);
                s.addBonusPercentReceitaProx(0.03);
                s.registrar("Marketing: -R$10k caixa, +5 rep, +3% receita na próxima rodada");
            }
            case EQUIPE -> {
                s.setCaixa(s.getCaixa() - 5_000);
                s.setMoral(s.getMoral() + 7);
                s.registrar("Equipe (treinamento): -R$5k caixa, +7 moral");
            }
            case PRODUTO -> {
                s.setCaixa(s.getCaixa() - 8_000);
                s.addBonusPercentReceitaProx(0.04);
                s.registrar("Produto (refino): -R$8k caixa, +4% receita na próxima rodada");
            }
            case INVESTIDORES -> {
                boolean aprovado = Math.random() < 0.60;
                if (aprovado) {
                    s.setCaixa(s.getCaixa() + 40_000);
                    s.setReputacao(s.getReputacao() + 3);
                    s.registrar("Investidores: APROVADO +R$40k caixa, +3 rep");
                } else {
                    s.setReputacao(s.getReputacao() - 2);
                    s.registrar("Investidores: REPROVADO (0 caixa), -2 rep");
                }
            }
            case CORTAR_CUSTOS -> {
                s.setCaixa(s.getCaixa() + 8_000);
                s.setMoral(s.getMoral() - 5);
                s.registrar("Cortar custos: +R$8k caixa, -5 moral");
            }
        }
        s.clamparHumor();
    }

    static void fecharRodada(Startup s) {
        double receita = s.receitaRodada();
        s.setCaixa(s.getCaixa() + receita);

        // Crescimento leve da receita base influenciado por reputação/moral
        double fatorCrescimento = 1.0
                + (s.getReputacao() / 100.0) * 0.01
                + (s.getMoral() / 100.0) * 0.005;
        s.setReceitaBase(s.getReceitaBase() * fatorCrescimento);

        s.registrar(String.format(Locale.US,
            "Fechamento: receita R$%.2f; nova receitaBase R$%.2f; caixa R$%.2f",
            round2(receita), round2(s.getReceitaBase()), round2(s.getCaixa())));
    }

    // ====== UI ======

    static List<Startup> criarStartupsIniciais() {
        // Três startups padrão (pode trocar por input do usuário)
        List<Startup> list = new ArrayList<>();
        list.add(new Startup("AlphaTech", 100_000, 10_000, 50, 60));
        list.add(new Startup("BioNova",     80_000,  7_000, 55, 55));
        list.add(new Startup("CloudZ",     110_000, 12_000, 45, 65));
        return list;
    }

    /** Menu robusto: Enter ou 0 finaliza; Q encerra o jogo inteiro. */
    static List<TipoDecisao> escolherDecisoesNoConsole(Startup s) {
        List<TipoDecisao> todas = Arrays.asList(TipoDecisao.values());
        List<TipoDecisao> escolhidas = new ArrayList<>();
        int restantes = MAX_DECISOES_POR_RODADA;

        while (restantes > 0) {
            System.out.println("\nEscolha uma decisão (" + restantes + " restante(s)):");
            for (int i = 0; i < todas.size(); i++) {
                System.out.println((i + 1) + ") " + todas.get(i));
            }
            System.out.println("0) finalizar (ou Enter)");
            System.out.println("Q) sair do jogo");

            System.out.print("Opção: ");
            String entrada = IN.nextLine().trim();

            if (entrada.isEmpty() || entrada.equals("0")) break;
            if (entrada.equalsIgnoreCase("q")) {
                System.out.println("Saindo do jogo a pedido do usuário...");
                System.exit(0);
            }

            int opt;
            try {
                opt = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite 1.." + todas.size() + ", 0/Enter para finalizar, ou Q para sair.");
                continue;
            }
            if (opt < 1 || opt > todas.size()) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            TipoDecisao d = todas.get(opt - 1);
            if (escolhidas.contains(d)) {
                System.out.println("Já escolhida nesta rodada.");
                continue;
            }
            escolhidas.add(d);
            restantes--;
        }

        if (escolhidas.isEmpty()) {
            System.out.println("Nenhuma decisão selecionada. Segue sem ação nesta rodada.");
        }
        return escolhidas;
    }

    // ====== Util ======
    static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}