package ui;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import config.Config;
import model.Startup;
import model.TipoDecisao;

public class ConsoleApp {
    private final Config config = new Config();
    private static final Scanner IN = new Scanner(System.in);
    private static final int MAX_DECISOES_POR_RODADA = 3;


    public void start(){
        System.out.println("Startup Game - Console");
        System.out.println("total.rodadas=" + config.totalRodadas());
        System.out.println("max.decisoes.por.rodada=" + config.maxDecisoesPorRodada());
        // TODO: implementar menu, criação/continuação de jogo e loop chamando GameEngine
    }

    /** Menu robusto: Enter ou 0 finaliza; Q encerra o jogo inteiro. */
    public static List<TipoDecisao> escolherDecisoesNoConsole(Startup s) {
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
}