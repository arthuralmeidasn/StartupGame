package model;

import model.vo.Dinheiro;
import model.vo.Humor;
import model.vo.Percentual;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Startup {
    private String nome;
    private Dinheiro caixa;
    private Dinheiro receitaBase;
    private Humor reputacao;
    private Humor moral;
    private int rodadaAtual = 1;
    private Percentual bonusPercentReceitaProx = new Percentual(0.0);

    private final List<String> historico = new ArrayList<>();

    public Startup(String nome, Dinheiro caixa, Dinheiro receitaBase, Humor reputacao, Humor moral) {
        this.nome = nome;
        this.caixa = caixa;
        this.receitaBase = receitaBase;
        this.reputacao = reputacao;
        this.moral = moral;
    }

    public void registrar(String linha) { historico.add("R" + rodadaAtual + " - " + linha); }

    public double receitaRodada() {
        double receita = bonusPercentReceitaProx.aplicarSobre(receitaBase.valor());
        bonusPercentReceitaProx = new Percentual(0.0);
        return receita;
    }

    public void clamparHumor() {
        reputacao = reputacao; // VO já valida limites na criação
        moral = moral;
    }

    public double scoreFinal() {
        return reputacao.valor() * 0.35
             + moral.valor() * 0.25
             + (caixa.valor() / 1000.0) * 0.15
             + (receitaBase.valor() / 1000.0) * 0.25;
    }

    // getters e setters mínimos
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Dinheiro getCaixa() { return caixa; }
    public void setCaixa(Dinheiro caixa) { this.caixa = caixa; }

    public Dinheiro getReceitaBase() { return receitaBase; }
    public void setReceitaBase(Dinheiro receitaBase) { this.receitaBase = receitaBase; }

    public Humor getReputacao() { return reputacao; }
    public void setReputacao(Humor reputacao) { this.reputacao = reputacao; }

    public Humor getMoral() { return moral; }
    public void setMoral(Humor moral) { this.moral = moral; }

    public int getRodadaAtual() { return rodadaAtual; }
    public void setRodadaAtual(int rodadaAtual) { this.rodadaAtual = rodadaAtual; }

    public Percentual getBonusPercentReceitaProx() { return bonusPercentReceitaProx; }
    public void addBonusPercentReceitaProx(double delta) { this.bonusPercentReceitaProx = new Percentual(bonusPercentReceitaProx.valor() + delta); }

    public List<String> getHistorico() { return historico; }

    @Override
    public String toString() {
        return String.format(Locale.US,
            "%s | Caixa: R$%.2f | ReceitaBase: R$%.2f | Rep: %d | Moral: %d",
            nome, caixa.valor(), receitaBase.valor(), reputacao.valor(), moral.valor());
    }
}
