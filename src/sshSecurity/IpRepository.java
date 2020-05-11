package sshSecurity;

import java.util.ArrayList;

public interface IpRepository {
    boolean isValid();
    ArrayList<String> getContent();
    boolean eraseContent();
    boolean modifyContent(ArrayList<String> newContent);
    boolean findIp(String ip);
    boolean deleteIP(String ip);
    boolean addIP(String ip);
}
