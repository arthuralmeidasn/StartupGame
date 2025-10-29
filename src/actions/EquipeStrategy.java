package actions;

import model.Deltas;
import model.Startup;

public class EquipeStrategy implements DecisaoStrategy {
    @Override
    public Deltas aplicar(Startup s) {
        double caixaDelta = -5_000;
        int reputacaoDelta = 0;
        int moralDelta = 7;
        double bonusDelta = 0;

        return new Deltas(caixaDelta, reputacaoDelta, moralDelta, bonusDelta);
    }
}