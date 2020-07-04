package Mithra.core;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


public class initNode extends init{
    private final String SERVER_IP;
    private final String SERVER_PORT;

    public initNode(String platformId, String serverIp, String serverPort ) throws UnknownHostException {
        super(platformId);
        SERVER_IP = serverIp;
        SERVER_PORT = serverPort;
    }

    public void start(){
        // check if the agent is valid.
        if( !checkAgents() ){
            System.out.println("ERROR, AGENTE NO VALIDO.");
            return;
        }

        // Start Client agent:
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.PLATFORM_ID, PLATFORM_ID);
        p.setParameter(Profile.MAIN_HOST, SERVER_IP);
        p.setParameter(Profile.LOCAL_HOST, HOST_IP);
        p.setParameter(Profile.CONTAINER_NAME, HOST_NAME);
        p.setParameter(Profile.MAIN_PORT, SERVER_PORT);
        p.setParameter(Profile.GUI, "true" );
        ContainerController containerController = rt.createAgentContainer(p);
        AgentController agentController;

        for(Map.Entry<String,MithraAgent> entry : agents.entrySet()) {
            try {
                agentController = containerController.acceptNewAgent( entry.getKey(), entry.getValue() );
                agentController.start();
            } catch (Exception e) {
                System.out.println("ERROR -> Agent (" + entry.getValue().getName() + ") failed to start \"" + HOST_NAME + "\"");
            }
        }
    }

}
