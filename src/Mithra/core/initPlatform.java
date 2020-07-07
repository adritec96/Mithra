package Mithra.core;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.net.UnknownHostException;
import java.util.Map;


public class initPlatform extends init {
    protected  String PLATFORM_PORT;

    public initPlatform(String platformId, String platformPort) throws UnknownHostException {
        super(platformId);
        PLATFORM_PORT = platformPort;
    }

    public void start(){
        // START SERVER:
        Profile p = new ProfileImpl(false);
        p.setParameter(Profile.PLATFORM_ID, PLATFORM_ID );
        p.setParameter(Profile.MAIN_HOST, HOST_IP );
        p.setParameter(Profile.LOCAL_HOST, HOST_IP );
        p.setParameter(Profile.MAIN_PORT, PLATFORM_PORT );
        p.setParameter(Profile.LOCAL_PORT,  PLATFORM_PORT );
        if(gui) p.setParameter(Profile.GUI, "true");
        try {
            ContainerController containerController = jade.core.Runtime.instance().createMainContainer(p);
            AgentController agentController;
            for(Map.Entry<String,MithraAgent> entry : agents.entrySet()) {
                try {
                    agentController = containerController.acceptNewAgent( entry.getKey(), entry.getValue() );
                    agentController.start();
                } catch (Exception e) {
                    System.out.println("ERROR -> Agent (" + entry.getValue().getName() + ") failed to start \"" + HOST_NAME + "\"");
                }
            }
        }catch (Exception e){
            System.out.println("Error Starting Agent Platform\n"+e);
        }



    }
}
