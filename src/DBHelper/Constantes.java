package DBHelper;

public class Constantes {

    private String ip_db = "192.168.43.176";
    private String port_db = "5432";
    private String url = "jdbc:postgresql://192.168.43.176:5432/redes2";

    private Constantes(){}

    public static Constantes getInstance() {
        return Constantes.NewSingletonHolder.INSTANCE;
    }

    private static class NewSingletonHolder {
        private static final Constantes INSTANCE = new Constantes();
    }

    public String getIp_db() {
        return ip_db;
    }

    public void setIp_db(String ip_db) {
        this.ip_db = ip_db;
    }

    public String getPort_db() {
        return port_db;
    }

    public void setPort_db(String port_db) {
        this.port_db = port_db;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl() {
        this.url = "jdbc:postgresql://"+ip_db+":"+port_db+"/redes2";
    }


}
