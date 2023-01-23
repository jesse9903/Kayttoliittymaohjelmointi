import java.sql.Date;

public class Levytilaus {
    
    private int tilaustunnus;
    private String levy_yhtio;
    private int maara;
    private Date tilauspvm;
    private double hinta;
    private int myyjatunnus;
    private int ean;

    public Levytilaus(int tilaustunnus, String levy_yhtio, int maara, String tilauspvm, double hinta, int myyjatunnus, int ean) {

        Date pvm = Date.valueOf(tilauspvm);

        this.tilaustunnus = tilaustunnus;
        this.levy_yhtio = levy_yhtio;
        this.maara = maara;
        this.tilauspvm = pvm;
        this.hinta = hinta;
        this.myyjatunnus = myyjatunnus;
        this.ean = ean;

    }

    public Levytilaus() {

    }

    public int getTilaustunnus() {
        return this.tilaustunnus;
    }

    public void setTilaustunnus(int tilaustunnus) {
        this.tilaustunnus = tilaustunnus;
    }

    public String getLevy_yhtio() {
        return this.levy_yhtio;
    }

    public void setLevy_yhtio(String levy_yhtio) {
        this.levy_yhtio = levy_yhtio;
    }

    public int getMaara() {
        return this.maara;
    }

    public void setMaara(int maara) {
        this.maara = maara;
    }

    public Date getTilauspvm() {
        return this.tilauspvm;
    }

    public void setTilauspvm(String tilauspvm) {
        Date pvm = Date.valueOf(tilauspvm);
        this.tilauspvm = pvm;
    }

    public void setTilauspvm(Date tilauspvm) {
        this.tilauspvm = tilauspvm;
    }

    public double getHinta() {
        return this.hinta;
    }

    public void setHinta(double hinta) {
        this.hinta = hinta;
    }

    public int getMyyjatunnus() {
        return this.myyjatunnus;
    }

    public void setMyyjatunnus(int myyjatunnus) {
        this.myyjatunnus = myyjatunnus;
    }

    public int getEan() {
        return this.ean;
    }

    public void setEan(int ean) {
        this.ean = ean;
    }



}
