package actions;

import model.Deltas;
import model.Startup;

public class CortarCustosStrategy implements DecisaoStrategy {
    public Deltas aplicar(Startup s) {
        double caixaDelta = 8_000;
        int reputacaoDelta = 0;
        int moralDelta = -5;
        double bonusDelta = 0;

        return new Deltas(caixaDelta, reputacaoDelta, moralDelta, bonusDelta);
    }
}