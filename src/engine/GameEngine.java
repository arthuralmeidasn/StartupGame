package engine;

import ui.ConsoleApp;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import model.Startup;
import model.vo.Dinheiro;
import model.vo.Humor;
import model.TipoDecisao;

// Executa as rodadas

public class GameEngine {   

    public void executar() {
        int totalRodadas = 8;
        Scanner IN = new Scanner(System.in);
        List<Startup> startups = new ArrayList<>();

        System.out.println("--Bem-vindo ao jogo Startup Game--");
        System.out.println("Digite o nome da sua Startup: ");
        String nomeStartup = IN.nextLine();

        Dinheiro caixaInicial = new Dinheiro(50000.0);
        Dinheiro receitaInicial = new Dinheiro(0.0);
        Humor reputacaoInicial = new Humor(50);
        Humor moralInicial = new Humor (50);

        Startup suaStartup = new Startup(nomeStartup, caixaInicial, receitaInicial, reputacaoInicial, moralInicial);
        startups.add(suaStartup);

        for (int rodada = 1; rodada <= totalRodadas; rodada++) {
            System.out.println("\n====== RODADA " + rodada + " / " + totalRodadas + " ======");

            int idx = 1;
            for (Startup s : startups) {
                s.setRodadaAtual(rodada);
                System.out.println("\n-- Jogador " + idx++ + ": " + s.getNome() + " --");
                System.out.println(s);

                // Seleciona decisões (até 3) de forma robusta
                List<TipoDecisao> escolhidas = ui.ConsoleApp.escolherDecisoesNoConsole(s);

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

    // Aplica lógica de uma decisão  específica
    private void aplicarDecisao(Startup s, TipoDecisao d) {
        System.out.println("Aplicando decisão: "+ d.name());

    }

    // Aplica lógica do fim da rodada
    private void fecharRodada(Startup s) {
        System.out.println("Fechando rodada para "+ s.getNome());
    }

    // Método utilitário para arredondar 2 casas decimais
    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}