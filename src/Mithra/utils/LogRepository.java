package Mithra.utils;

/**
 *
 * @author Adrián Ruiz Lopez
 */
public interface LogRepository {
    // Check if repository is valid ( if repository is available to insert.. select.. modify..)
    boolean isValid();
    // Start repository (open file, load DB connection...)
    boolean Init();
    // Add new log entry in log repository.
    boolean AddLog( String subProccess,String message);
}
