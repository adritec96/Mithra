package Mithra;

import Mithra.core.ServerAgent;
import Mithra.hostFile.sendHostFile;
import Mithra.utils.Config;
import com.google.gson.Gson;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.InetAddress;


/**
 *
 * @author adrij
 */
public class InitServerAgent {


    public static void main(String[] args) {
        String hostName = null;
        String hostIp = null;
        String pathConfigFile;
        Config config = null;
        
        // Getting name and ip host:
        try{
            InetAddress address = InetAddress.getLocalHost();
            hostName = address.getHostName();
            hostIp = address.getHostAddress();
        }catch(Exception e){
            System.out.println("ERROR: Hostname or ip not found.");
            System.exit(1);
        }
        
       // Caption to arguments:
        if( args == null || args.length != 1 ){
            System.out.println("ERROR: Argumentos erroneos");
            System.exit(1);
        }
        pathConfigFile = args[0];


        // read configuration file:
        try{
            String cadena;
            StringBuilder archivo = new StringBuilder();
            FileReader f = new FileReader(pathConfigFile);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                archivo.append(cadena);
            }
            b.close();
            // Serialised from json file:
            Gson gson = new Gson();
            config = gson.fromJson( archivo.toString() , Config.class);
            assert( config != null);
        }catch(Exception e){
            System.out.println("ERROR -> Can't read configuration file.");
            System.exit(1);
        }



        // START SERVER:
        Profile p = new ProfileImpl(false);
        p.setParameter(Profile.PLATFORM_ID, config.PLATFORM_ID );
        p.setParameter(Profile.MAIN_HOST, hostIp );
        p.setParameter(Profile.LOCAL_HOST, hostIp );
        p.setParameter(Profile.MAIN_PORT, config.MAIN_PORT );
        p.setParameter(Profile.LOCAL_PORT,  config.MAIN_PORT );
        p.setParameter(Profile.GUI, "true");
        try {
            ServerAgent serverAgent = new ServerAgent(config.AGENT_LOG_FILE);
            serverAgent.addBehaviour( new sendHostFile(serverAgent,"hosts"));
            ContainerController containerController = jade.core.Runtime.instance().createMainContainer(p);
            AgentController ac = containerController.acceptNewAgent("serverAgent-" + hostName, serverAgent);
            ac.start();
        }catch (Exception e){
            System.out.println("Error al iniciar el server:\n"+e);
        }

        
    }
}
