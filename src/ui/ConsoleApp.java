package ui;

import config.Config;

public class ConsoleApp {
    private final Config config = new Config();

    public void start(){
        System.out.println("Startup Game - Console");
        System.out.println("total.rodadas=" + config.totalRodadas());
        System.out.println("max.decisoes.por.rodada=" + config.maxDecisoesPorRodada());
        // TODO: implementar menu, criação/continuação de jogo e loop chamando GameEngine
    }
}
