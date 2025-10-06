package config;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private final Properties props = new Properties();
    private final int totalRodadas;
    private final int maxDecisoesPorRodada;

    public Config() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("game.properties")) {
            if (in == null) throw new IllegalStateException("Arquivo resources/game.properties não encontrado");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao carregar game.properties", e);
        }
        this.totalRodadas = Integer.parseInt(props.getProperty("total.rodadas").trim());
        this.maxDecisoesPorRodada = Integer.parseInt(props.getProperty("max.decisoes.por.rodada").trim());
    }

    public int totalRodadas() { return totalRodadas; }
    public int maxDecisoesPorRodada() { return maxDecisoesPorRodada; }
}
