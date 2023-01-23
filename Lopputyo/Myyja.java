public class Myyja {
    
    private int myyjatunnus;
    private String etunimi;
    private String sukunimi;
    private String osoite;
    private String puhnum;
    private String sposti;

    public Myyja(int myyjatunnus, String etunimi, String sukunimi, String osoite, String puhnum, String sposti) {

        this.myyjatunnus = myyjatunnus;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.osoite = osoite;
        this.puhnum = puhnum;
        this.sposti = sposti;

    }

    public Myyja() {
        
    }

    public int getMyyjatunnus() {
        return this.myyjatunnus;
    }

    public void setMyyjatunnus(int myyjatunnus) {
        this.myyjatunnus = myyjatunnus;
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

}
