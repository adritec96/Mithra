package Mithra.utils;


import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class LocalLogRepository implements LogRepository {
    private final String filename;
    private boolean valid;

    public LocalLogRepository() {
        filename = "defaultlog.json";
        valid = false;
    }

    public LocalLogRepository(String file) {
        filename = file;
        valid = false;
    }

    public LocalLogRepository(String path, String file) {
        filename = path + file;
        valid = false;
    }


    @Override
    public boolean isValid() {
        return valid;
    }


    @Override
    public boolean Init() {
        File file = new File(filename);

        if (file == null) return false;                        // FALSE: fichero null, se ha producido un error
        if (file.exists()) {
            if (!file.isFile()) return false;                  // FALSE: es un directorio
            return (valid = true);                                // TRUE: Fichero existe!
        } else {
            try {
                if (!file.createNewFile()) return false;       // FALSE: fichero no se ha podido crear
                return (valid = true);                            // TRUE: fichero creado correctamente
            } catch (IOException ex) {
                return false;                                   // FALSE: error del SO al crear archivo
            }
        }

    }

    @Override
    public boolean AddLog(String subProccess,String message) {

        if( !valid ) return false;

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        PrintWriter outfile;
        try {
            outfile = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
        } catch (IOException ex) {
            return false;
        }
        JsonObject objeto = new JsonObject();
        objeto.addProperty("date",timeStamp);
        objeto.addProperty("value",message);
        objeto.addProperty("subProc",subProccess);
        outfile.println( objeto.toString() );
        outfile.close();
        return true;
    }


}

    
