package Mithra.sshFile;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RamIpRepository implements IpRepository{
    Map<String, Date> ipMap;

    public RamIpRepository() {
        this.ipMap = new HashMap<>();
    }

    @Override
    public boolean isValid() {
        if( ipMap == null ) return init();
        return true;
    }

    @Override
    public boolean init() {
        try {
            if( this.ipMap == null ) this.ipMap = new HashMap<>();
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    @Override
    public void addIp(String ip) {
        Date fechaActual = new Date();
        ipMap.put(ip,fechaActual);
    }

    @Override
    public void delete(String ip) {
        ipMap.remove(ip);
    }

    @Override
    public void update(String ip) {
        if( exists(ip) ){
            Date fechaActual = new Date();
            ipMap.put(ip,fechaActual);
        }
    }

    @Override
    public boolean exists(String ip) {
        return ipMap.containsKey(ip);
    }

    @Override
    public JsonArray getOldIps(int minutes) {
        JsonArray result = new JsonArray();
        int mili = minutes * 60000;
        for (String key: ipMap.keySet()) {
            if( ipMap.get(key).getTime() >= new Date().getTime() - mili ) result.add(key);
        }
        return result;
    }


}
