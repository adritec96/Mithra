package Mithra;

import Mithra.core.MithraAgent;
import Mithra.core.initAgent;
import Mithra.core.initPlatform;
import Mithra.hostFile.checkHostFile;
import Mithra.hostFile.reciveNewHostFile;

public class Example {

    public static void main(String[] args) {
        //Variables established by the client.
        String name_Platform = "Mithra-Platform";
        String port_platform = "1099"; // agents and platform used 1099
        String ip_platform = "192.168.1.2";
        try {
            
            // Start Agents Platform
            initPlatform initPlat = new initPlatform(name_Platform,port_platform);
            initPlat.start();

            // Start Agent
            initAgent initAg = new initAgent(name_Platform,ip_platform,port_platform);
            // Create Agent
            MithraAgent mithraAgent = new MithraAgent("./log/loginEX.log");
            // Create and Add observers and services
            mithraAgent.addBehaviour(new checkHostFile(mithraAgent,1000,"./hosts"));
            mithraAgent.addBehaviour(new reciveNewHostFile(mithraAgent,"./hosts"));
            // Set Agent
            initAg.setAgent(mithraAgent);
            // Start Agent
            initAg.start();
        }catch (Exception ex){
            System.out.println("ERROR");
        }

    }
}
