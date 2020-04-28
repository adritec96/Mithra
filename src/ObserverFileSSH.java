
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adrij
 */
public class ObserverFileSSH extends TickerBehaviour{
    private File fileSSH;
    private sshAgent miAgente;
    private int lineas = 100;
    
    public ObserverFileSSH(sshAgent agn, int milisec) {
        super(agn, milisec);
        this.miAgente =agn;
        this.fileSSH = agn.sshFile;
    }

    @Override
    protected void onTick() {
        
        
        
    }
    
}
