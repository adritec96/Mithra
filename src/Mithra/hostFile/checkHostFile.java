package Mithra.hostFile;

import Mithra.core.MithraAgent;
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
    private final MithraAgent agn;
    private final String pathHostFile;


    public checkHostFile(MithraAgent agent, int milliseconds, String pathHostFile) {
        super(agent,milliseconds);
        this.milliseconds = milliseconds;
        this.agn = agent;
        this.pathHostFile = pathHostFile;
    }


    @Override
    public void onTick() {
        agn.log("checkHostFile","start checking hostFile");
        // Refresh Server names:
        AID[] serversOnline = agn.askAgents("server","checkHostFile");
        if( serversOnline == null || serversOnline.length < 1) return;

        AID serverSelected = serversOnline[0];               /////////////////////////////////  Ahora mismo solo lo enviamos al primero. o implementar un call of proposal.

        // Request content Host File:
        System.out.println("se pide el contenido al server");
        requestContentHostFile(serverSelected);

        // Wait Content Host File:
        MessageTemplate mt = MessageTemplate.and( MessageTemplate.and(MessageTemplate.MatchConversationId("HostFile"),
                MessageTemplate.MatchSender(serverSelected)), MessageTemplate.MatchPerformative(ACLMessage.INFORM) );
        ACLMessage msg = agn.receive(mt);
        if( msg != null ){
            System.out.println("contenido recibido.");
            // TransForm contain message to jsonObject
            Gson gson = new Gson();
            JsonArray jsonFileRemote = gson.fromJson(msg.getContent(),JsonArray.class);
            String remoteFile = jsonFileRemote != null ? jsonFileRemote.toString() : null;

            // Read Local Hosts File:
            JsonArray jsonFileLocal = LectorFiles.readHostFile(pathHostFile);
            String  localFile = jsonFileLocal != null ? jsonFileLocal.toString() : null;

            // Check equal host file contain received and local contain
            System.out.print("Resultado de la comparacion = ");
            if( localFile == null || remoteFile == null ){
                agn.log("checkHostFile", "ERROR -> Error read local or remote file");
            }else if( !localFile.equals(remoteFile) ){
                // if not are equals, send problem
                System.out.println("Error, necesario avisar del error");
                sendProblemToServer(msg);
            }else{
                System.out.println("Correcto!");
                agn.log("checkHostFile","Correct File!");
            }

        }else{
            block(); // block if the message not is received.
        }

    }// end OnTick

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
