public class Asiakas {
    
    private int asiakastunnus;
    private String etunimi;
    private String sukunimi;
    private String osoite;
    private String puhnum;
    private String sposti;
    private String kayttajatunnus;

    public Asiakas(int asiakastunnus, String etunimi, String sukunimi, String osoite, String puhnum, String sposti) {

        this.asiakastunnus = asiakastunnus;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.osoite = osoite;
        this.puhnum = puhnum;
        this.sposti = sposti;
        
    }

    public Asiakas() {

    }

    public int getAsiakastunnus() {
        return this.asiakastunnus;
    }

    public void setAsiakastunnus(int asiakastunnus) {
        this.asiakastunnus = asiakastunnus;
    }

    public String getEtunimi() {
        return this.etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return this.sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getOsoite() {
        return this.osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getPuhnum() {
        return this.puhnum;
    }

    public void setPuhnum(String puhnum) {
        this.puhnum = puhnum;
    }

    public String getSposti() {
        return this.sposti;
    }

    public void setSposti(String sposti) {
        this.sposti = sposti;
    }

    public String getKayttajatunnus() {
        return this.kayttajatunnus;
    }

    public void setKayttajatunnus(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
    }

    

}
