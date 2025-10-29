package actions;

import model.Deltas;
import model.Startup;

public class InvestidoresStrategy implements DecisaoStrategy {
    @Override
    public Deltas aplicar(Startup s) {
        boolean aprovado = Math.random() < 0.60;

        if(aprovado) {
                return new Deltas(40_000,3,0,0);
        } else {
                return new Deltas(0,-2,0,0);
        }
    }
}