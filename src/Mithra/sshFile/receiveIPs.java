package Mithra.sshFile;

import Mithra.core.MithraAgent;
import Mithra.core.MithraService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class receiveIPs extends MithraService {
    private MithraAgent agn;
    private IpRepository repo;

    public receiveIPs(MithraAgent agn,IpRepository repo) {
        super(agn);
        this.agn = agn;
        this.repo = repo;
        this.agn.addAgentService("receiveIps");
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchConversationId("sshIPs");
        ACLMessage msg = agn.receive(mt);

        if( msg != null ){
            Gson gson = new Gson();
            JsonArray newIps;
            switch (msg.getPerformative()) {
                case ACLMessage.REQUEST:
                    // Receive new bad Ips
                    agn.log("reciveIPs", "A message was received with the new BAD ips and they were blocked");
                    // Get BAD Ips:
                    newIps = gson.fromJson(msg.getContent(), JsonArray.class);
                    // BLock BAD Ips
                    addAtacks(newIps);
                    break;
                case ACLMessage.INFORM:
                    // Receive new GOOD Ips
                    agn.log("reciveIPs", "A message was received with the new GOOD ips and they were unlocked");
                    // Get GOOD Ips:
                    newIps = gson.fromJson(msg.getContent(), JsonArray.class);
                    // BLock BAD Ips
                    freeIps(newIps);
                    break;
            }
        }else{
            // Block While no received message.
            block();
        }

    }

    /**
     * Read the IPs and if it exists we update it and if not, I add it
     */
    private void addAtacks(JsonArray newIps){
        for(JsonElement j : newIps){
            // Add to repository
            String ip = j.getAsString();
            if( repo.exists(ip) ){
                repo.update(ip);
            }else{
                repo.addIp(ip);
            }
            // Block in Firewall                                            ///////////////////////////////////


        }
    }

    private void freeIps(JsonArray newIps){
        for(JsonElement j : newIps){
            // Remove in repository
            String ip = j.getAsString();
            repo.delete(ip);

            // Unlock in Firewall                                           ////////////////////////////////////
        }
    }


}
