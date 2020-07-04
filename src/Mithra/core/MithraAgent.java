package Mithra.core;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import Mithra.utils.LocalLogRepository;
import Mithra.utils.LogRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MithraAgent extends Agent {
    private LogRepository log;                      // Object for  log repository.
    private final String pathLogFile;               // Path to log file.
    ArrayList<ServiceDescription> OfferServices;    // Services offered
    Map<String,AID[]> reqServices;                  // Required services

    public MithraAgent(String pathLogFile) {
        this.OfferServices = new ArrayList<>();
        this.reqServices = new HashMap<>();
        this.pathLogFile = pathLogFile;
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Iniciando agente " + getName());
        // Start log system:
        if( (log = startLogger(pathLogFile)) == null ){
            System.out.println("ERROR --> Start logger.");
            System.exit(1);
        }

        // Register Service Agent in Platform:
        if( !subcribePlatform() ){
            log("agent","ERROR --> Register agent in platform.");
            takeDown();
        }

        // Refresh Required Services
        addBehaviour(new TickerBehaviour(this,100000) {           /////////////// implementar y quizá quitar el primero.
            @Override                                           /////////////// Para ello, no tiene que fallar si se
            protected void onTick() {
                actualizarServicios();
            }
        });




    }
    /*
     * Añade un servicio a la lista de servicios que ofrece el agente.
     * @author Adrián Ruiz Lopez
     * @param nameService Nombre del servicio que se desea añadir
     */
    public void addAgentService(String nameService ){
        try{
            ServiceDescription serv = new ServiceDescription();
            serv.setName(nameService);
            serv.setType("service");
            OfferServices.add(serv);
        }catch (Exception e){
            log("agent"," ERROR -> adding service to agent");
            System.exit(1);
        }
    }
    /*
     * Añade un nuevo servicio al Map de servicios requeridos por el agente.
     * @author Adrián Ruiz Lopez
     * @param nameService Nombre del servicio que se desea añadir
     */
    public void requireService(String nameService){
        reqServices.put(nameService,new AID[0]);
    }
    /*
     * Realiza una actualización de los servicios requeridos por el agente.
     * @author Adrián Ruiz Lopez
     */
    private void actualizarServicios(){
        System.out.println("Actualizando servicios");
        for (String key : reqServices.keySet()) {
            AID[] nuevaLista = askAgents(key);
            reqServices.put(key,nuevaLista);
        }
    }

    /*
     * Devuelve una lista con los candidatos que ofrecen un servicio. Si está en caché, lo devuelve directamente,
     * si no, realiza una petición al agente DF y actualiza la cache para su proxima consulta.
     * @author Adrián Ruiz Lopez
     * @return Un vector de AID ( Agent ID ). Si no existe de ninguna manera, devuelve un vetor vacio.
     * @param nameService Nombre del servicio que desea buscar
     */
    public AID[] getCandidates(String nameService){
        AID[] result = reqServices.get(nameService);
        if( result == null ){
            requireService(nameService);
            actualizarServicios();
            result = reqServices.get(nameService);
            if ( result == null ) return new AID[0];
        }
        return result;
    }
    /*
     * Suscribe el agente a la plataforma leyendo todos los servicios que ofrece y añadiendolos al Agente DF.
     * @author Adrián Ruiz Lopez
     * @return True si se ha podido realizar la subcripción o False si no se ha podido realizar.
     */
    protected boolean subcribePlatform(){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addLanguages("Spain");
        for (ServiceDescription sd : OfferServices){
            dfd.addServices(sd);
            log("agent"," Agent start service: " + sd.getName() );
        }

        try{
            DFService.register(this, dfd);
            log("agent"," Agent registered successful");
            return true;
        }catch(Exception e){
            log("agent","ERROR --> Fail to register agent from platform.");
            return false;
        }
    }
    /*
     * Elimina la subcripción a la plataforma.
     * @author Adrián Ruiz Lopez
     */
    protected void unSucribePlatform(){
        try{
            DFService.deregister(this);
            log("agent","Unsubscribed from the platform.");
        }catch(FIPAException fe){
            log("agent","ERROR -> Failed to completely remove agent from platform.");
        }
    }
    /*
     * Inicializa el sistema de log del agente.
     * @author Adrián Ruiz Lopez.
     * @return Una instancia de LogRepository si se inició correctamente o null si no lo hizo.
     * @param dir Directorio donde se almacenará el Log del agente.
     */
    protected LogRepository startLogger(String dir){
        LogRepository log = new LocalLogRepository(dir);
        if (!log.init()){
            System.err.println("ERROR -> Start logger.");
            return null;
        }
        return log;
    }
    /*
     * Realiza una entrada nueva en el log del agente
     * @author Adrián Ruiz Lopez
     * @param subProccess Nombre identificativo para controlar de que subProceso del agente es la entrada.
     * @param message Mensaje que deseamos guardar en el log.
     */
    public void log(String subProccess, String message){
        if( !log.isValid() ) System.err.println("ERROR -> action \"log\" without starting logger.");
        if ( !log.addLog(subProccess,message) ) System.out.println("ERROR -> can't log message.");
    }
    /*
     * Metodo que se ejecuta al final de la vida del agente
     * @author Adrián Ruiz Lopez
     */
    @Override
    protected void takeDown(){
        super.takeDown();
        // UnSubcribe to teh platform
        unSucribePlatform();
    }
    /*
     * Consulta al agente DF para obtener una lista de los agentes de acuerdo a una plantilla.
     * @author Adrián Ruiz Lopez.
     * @return Un Vector con los AIDs que corresponden a la plantilla. Si no existen se devuelve un vector vacio.
     * @param template Plantilla que tienen que cumplir los resultados.
     */
    private AID[] getListNamesAgents(DFAgentDescription template){
        try{
            DFAgentDescription[] agents = DFService.search(this,template);
            AID[] result = new AID[agents.length];
            for(int i=0; i<agents.length; i++){
                result[i] = agents[i].getName();
            }
            return result;
        }catch ( FIPAException e){
            log("agent","ERROR -> not find agents");
            return new AID[0];
        }
    }
    /*
     * Crea una plantilla y realiza una llamada a getListNamesAgents.
     * @author Adrián Ruiz Lopez.
     * @return Un Vector con los AIDs del servicio
     * @param serviceName Nombre del servicio que se necesita
     */
    private AID[] askAgents(String serviceName) {
        // Create template to search names agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("service");
        sd.setName(serviceName);
        template.addServices(sd);
        return getListNamesAgents(template);
    }

    public boolean isValid() { return true; } ////////////////////////////////////////////////////////////////////////////////




}
