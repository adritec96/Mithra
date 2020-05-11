package sshSecurity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LocalIpRepository implements IpRepository {
    private File file;
    private boolean valid;
    private ArrayList<String> data;

    public LocalIpRepository(String filename ) {
        // Realizamos la apertura del archivo donde meteremos la ips
        valid = true;
        file = new File(filename);
        if ( !file.exists() || !file.isFile() ){
            try{
                if( !file.createNewFile() ) valid = false;
            }catch (IOException ioe){
                System.out.println("ERROR -> No se ha podido crear el archivo de ips");
            }
        }
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public ArrayList<String> getContent() {
        return data;
    }

    @Override
    public boolean eraseContent() {
        ///////////////////////////////////////////////////// BORRAR EL CONTENIDO DEL ARCHIVO
        data.clear();
        return false;
    }

    @Override
    public boolean modifyContent( ArrayList<String> newContent) {
        ///////////////////////////////////////////////////// SOBREESCRIBIR CONTENIDO DEL ARCHIVO
        data = newContent;
        return false;
    }

    @Override
    public boolean findIp(String ip) {
        return data.contains(ip);
    }

    @Override
    public boolean deleteIP(String ip) {
        //////////////////////////////////////////////////// OBTENER CONTENIDO, BORRAR LA IP Y VOLVER A ESCRIBIR.
        data.remove(ip);
        return false;
    }

    @Override
    public boolean addIP(String ip) {
        data.add(ip);
        return modifyContent(data);
    }
}
