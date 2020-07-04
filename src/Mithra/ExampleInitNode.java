package Mithra;

import Mithra.core.MithraAgent;
import Mithra.core.initNode;
import Mithra.hostFile.checkHostFile;
import Mithra.hostFile.reciveNewHostFile;

public class ExampleInitNode {

    public static void main(String[] args) {
        //Variables established by the client.
        String name_Platform = "Mithra-Platform";
        String port_platform = "1099"; // agents and platform used 1099
        String ip_platform = "192.168.1.2";
        try {

            // Start Agent
            initNode initNode = new initNode(name_Platform,ip_platform,port_platform);
            // Create Agent
            MithraAgent mithraAgent = new MithraAgent("./log/loginEX.log");
            // Create and Add observers and services
            mithraAgent.addBehaviour(new checkHostFile(mithraAgent,1000,"./hosts"));
            mithraAgent.addBehaviour(new reciveNewHostFile(mithraAgent,"./hosts"));
            // Set Agent 1
            initNode.addAgent("agentePrincipal",mithraAgent);
            // Set Agent 2
            mithraAgent = new MithraAgent("./log/loginEX2.log");
            mithraAgent.addBehaviour(new checkHostFile(mithraAgent,1000,"./hosts"));
            initNode.addAgent("agenteSecundario",mithraAgent);
            // Start Agent
            initNode.start();


        }catch (Exception ex){
            System.out.println("ERROR");
        }

    }
}
