package Mithra.core;



/**
 *
 * @author Adrian Ruiz Lopez
 */
public class ServerAgent extends MyJadeAgent {

    public ServerAgent(String pathLogFile) {
        super(pathLogFile);
    }

    @Override
    protected void setup(){
        // Call super method to start logger:
        super.setup();

        // Register Service Agent in Platform:
        if( !subcribePlatform() ){
            System.out.println("ERROR --> Register agent server in platform.");
            System.exit(1);
        }

    }



}// Fin clase de Agente
