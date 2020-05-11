package sshSecurity;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OcurrencesSearch {
    private String file;

    public OcurrencesSearch(String filename) {
        this.file = filename;
    }

    public ListaOcurrencias search(int max, int second){
        ListaOcurrencias occurrences = new ListaOcurrencias();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line, last_line = "", ip, format = "yyyy MMM dd HH:mm:ss";
            Date date, current_date, last_cycle;
            Calendar calendar;
            String [] parts;

            calendar = Calendar.getInstance();
            current_date = calendar.getTime();
            calendar.setTime(current_date);
            String year = Integer.toString(calendar.get(Calendar.YEAR));

            last_cycle = new Date(current_date.getTime() - second * 1000);

            while ((line = br.readLine()) != null) {
                if( line.contains("sshd")){
                    if( line.contains("refused connect") || line.contains("Invalid user") ||
                            ( line.contains("Failed password") && last_line.contains("sshd") &&  last_line.contains("last message repeated"))){
                        //Checking date and time
                        parts = line.split(" ");

                        //First we take the month, day and time
                        String month = parts[0];
                        String day, time;
                        if( StringUtils.isNumeric(parts[1])){
                            day = parts[1];
                            time = parts[2];
                        }
                        else{   //Sometimes we have to get pos 2
                            day = parts[2];
                            time = parts[3];
                        }

                        //Now we change the string into a valid date
                        switch(month){
                            case "Jan":
                                month = "Ene";
                                break;
                            case "Apr":
                                month = "Abr";
                                break;
                            case "Aug":
                                month = "Oct";
                                break;
                            case "Dec":
                                month = "Dic";
                                break;
                        }

                        date = new SimpleDateFormat(format).parse(year+" "+month+" "+day+" "+time);

                        //If the date with the current year is after the current date
                        //(when the current year changed), we take the last year before this one
                        if(date.after(current_date)){
                            year = Integer.toString(calendar.get(Calendar.YEAR)-1);
                            date = new SimpleDateFormat(format).parse(year+" "+month+" "+day+" "+time);
                        }

                        //Finally, we compare if the new date and time are between this cycle
                        //and the last one
                        if(date.after(last_cycle) && date.before(current_date)){

                            //Taking the IP
                            if(line.contains("refused connect")){
                                ip = line.substring(line.indexOf("(") + 1);
                                ip = ip.substring(0, ip.indexOf(")"));
                            }
                            else if(line.contains("Invalid user")){
                                ip = parts[parts.length-1];
                            }
                            else if(line.contains("Failed password") &&
                                    last_line.contains("sshd") &&
                                    last_line.contains("last message repeated")){
                                ip = parts[parts.length-4];
                            }
                            else{
                                ip = "";
                                System.out.println("ERROR TAKING IP (no matches found)");
                            }

                            if(!ip.equals("")){
                                occurrences.addOcurrencia(ip,new SimpleDateFormat(format).format(date));
                            }

                        }
                    }
                    last_line = line;
                }
                br.close();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return occurrences.overloadOcurrences(max);
    }

}
