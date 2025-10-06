package model.vo;

public record Humor(int valor) {
    public Humor {
        if (valor < 0 || valor > 100) throw new IllegalArgumentException("Humor deve estar entre 0 e 100");
    }
    public Humor aumentar(int delta){ return new Humor(Math.min(100, valor + delta)); }
    public Humor reduzir(int delta){ return new Humor(Math.max(0, valor - delta)); }
}
