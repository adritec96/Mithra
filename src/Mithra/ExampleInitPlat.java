package Mithra;

import Mithra.core.MithraAgent;
import Mithra.core.initNode;
import Mithra.core.initPlatform;
import Mithra.hostFile.checkHostFile;
import Mithra.hostFile.reciveNewHostFile;

public class ExampleInitPlat {

    public static void main(String[] args) {
        //Variables established by the client.
        String name_Platform = "Mithra-Platform";
        String port_platform = "1099"; // agents and platform used 1099
        String ip_platform = "192.168.1.2";
        try {
            
            // Start Agents Platform
            initPlatform initPlat = new initPlatform(name_Platform,port_platform);
            // Create Agent
            MithraAgent mithraAgent = new MithraAgent("./log/loginEX.log");
            // Create and Add observers and services
            mithraAgent.addBehaviour(new checkHostFile(mithraAgent,1000,"./hosts"));
            initPlat.addAgent("principal",mithraAgent);
            initPlat.start();



        }catch (Exception ex){
            System.out.println("ERROR");
        }

    }
}
