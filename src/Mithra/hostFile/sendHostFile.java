package Mithra.hostFile;

import Mithra.core.MyJadeAgent;
import Mithra.core.ServerAgent;
import Mithra.utils.LectorFiles;
import com.google.gson.JsonArray;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class sendHostFile extends CyclicBehaviour {

    private final ServerAgent agn;
    private final String pathHostFile;

    public sendHostFile(ServerAgent agn, String pathHostFile) {
        this.agn = agn;
        this.pathHostFile = pathHostFile;
        this.agn.addAgentService("server","checkHostFile");
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("HostFile");
        ACLMessage msg = agn.receive(mt);

        if( msg != null ){
            switch (msg.getPerformative()){
                // The client requests the content of the file:
                case ACLMessage.QUERY_REF:
                    agn.log("sendHostFile","Message was received and the contents of the host file are sent");
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent( getContentHostFile() );
                    agn.send(reply);
                    break;
                // The client has a problem in the file and it is necessary to send the other clients
                case ACLMessage.REQUEST:
                    agn.log("sendHostFile","Message was received and notifications will be sent to all clients");
                    // Get clients:
                    AID[] clientsHostFile = agn.askAgents("client","replaceHostFile");
                    ACLMessage msgAll = new ACLMessage(ACLMessage.REQUEST);
                    msgAll.setConversationId("newHostFile");
                    // add all clients
                    for( AID aid : clientsHostFile){
                        msgAll.addReceiver(aid);
                    }
                    String contenido = getContentHostFile();
                    msgAll.setContent(contenido);
                    System.out.println("SEVER ENVIA: " +  contenido );
                    agn.send(msgAll);
                    break;
                default:
                    agn.log("sendHostFile","ERROR,unexpected message convID="+ msg.getConversationId() + "and performative= "+ msg.getPerformative());
                    break;
            }
        }else{
            // Block While no received message.
            block();
        }
    }


    private String getContentHostFile(){
        try{
            JsonArray js = LectorFiles.readHostFile(pathHostFile);
            assert js != null;
            return js.toString();
        }catch (Exception e){
            agn.log("sendHostFile","ERROR load host file => " + e);
            return null;
        }
    }



}
