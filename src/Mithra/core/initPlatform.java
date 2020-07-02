package Mithra.core;

import jade.core.Profile;
import jade.core.ProfileImpl;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class initPlatform {
    private  String PLATFORM_ID;
    private  String HOST_IP;
    private  String PLATFORM_PORT;


    public initPlatform(String platformId,String platformPort ) throws UnknownHostException {
        PLATFORM_ID = platformId;
        PLATFORM_PORT = platformPort;

        // Get hostname and ip for the Computer
        InetAddress address = InetAddress.getLocalHost();
        HOST_IP = address.getHostAddress();
    }


    public void start(){
        // START SERVER:
        Profile p = new ProfileImpl(false);
        p.setParameter(Profile.PLATFORM_ID, PLATFORM_ID );
        p.setParameter(Profile.MAIN_HOST, HOST_IP );
        p.setParameter(Profile.LOCAL_HOST, HOST_IP );
        p.setParameter(Profile.MAIN_PORT, PLATFORM_PORT );
        p.setParameter(Profile.LOCAL_PORT,  PLATFORM_PORT );
        p.setParameter(Profile.GUI, "true");
        try {
            jade.core.Runtime.instance().createMainContainer(p);
        }catch (Exception e){
            System.out.println("Error al iniciar el server:\n"+e);
        }
    }
}
