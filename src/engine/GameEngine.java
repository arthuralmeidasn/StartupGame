package engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import actions.CortarCustosStrategy;
import actions.DecisaoStrategy;
import actions.EquipeStrategy;
import actions.InvestidoresStrategy;
import actions.MarketingStrategy;
import actions.ProdutoStrategy;
import config.Config;
import model.Startup;
import model.enums.TipoDecisao;
import model.vo.Dinheiro;
import model.vo.Humor;
import persistence.StartupRepository;

public class GameEngine {
    private final Config config;
    private final StartupRepository repository;


    public GameEngine() {
        this.config = new Config();
        this.repository = new StartupRepository();
        this.repository.prepararBanco();
    }

    private static List<Startup> criarStartupsIniciais() {
        List<Startup> list = new ArrayList<>();
        list.add(new Startup("AlphaTech", new Dinheiro(100_000),
                new Dinheiro(10_000),
                new Humor(50),
                new Humor(60)));

        list.add(new Startup("BioNova", new Dinheiro(80_000),
                new Dinheiro(7_000),
                new Humor(55),
                new Humor(55)));

        list.add(new Startup("CloudZ", new Dinheiro(110_000),
                new Dinheiro(12_000),
                new Humor(50),
                new Humor(65)));
        return list;
    }

    public void executar() {
        int totalRodadas = config.totalRodadas();
        List<Startup> startups = criarStartupsIniciais();

        for (int rodada = 1; rodada <= totalRodadas; rodada++) {
            System.out.println("\n====== RODADA " + rodada + " / " + totalRodadas + " ======");

            int idx = 1;
            for (Startup s : startups) {
                s.setRodadaAtual(rodada);
                System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                System.out.println(s);

                List<TipoDecisao> escolhidas = ui.ConsoleApp.escolherDecisoesNoConsole(s);

                for (TipoDecisao d : escolhidas) {
                    aplicarDecisao(s, d);
                }

                fecharRodada(s);

                repository.salvar(s);
                System.out.println(" [Autosave] Progresso de " + s.getNome() + " salvo.");

                System.out.println("Resumo pós-rodada: " + s);
            }
        }

        System.out.println("\n====== RELATÓRIO FINAL ======");
        startups.sort(Comparator.comparingDouble(Startup::scoreFinal).reversed());
        int pos = 1;
        for (Startup s : startups) {
            System.out.printf(Locale.US, "%d) %s | SCORE: %.2f | %s%n",
                    pos++, s.getNome(), round2(s.scoreFinal()), s.toString());
        }

        System.out.print("\nDeseja ver histórico (s/n)? ");
        Scanner IN = new Scanner(System.in);
        if (IN.nextLine().trim().equalsIgnoreCase("s")) {
            for (Startup s : startups) {
                System.out.println("\n-- Histórico: " + s.getNome() + " --");
                s.getHistorico().forEach(System.out::println);
            }
        }

        System.out.println("\nFim. Obrigado por jogar!");
    }

    private void aplicarDecisao(Startup s, TipoDecisao d) {
        DecisaoStrategy estrategiaEscolhida;

        switch (d) {
            case MARKETING -> estrategiaEscolhida = new MarketingStrategy();
            case EQUIPE -> estrategiaEscolhida = new EquipeStrategy();
            case PRODUTO -> estrategiaEscolhida = new ProdutoStrategy();
            case INVESTIDORES -> estrategiaEscolhida = new InvestidoresStrategy();
            case CORTAR_CUSTOS -> estrategiaEscolhida = new CortarCustosStrategy();
            default -> {
                System.err.println("Decisão não reconhecida: " + d);
                return;
            }
        }
        estrategiaEscolhida.aplicar(s);
    }

    private void fecharRodada(Startup s) {
        double receita = s.receitaRodada();

        s.setCaixa(s.getCaixa().somar(new Dinheiro(receita)));

        double fatorReputacao = (s.getReputacao().valor() / 100.0) * 0.01;
        double fatorMoral = (s.getMoral().valor() / 100.0) * 0.005;
        double fatorCrescimento = 1.0 + fatorReputacao + fatorMoral;
        double novaReceitaBase = s.getReceitaBase().valor() * fatorCrescimento;
        s.setReceitaBase(new Dinheiro(novaReceitaBase));

        s.registrar(String.format(Locale.US,
                "Fechamento: receita R$%.2f; nova receitaBase R$%.2f; caixa R.2f",
                round2(receita),
                round2(s.getReceitaBase().valor()),
                round2(s.getCaixa().valor())));
    }

    private static double round2(double v) {
        return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}