package actions;

import model.Startup;
import model.Deltas;

public interface DecisaoStrategy {
    Deltas aplicar(Startup s);
    default void reverter(Startup s, model.Deltas d) { /* opcional */ }
}
