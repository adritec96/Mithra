package sshSecurity;

import java.util.Objects;

public class Ocurrencia {
    public String ip;
    public int numeroOcurrencas;
    public String date;

    public Ocurrencia(String ip, int numeroOcurrencas, String date) {
        this.ip = ip;
        this.numeroOcurrencas = numeroOcurrencas;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ocurrencia)) return false;
        Ocurrencia that = (Ocurrencia) o;
        return ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip);
    }
}
