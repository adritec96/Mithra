package Mithra.core;

import jade.core.behaviours.CyclicBehaviour;

public abstract class MithraService  extends CyclicBehaviour {
    public MithraService(MithraAgent agn) {
        super(agn);
    }
}
