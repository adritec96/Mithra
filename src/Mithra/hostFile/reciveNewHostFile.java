package Mithra.hostFile;

import Mithra.core.ClientAgent;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class reciveNewHostFile extends CyclicBehaviour {

    private final ClientAgent agn;
    private final String path;

    public reciveNewHostFile(ClientAgent agn,String path) {
        this.agn = agn;
        this.path = path;
        this.agn.addAgentService("client","replaceHostFile");
    }

    @Override
    public void action() {
        MessageTemplate cond1 = MessageTemplate.MatchConversationId("newHostFile");
        MessageTemplate cond2 = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        MessageTemplate mt = MessageTemplate.and(cond1,cond2);
        ACLMessage msg = agn.receive(mt);
        if( msg != null ){
            agn.log("receiveNewHostFile","new file was received");
            Gson gson = new Gson();
            JsonArray newContentHostFile = gson.fromJson(msg.getContent(),JsonArray.class);
            System.out.println("NUEVO HOST: "+ newContentHostFile.toString());
            sustituirArchivo(path,newContentHostFile);
        }else{
            block();
        }
    }

    private void sustituirArchivo( String path, JsonArray newContentHostFile){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            for(JsonElement ele : newContentHostFile){
                JsonObject obj = ele.getAsJsonObject();
                System.out.println(obj.toString());
                bw.write(obj.get("ip").getAsString() + " " + obj.get("domain").getAsString() + "\n" );
            }
            bw.close();
        }catch (Exception e){
            agn.log("reciveNewHostFile","ERROR AL SUSTITUIR EL ARCHIVO");
        }
    }

}