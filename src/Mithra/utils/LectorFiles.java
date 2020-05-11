package Mithra.utils;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;

public class LectorFiles {

    public static JsonArray readHostFile(String filename) {
        JsonArray data = new JsonArray();

        try {
            String cadena;
            FileReader f = new FileReader(filename);
            BufferedReader b = new BufferedReader(f);
            while ((cadena = b.readLine()) != null) {
                cadena = cadena.replaceAll("\t", " ").replaceAll("( )+", " ");
                String[] parts = cadena.split(" ");
                if (!parts[0].equals("#") && parts.length > 1) {
                    if (isIP(parts[0]) && isURL(parts[1])) {
                        JsonObject newEntry = new JsonObject();
                        newEntry.addProperty("ip",parts[0]);
                        newEntry.addProperty("domain",parts[1]);
                        data.add(newEntry);
                    }
                }
            }
            return data;
        } catch (Exception e) {
            System.err.println("ERROR AL ACCEDER AL ARCHIVO \n" + e);
            return null;
        }
    }


    private static boolean isIP (String ip) {
        try {
            if ( ip == null || ip.isEmpty() ) {
                return false;
            }

            String[] parts = ip.split( "\\." );
            if ( parts.length != 4 ) {
                return false;
            }

            for ( String s : parts ) {
                int i = Integer.parseInt( s );
                if ( (i < 0) || (i > 255) ) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }


    private static boolean isURL(String url){
        String[] parts = url.split("\\.");
        return parts.length >= 2;
    }


}
