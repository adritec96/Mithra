
import com.google.gson.*;
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
public class initAgentSsh {


    public static void main(String[] args) {
        String pepe;
        String hostName = "";
        String hostIp = "";
        Config config = new Config(); ////////////////////////////////////////////////////////  mirar forma de no implejmentarlo asi.. y ponerlo con unos valores por defecto o algo.
        
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
        
        
        // Lectura del archivo de configuración:
        try{
            // Leemos el archivo de configuracion:
            String cadena;
            StringBuilder archivo = new StringBuilder();
            FileReader f = new FileReader("src/myConfig.json"); ////////////////////////////////////////////////////// Abra que cambiarlo de ubicacion en algún momento.
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                archivo.append(cadena);
            }
            b.close();
            // Serializamos desde json:
            Gson gson = new Gson();
            config = gson.fromJson( archivo.toString() , Config.class);
        }catch(Exception e){
            System.out.println("ERROR: No se ha podido leer el archivo de configuracion.\n" + e);
            System.exit(1);
        }
        

        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  terminar primero el server!!!
        // Realizamos el inicio del agente: 
        jade.core.Runtime rt = jade.core.Runtime.instance();
        Profile p = new ProfileImpl();
        p.setParameter(Profile.PLATFORM_ID, config.PLATFORM_ID  );
        p.setParameter(Profile.MAIN_HOST, config.MAIN_HOST );
        p.setParameter(Profile.LOCAL_HOST, hostIp );
        p.setParameter(Profile.MAIN_PORT, config.MAIN_PORT );
        p.setParameter(Profile.GUI, "true");
        ContainerController cc = rt.createAgentContainer(p);
        AgentController ac;
        Object[] argumentos = { config.AGENT_LOG_FILE , config.SSH_FILE };
        try{
            ac = cc.createNewAgent("sshAgent-"+hostName, sshAgent.class.getName() , argumentos );
            ac.start();
        }catch( Exception e){
            System.out.println("No se ha podido iniciar el agente \""+hostName+"\"");
        }
        
    }
}
