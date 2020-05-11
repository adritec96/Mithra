package Mithra.core;


/**
 *
 * @author Adrian Ruiz Lopez
 */
public class ClientAgent extends MyJadeAgent {

    public ClientAgent(String pathLogFile) {
        super(pathLogFile);
    }


    @Override
    protected void setup(){
        // Call super method to start logger:
        super.setup();

        // Register Service Agent in Platform:
        if( !subcribePlatform() ){
            log("agent","ERROR --> Register agent in platform.");
            takeDown();
        }

    }





}// Fin clase de Agente
