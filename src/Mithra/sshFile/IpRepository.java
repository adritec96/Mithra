package Mithra.sshFile;

import com.google.gson.JsonArray;


public interface IpRepository {
    // Check if repository is valid ( if repository is available to insert.. select.. modify..)
    boolean isValid();
    // Start repository (open file, load DB connection...)
    boolean init();
    // Add new log entry in log repository.
    void addIp(String ip);
    // Delete ip
    void delete( String ip );
    // Update ip
    void update(String ip);
    // Return true if exit ip
    boolean exists(String ip);
    // Get the ips at which your ban time has expired
    JsonArray getOldIps (int minutes);

}
