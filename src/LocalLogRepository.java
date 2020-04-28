import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;



public class LocalLogRepository implements LogRepository{
    private String filename;
    private boolean valid;
    
    public LocalLogRepository() {
        filename="defaultlog.json";
        valid = false;
    }
    
    public LocalLogRepository(String file) {
        filename = file;
        valid = false;    
    }
    
    public LocalLogRepository(String path, String file) {
        filename = path+file;
        valid = false;    
    }
    
   
    @Override
    public boolean isValid(){
        return valid;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////// se puede arreglar este metodo y ponerlo con condiciones de if negadas.
    @Override
    public boolean Init()  {
        File file;

        file = new File(filename);
        if (file != null)  ////////////////////////////////////////////////// me dice que esto nunca va a ser null
            if (file.exists())
                if (file.isFile())
                    return (valid = true);    // Fichero existe
                else
                    return false;   // Es Directorio
            else   {
                try {
                     if( file.createNewFile() ){
                         return (valid=true); // Fichero nuevo OK
                     }else{
                         return false;
                     }
                } catch (IOException ex) {
                    return false;   // Fichero nuevo MAL
                }
            }
        else
           return false;            // null
    }
    
    @Override
    public String AddLog(String s) {
        if (valid)  {
            String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            PrintWriter outfile;
            try {
                outfile = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            } catch (IOException ex) {
                return "";
            }
            BufferedWriter out = new BufferedWriter(outfile);
            String toRecord="{\"date\":\""+timeStamp+"\", \"value\":\""+s+"\"}"; 
            outfile.println(toRecord);
            //System.out.println(toRecord);
            outfile.close();
            return s;
        }
        else
            return "";
    }
    

}

    
