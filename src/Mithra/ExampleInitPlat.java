package Mithra;

import Mithra.core.MithraAgent;
import Mithra.core.initPlatform;
import Mithra.hostFile.reciveNewHostFile;
import Mithra.hostFile.sendHostFile;
import Mithra.sshFile.IpRepository;
import Mithra.sshFile.RamIpRepository;
import Mithra.sshFile.checkSshFile;
import Mithra.sshFile.receiveIPs;

public class ExampleInitPlat {

    public static void main(String[] args) {
        //Variables established by the client.
        String name_Platform = "Mithra-Platform";
        String port_platform = "1099"; // agents and platform used 1099
        String ip_platform = "192.168.1.2";
        try {

            // Start Agents Platform
            initPlatform initPlat = new initPlatform(name_Platform,port_platform);
            initPlat.activeGui();
            // Create Agent
            MithraAgent mithraAgent = new MithraAgent("./log/loginplat.log");
            // Create and Add observers and services
            mithraAgent.addBehaviour(new sendHostFile(mithraAgent,"./hosts"));

            initPlat.addAgent("principalNodoPlat",mithraAgent);
            initPlat.start();


        }catch (Exception ex){
            System.out.println("ERROR");
        }

    }
}
