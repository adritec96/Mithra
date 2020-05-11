package sshSecurity;

import java.util.ArrayList;

public class ListaOcurrencias {
    private ArrayList<Ocurrencia> ocurrencias;

    public ListaOcurrencias() {
        ocurrencias = new ArrayList<>();
    }

    public void addOcurrencia(Ocurrencia newOcurrence){
        int index = ocurrencias.indexOf(newOcurrence);
        if( index != -1 ){
            ocurrencias.get(index).numeroOcurrencas++;
        }else{
            ocurrencias.add( newOcurrence );
        }
    }

    public void addOcurrencia(String ip, String date){
        Ocurrencia newOcurrence = new Ocurrencia(ip,1,date);
        this.addOcurrencia(newOcurrence);
    }

    public Ocurrencia get(int index){
        return ocurrencias.get(index);
    }

    public int size(){
        return ocurrencias.size();
    }

    public ListaOcurrencias overloadOcurrences(int max){
        ListaOcurrencias result = new ListaOcurrencias();
        for(Ocurrencia ocu : ocurrencias){
            if( ocu.numeroOcurrencas > max ) result.addOcurrencia(ocu);
        }
        return result;
    }


}
