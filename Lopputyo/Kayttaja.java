public class Kayttaja {

    private String kayttajatunnus;
    private String etunimi;
    private String sukunimi;
    private String sposti;
    private String salasana;
    private int asiakastunnus;

    public Kayttaja(String kayttajatunnus, String etunimi, String sukunimi, String sposti, String salasana, int asiakastunnus) {

        this.kayttajatunnus = kayttajatunnus;
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.sposti = sposti;
        this.salasana = salasana;
        this.asiakastunnus = asiakastunnus;

    }

    public Kayttaja() {

    }

    public String getKayttajatunnus() {
        return this.kayttajatunnus;
    }

    public void setKayttajatunnus(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
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

    public String getSposti() {
        return this.sposti;
    }

    public void setSposti(String sposti) {
        this.sposti = sposti;
    }

    public String getSalasana() {
        return this.salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }

    public void setAsiakastunnus(int asiakastunnus) {
        this.asiakastunnus = asiakastunnus;
    }

    public int getAsiakastunnus() {
        return asiakastunnus;
    }
    
}
