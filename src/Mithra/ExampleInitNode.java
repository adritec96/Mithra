package Mithra;

import Mithra.core.MithraAgent;
import Mithra.core.initNode;
import Mithra.hostFile.checkHostFile;
import Mithra.hostFile.reciveNewHostFile;
import Mithra.sshFile.IpRepository;
import Mithra.sshFile.RamIpRepository;
import Mithra.sshFile.checkSshFile;
import Mithra.sshFile.receiveIPs;

public class ExampleInitNode {

    public static void main(String[] args) {
        //Variables established by the client.
        String name_Platform = "Mithra-Platform";
        String port_platform = "1099"; // agents and platform used 1099
        String ip_platform = "192.168.1.2";
        try {


            // Start Agent
            initNode initNode = new initNode(name_Platform,ip_platform,port_platform);
            initNode.changeNamePlatform("Node1");
            // Create Agent
            MithraAgent mithraAgent = new MithraAgent("./log/loginNode.log");
            // Create and Add observers and services
            mithraAgent.addBehaviour(new checkHostFile(mithraAgent,10000,"./hostsNode1"));
            mithraAgent.addBehaviour(new reciveNewHostFile(mithraAgent,"./hostsNode1"));
            // Set Agent
            initNode.addAgent("agentePrincipalNodo1",mithraAgent);
            // Start Node
            initNode.start();



            // Start Agent
            initNode initNode2 = new initNode(name_Platform,ip_platform,port_platform);
            initNode2.changeNamePlatform("Node2");
            // Create Agent
            MithraAgent mithraAgent2 = new MithraAgent("./log/loginNode2.log");
            // Create and Add observers and services
            mithraAgent2.addBehaviour(new reciveNewHostFile(mithraAgent2,"./hostsNode2"));
            // Set Agent
            initNode2.addAgent("agentePrincipalNodo2",mithraAgent2);
            // Start Node
            initNode2.start();


        }catch (Exception ex){
            System.out.println("ERROR");
        }

    }
}
