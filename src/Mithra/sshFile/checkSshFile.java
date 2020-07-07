package Mithra.sshFile;

import Mithra.core.MithraAgent;
import Mithra.core.MithraObserver;
import com.google.gson.JsonArray;
import jade.core.AID;
import jade.lang.acl.ACLMessage;


public class checkSshFile extends MithraObserver {
    private MithraAgent agn;
    private String filename;
    private IpRepository repo;
    private AID[] serversOnline;

    public checkSshFile(MithraAgent a, long period, String pathSSHLog, IpRepository repo) {
        super(a, period);
        this.agn = a;
        this.filename = pathSSHLog;
        this.repo = repo;
    }

    @Override
    protected void onTick() {
        agn.log("checkSshFile","start checking SshFile");
        // Refresh Server names:
        serversOnline = agn.getCandidates("receiveIps");

        // Si no se encuentran candidatos, se volverá a intenar..
        if( serversOnline == null || serversOnline.length < 1) return;

        // Check ssh file, analyze possible attacks
        JsonArray badIps = readFile();

        // Send danger ips to other Clients:
        sendBadIps(badIps);

        // check repo and free ips
        JsonArray goodIps = repo.getOldIps(5);
        sendGoodIps(goodIps);

    }

    private JsonArray readFile(){
        JsonArray result = new JsonArray();
        // Sustitución de este ejemplo, por la lectura de un archivo de log SHH,  ///////////////////////////////// ARREGLARLO???
        // obteniendo las ips que están atacando el sistema.
        int rand = (int) (Math.random()*20+10);
        result.add("192.168.1."+rand);
        return result;
    }

    private void sendBadIps(JsonArray ips){
        agn.log("checkSshFile","Send Bad ips to other clients");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        for(AID aid : serversOnline ){
            msg.addReceiver(aid);
        }
        msg.setLanguage("English");
        msg.setConversationId("sshIPs");
        msg.setContent(ips.toString());
        agn.send(msg);
    }


    private void sendGoodIps(JsonArray ips){
        agn.log("checkSshFile","Send good ips to other clients");
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        for(AID aid : serversOnline ){
            msg.addReceiver(aid);
        }
        msg.setLanguage("English");
        msg.setConversationId("sshIPs");
        msg.setContent(ips.toString());
        agn.send(msg);
    }



}
