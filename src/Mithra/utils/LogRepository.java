package Mithra.utils;

/**
 *
 * @author Adrián Ruiz Lopez
 */
public interface LogRepository {
    // Check if repository is valid ( if repository is available to insert.. select.. modify..)
    boolean isValid();
    // Start repository (open file, load DB connection...)
    boolean init();
    // Add new log entry in log repository.
    boolean addLog( String subProccess,String message);
}
