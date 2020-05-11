package sshSecurity;

import Mithra.core.ClientAgent;
import jade.core.behaviours.TickerBehaviour;

import java.io.*;


/**
 * @author Adrián Ruiz Lopez
 */
public class ObserverFileSSH extends TickerBehaviour {
    private ClientAgent agent;
    private int state;
    private static final int OBSERVANDO_FICHERO = 0;
    private static final int REACTIVAR_IPS = 1;
    private static final int BAN_IPS = 2;
    private static final int SEND_IPS = 3;
    private static final int WAIT = 4;

    private static final int SEGUNDOS = 10;
    private final int LANES = 100;
    private final int INTENTOS = 3;

    private File tempFileSsh;
    private File fileSSH;

    private final String PATHTEMPSHHFILE = "tempsshfile.txt";
    private final String PATHBANIP = "banips.txt";
    private final String PATHREALLOW = "reAllowIps.txt";
    private final String PATHSSH = "sshlog.txt";
    private IpRepository banIps;
    private final IpRepository reAllowIPs;



    public ObserverFileSSH(ClientAgent agn, int milisec) {
        super(agn, milisec);
        this.agent = agn;
        //this. = agn.sshFile;      /////////////////////////// pongo en el behabiour los tipos File o los pongo en el tipo
        if ( (this.fileSSH = ObtenerArchivoTemporalSSH(PATHTEMPSHHFILE)) == null){
            stop();
        }
        if ( (this.tempFileSsh = ObtenerArchivoTemporalSSH(PATHTEMPSHHFILE)) == null){
            stop();
        }
        this.banIps = new LocalIpRepository(PATHBANIP);
        this.reAllowIPs = new LocalIpRepository(PATHREALLOW);
        this.state = OBSERVANDO_FICHERO;
    }

    @Override
    protected void onTick() {
        switch (state){
            case OBSERVANDO_FICHERO:
                observarFichero(fileSSH);
                break;
            case REACTIVAR_IPS:
                //reactivarIPs();
                break;
            case BAN_IPS:
                break;
            case SEND_IPS:
                break;
            case WAIT:
                break;
        }

    }


    private File ObtenerArchivoTemporalSSH( String path ){
        File tempSsh = new File(path);
        if( !tempSsh.exists() ){
            try {
                if( !tempSsh.createNewFile() ) return null;
            }catch (Exception ex){
                agent.log("observerFileSHH","ERROR -> no se ha podido crear el archivo temporal de ssh");
                return null;
            }
        }
        return tempSsh;
    }



    private void observarFichero(File file){
        /////////// Obtenemos el contenido de las ultimas X LANES
        String content = tail(file,LANES);

        /////////// Copiamos el contenido en nuestro fichero temporal.
        try{
            PrintWriter writer = new PrintWriter(tempFileSsh);
            writer.print(content);
            writer.close();
        }catch(Exception e){
            agent.log("observerFileSHH","ERROR -> no se ha podido realizar la copia al archivo temporal");
            stop();
        }

        /////////// Extraemos las ips que sobrepasan los intentos del archivo temporal:
        OcurrencesSearch occurrences = new OcurrencesSearch(PATHTEMPSHHFILE);
        ListaOcurrencias newAttackerIPs = occurrences.search(INTENTOS,SEGUNDOS);

        ////////// si es mayor que 1 las ocurrencias
        banIps.eraseContent();

        for(int i=0; i<newAttackerIPs.size(); i++){
            String ip = newAttackerIPs.get(i).ip;
            banIps.addIP(ip);
        }

    } // fin de observarFichero






    private String tail( File file, int lines) {
        RandomAccessFile fileHandler = null;
        try {
            fileHandler = new RandomAccessFile( file, "r" );
            long fileLength = fileHandler.length() - 1;
            StringBuilder sb = new StringBuilder();
            int line = 0;

            for(long filePointer = fileLength; filePointer != -1; filePointer--){
                fileHandler.seek( filePointer );
                int readByte = fileHandler.readByte();

                if( readByte == 0xA ) {
                    if (filePointer < fileLength) {
                        line = line + 1;
                    }
                } else if( readByte == 0xD ) {
                    if (filePointer < fileLength-1) {
                        line = line + 1;
                    }
                }
                if (line >= lines) {
                    break;
                }
                sb.append( ( char ) readByte );
            }

            return sb.reverse().toString();
        } catch( FileNotFoundException e ) {
            e.printStackTrace();
            return null;
        } catch( IOException e ) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (fileHandler != null )
                try {
                    fileHandler.close();
                } catch ( IOException e) {
                }
        }
    }



}
