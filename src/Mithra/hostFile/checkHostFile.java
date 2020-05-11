package Mithra.hostFile;

import Mithra.core.ClientAgent;
import Mithra.utils.LectorFiles;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


/**
 * @author Adrián Ruiz Lopez
 */
public class checkHostFile extends TickerBehaviour {
    private final int milliseconds;
    private final ClientAgent agn;
    private final String pathHostFile;

    public checkHostFile(ClientAgent agent, int milliseconds,String pathHostFile) {
        super(agent,milliseconds);
        this.milliseconds = milliseconds;
        this.agn = agent;
        this.pathHostFile = pathHostFile;
    }


    @Override
    public void onTick() {
        agn.log("checkHostFile","start checking hostFile");
        // Refresh Server names:
        AID[] serversOnline = refreshServerNames();
        if( serversOnline == null) return;

        AID serverSelected = serversOnline[0];               /////////////////////////////////  Ahora mismo solo lo enviamos al primero. o implementar un call of proposal.

        // Request content Host File:
        requestContentHostFile(serverSelected);

        // Wait Content Host File:
        MessageTemplate mt = MessageTemplate.and( MessageTemplate.and(MessageTemplate.MatchConversationId("HostFile"),
                MessageTemplate.MatchSender(serverSelected)), MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
        ACLMessage msg = agn.receive(mt);
        if( msg != null ){
            // TransForm contain message to jsonObject
            Gson gson = new Gson();
            JsonArray jsonFileRemote = gson.fromJson(msg.getContent(),JsonArray.class);
            String remoteFile = jsonFileRemote != null ? jsonFileRemote.toString() : null;

            // Read Local Hosts File:
            JsonArray jsonFileLocal = LectorFiles.readHostFile(pathHostFile);
            String  localFile = jsonFileLocal != null ? jsonFileLocal.toString() : null;

            // Check equal host file contain received and local contain
            if( localFile == null || remoteFile == null ){
                agn.log("checkHostFile", "ERROR -> Error read local or remote file");
            }else if( !localFile.equals(remoteFile) ){
                // if not are equals, send problem
                sendProblemToServer(msg);
            }else{
                agn.log("checkHostFile","Correct File!");
            }

        }else{
            block(); // block if the message not is received.
        }

    }// end OnTick


    private AID[] refreshServerNames(){
        AID[] serversHostFile = agn.askAgents("server","checkHostFile");
        if( serversHostFile == null || serversHostFile.length < 1 ){
            agn.log("checkHostFile","ERROR -> At least 1 server is required ");
            return null;
        }
        return serversHostFile;
    }

    private void requestContentHostFile(AID serverSelected){
        agn.log("checkHostFile","Request Content Host File");
        ACLMessage msg = new ACLMessage(ACLMessage.QUERY_REF);
        msg.addReceiver(serverSelected);
        msg.setLanguage("English");
        msg.setConversationId("HostFile");
        agn.send(msg);
    }

    private void sendProblemToServer(ACLMessage msg){
        agn.log("checkHostFile","PROBLEM DETECTED: Send result to server");
        ACLMessage reply = msg.createReply();
        reply.setPerformative(ACLMessage.REQUEST);
        reply.setConversationId("HostFile");
        reply.setContent("ERROR");
        agn.send(reply);
    }



}
