package Mithra.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public abstract class init {
    protected  String PLATFORM_ID;
    protected  String HOST_IP;
    protected  String HOST_NAME;
    protected  Map<String,MithraAgent> agents;

    public init(String platformId) throws UnknownHostException {
        this.agents = new HashMap<>();
        PLATFORM_ID = platformId;


        // Get hostname and ip for the Computer
        InetAddress address = InetAddress.getLocalHost();
        HOST_IP = address.getHostAddress();
        HOST_NAME = address.getHostName();
    }

    public void addAgent( String name, MithraAgent myAgent ){
        if( myAgent.isValid() ){
            agents.put(name,myAgent);
        }else{
            System.out.println("ERROR: Invalid Agent! please check agent");
        }
    }

    public boolean checkAgents(){
        // Check if there is a minimum agent.
        if( agents.size() < 1 ) return false;
        // Check if all agents are valid.
        for(Map.Entry<String,MithraAgent> entry : agents.entrySet() ){
            if( !entry.getValue().isValid() ) return false;
        }
        return true;
    }

    public abstract void start();
}
