package Mithra.core;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public abstract class MithraObserver extends TickerBehaviour {
    public MithraObserver(Agent a, long period) {
        super(a, period);
    }
}
