
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.io.File;


/**
 *
 * @author Adrian Ruiz Lopez
 */
public class sshAgent extends Agent {
    protected File sshFile;         // Archivo SSH que va a usar el agente.
    private LogRepository dlogger;  // Sistema de Log del agente.
    private String pathLogFile;     // Direccion del archivo de log del agente.
    private String pathSshFile;     // Direccion del archivo de SSH del agente.
    private String name;            // Nombre del Agente.
    

    @Override
    protected void setup(){
        // Obtenemos el nombre del agente para el log
        String[] nameIP = getName().split("@");
        this.name = nameIP[0];

        // Obtenemos los parametros del agente.
        if( !obtenerParametros() ){
            System.out.println("ERROR --> Obtencion de Argumentos incorrecto.");
            System.exit(1);
        }

        // Iniciamos el sistema de log:
        if( !iniciarLogger(pathLogFile) ){
            System.out.println("ERROR --> Inicio de Logger incorrecto.");
            System.exit(1);
        }

        // Register Service Agent in Platform:
        if( !registrar("ssh") ){
            System.out.println("ERROR --> Registro del agente incorrecto.");
            System.exit(1);
        }


        // Añadimos el comportamiento principal:
        addBehaviour( new ObserverFileSSH(this,2000) );

        
    }
    
    @Override
    protected void takeDown(){
        darBaja();
    }
    

    
    /***************************************************************************/
    
    
    private boolean obtenerParametros(){
        boolean result = false;
        Object[] args = getArguments();
        if(args == null || args.length == 0){
            System.out.println("ERROR: Falta de Argumentos.");
            return result;
        }
        
        try {
            // Obtener path del archivo log:
            this.pathLogFile = (String) args[0];
            // Obtener path del archivo de SSH:
            this.pathSshFile = (String) args[1];
            result = true;
        } catch (Exception e) {
            System.out.println("ERROR: Obtención de Argumentos incorrecta.");
            result = false;
        }
        return result;
   
    }// Fin funcion obtenerParametros



    protected boolean registrar(String rol){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addLanguages("Spain");
        ServiceDescription servicio = new ServiceDescription();
        servicio.setType(rol);
        servicio.setName(rol); // en nuestro caso, le ponemos el mismo nombre.
        dfd.addServices(servicio);
        
        try{
            DFService.register(this, dfd);
            log("Registro completado correctamente con el ROL=\""+rol+"\"");
            return true;
        }catch(Exception e){
            log("ERROR --> No se pudo regista al agente." + e);
            return false;
        }
    }// Fin funcion de registrar.
    
    
    
    protected void darBaja(){
        try{
            DFService.deregister(this);
            log("Se ha dado de baja de la plataforma.");
        }catch(FIPAException fe){
            log("No se ha podido eliminar por completo el agente de la plataforma");
        }
    }
    
    
    
    
    public boolean iniciarLogger(String dir){
        this.dlogger = new LocalLogRepository(dir);
        if (!this.dlogger.Init()){
            System.err.println("ERROR: Inicio de logger incorrecto.");
            return false;
        }
        return true;
    }
    
    
    public void log(String mensaje){
        if( dlogger.isValid() ) dlogger.AddLog(mensaje);
        else{
            System.err.println("ERROR: log realizado sin iniciar logger.");
        }
    }
    
    
    
}// Fin clase de Agente
