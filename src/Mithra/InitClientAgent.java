package Mithra;

import Mithra.core.ClientAgent;
import com.google.gson.*;
import Mithra.hostFile.checkHostFile;
import Mithra.hostFile.reciveNewHostFile;
import Mithra.utils.Config;
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
public class InitClientAgent {


    public static void main(String[] args) {
        String hostName = "";
        String hostIp = "";
        String pathConfigFile;
        Config config = null;
        
        // Obtenemos el nombre del equipo y la direccion ip
        try{
            InetAddress address = InetAddress.getLocalHost();
            hostName = address.getHostName();
            hostIp = address.getHostAddress();
        }catch(Exception e){
            System.out.println("ERROR: No se ha podido obtener el  Hostname/Ip");
            System.exit(1);
        }
        
       // Realizamos la captacion de argumentos:
        if( args == null || args.length != 1 ){
            System.out.println("ERROR: Argumentos erroneos");
            System.exit(1);
        }
        pathConfigFile = args[0];
        
        
        // Lectura del archivo de configuración:
        try{
            // Leemos el archivo de configuracion:
            String cadena;
            StringBuilder archivo = new StringBuilder();
            FileReader f = new FileReader(pathConfigFile);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                archivo.append(cadena);
            }
            b.close();
            // Serializamos desde json:
            Gson gson = new Gson();
            config = gson.fromJson( archivo.toString() , Config.class);
            assert(config != null);
        }catch(Exception e){
            System.out.println("ERROR: No se ha podido leer el archivo de configuracion.\n" + e);
            System.exit(1);
        }




        // Start Client agent:
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.PLATFORM_ID, config.PLATFORM_ID  );
        p.setParameter(Profile.MAIN_HOST, config.MAIN_HOST );
        p.setParameter(Profile.LOCAL_HOST, hostIp );
        p.setParameter(Profile.CONTAINER_NAME, hostName );
        p.setParameter(Profile.MAIN_PORT, config.MAIN_PORT );
        p.setParameter(Profile.GUI, "true" );
        ContainerController containerController = rt.createAgentContainer(p);
        AgentController agentController;
        try{
            ClientAgent clientAgent = new ClientAgent(config.AGENT_LOG_FILE);
            clientAgent.addBehaviour( new checkHostFile(clientAgent,15000,"hosts2"));  ///////////////////////////////////// ponerlos en el Config el nombre del archivo
            clientAgent.addBehaviour( new reciveNewHostFile(clientAgent,"hosts2"));
            agentController = containerController.acceptNewAgent("clientAgent-"+hostName,clientAgent);
            agentController.start();
        }catch( Exception e){
            System.out.println("ERROR -> Agent failed to start \""+hostName+"\"");
        }
        
    }// End MAIN
}
