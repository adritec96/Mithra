package Mithra.core;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.Behaviour;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class initAgent {
    private final  String PLATFORM_ID;
    private final String PLATFORM_IP;
    private final String HOST_IP;
    private final String HOST_NAME;
    private final String PLATFORM_PORT;
    private MithraAgent agent;

    public initAgent(String platformId, String platformIp,String platformPort ) throws UnknownHostException {
        PLATFORM_ID = platformId;
        PLATFORM_IP = platformIp;
        PLATFORM_PORT = platformPort;

        // Get hostname and ip for the Computer
        InetAddress address = InetAddress.getLocalHost();
        HOST_NAME = address.getHostName();
        HOST_IP = address.getHostAddress();
    }

    public void setAgent( MithraAgent myAgent ){
        if( myAgent.isValid() ){
            agent = myAgent;
        }else{
            System.out.println("Error, agente no valido.");
        }
    }

    public void start(){
        // Start Client agent:
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.PLATFORM_ID, PLATFORM_ID);
        p.setParameter(Profile.MAIN_HOST, PLATFORM_IP);
        p.setParameter(Profile.LOCAL_HOST, HOST_IP);
        p.setParameter(Profile.CONTAINER_NAME, HOST_NAME);
        p.setParameter(Profile.MAIN_PORT, PLATFORM_PORT);
        p.setParameter(Profile.GUI, "true" );
        ContainerController containerController = rt.createAgentContainer(p);
        AgentController agentController;
        try{
            agentController = containerController.acceptNewAgent("Agent-"+ HOST_NAME,agent);
            agentController.start();
        }catch( Exception e){
            System.out.println("ERROR -> Agent failed to start \""+HOST_NAME+"\"");
        }
    }



}
