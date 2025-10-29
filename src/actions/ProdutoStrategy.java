package actions;

import model.Deltas;
import model.Startup;

public class ProdutoStrategy implements DecisaoStrategy {
    @Override
    public Deltas aplicar(Startup s) {
        double caixaDelta = -8_000;
        int reputacaoDelta = 0;
        int moralDelta = 0;
        double bonusDelta = 0.04;

        return new Deltas(caixaDelta, reputacaoDelta, moralDelta, bonusDelta);
    }
}