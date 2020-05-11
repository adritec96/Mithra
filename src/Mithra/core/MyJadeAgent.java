package Mithra.core;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import Mithra.utils.LocalLogRepository;
import Mithra.utils.LogRepository;

import java.util.ArrayList;

public abstract class MyJadeAgent extends Agent {
    private LogRepository log;                  // Object for  log repository.
    private final String pathLogFile;           // Path to log file.
    ArrayList<ServiceDescription> services;

    public MyJadeAgent(String pathLogFile) {
        this.services = new ArrayList<>();
        this.pathLogFile = pathLogFile;
    }

    @Override
    protected void setup() {
        super.setup();
        // Start log system:
        if( (log = startLogger(pathLogFile)) == null ){
            System.out.println("ERROR --> Start logger.");
            System.exit(1);
        }

    }

    public void addAgentService( String type, String name ){
        try{
            ServiceDescription serv = new ServiceDescription();
            serv.setName(name);
            serv.setType(type);
            services.add(serv);
        }catch (Exception e){
            log("agent"," ERROR -> adding service to agent");
            System.exit(1);
        }


    }

    protected boolean subcribePlatform(){
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addLanguages("Spain");
        for (ServiceDescription sd : services ){
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
    }// Fin funcion de registrar.

    protected void unSucribePlatform(){
        try{
            DFService.deregister(this);
            log("agent","Unsubscribed from the platform.");
        }catch(FIPAException fe){
            log("agent","ERROR -> Failed to completely remove agent from platform.");
        }
    }

    protected LogRepository startLogger(String dir){
        LogRepository log = new LocalLogRepository(dir);
        if (!log.Init()){
            System.err.println("ERROR -> Start logger.");
            return null;
        }
        return log;
    }

    public void log(String subProccess, String message){
        if( !log.isValid() ) System.err.println("ERROR -> action \"log\" without starting logger.");
        if ( !log.AddLog(subProccess,message) ) System.out.println("ERROR -> can't log message.");
    }

    @Override
    protected void takeDown(){
        super.takeDown();
        // UnSubcribe to teh platform
        unSucribePlatform();
    }



    // Get a list of agents according to the template
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
            return null;
        }
    }

    public AID[] askAgents(String serviceType) {
        // Create template to search names agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        template.addServices(sd);
        return getListNamesAgents(template);
    }


    public AID[] askAgents(String serviceType, String serviceName) {
        // Create template to search names agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(serviceType);
        sd.setName(serviceName);
        template.addServices(sd);
        return getListNamesAgents(template);
    }




}
