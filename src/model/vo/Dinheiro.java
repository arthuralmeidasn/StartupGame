package model.vo;

public record Dinheiro(double valor) {
    public Dinheiro {
        if (valor < 0) throw new IllegalArgumentException("Dinheiro não pode ser negativo");
    }
    public Dinheiro somar(Dinheiro outro){ return new Dinheiro(this.valor + outro.valor); }
    public Dinheiro subtrair(Dinheiro outro){
        double novo = this.valor - outro.valor;
        if (novo < 0) throw new IllegalArgumentException("Saldo insuficiente");
        return new Dinheiro(novo);
    }
    @Override public String toString(){ return String.format("R$%.2f", valor); }
}
