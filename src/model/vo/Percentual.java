package model.vo;

public record Percentual(double valor) {
    public Percentual {
        if (valor < -1.0 || valor > 1.0) {
            throw new IllegalArgumentException("Percentual deve estar entre -100% e +100%");
        }
    }
    public double aplicarSobre(double base){ return base * (1.0 + valor); }
}
