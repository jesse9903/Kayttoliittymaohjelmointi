import java.sql.Date;

public class Asiakastilaus {
    
    private int tilaustunnus;
    private int ean;
    private int maara;
    private Date tilauspvm;
    private double hinta;
    private int myyjatunnus;
    private int asiakastunnus;

    public Asiakastilaus(int tilaustunnus, int ean, int maara, String tilauspvm, double hinta, int myyjatunnus, int asiakastunnus) {

        Date pvm = Date.valueOf(tilauspvm);

        this.tilaustunnus = tilaustunnus;
        this.ean = ean;
        this.maara = maara;
        this.tilauspvm = pvm;
        this.hinta = hinta;
        this.myyjatunnus = myyjatunnus;
        this.asiakastunnus = asiakastunnus;

    }

    public Asiakastilaus() {

    }

    public int getTilaustunnus() {
        return this.tilaustunnus;
    }

    public void setTilaustunnus(int tilaustunnus) {
        this.tilaustunnus = tilaustunnus;
    }

    public int getEan() {
        return this.ean;
    }

    public void setEan(int ean) {
        this.ean = ean;
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

    public int getAsiakastunnus() {
        return this.asiakastunnus;
    }

    public void setAsiakastunnus(int asiakastunnus) {
        this.asiakastunnus = asiakastunnus;
    }

}
