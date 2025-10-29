package actions;

import model.Deltas;
import model.Startup;

public class MarketingStrategy implements DecisaoStrategy {
    @Override
    public Deltas aplicar(Startup s) {
        double caixaDelta = -10_000;
        int reputacaoDelta = 5;
        int moralDelta = 0;
        double bonusDelta =0.03;

        return new Deltas(caixaDelta, reputacaoDelta, moralDelta, bonusDelta);
    }
}