public class Albumi {
    
    private int ean;
    private String nimi;
    private String artisti;
    private int julkaisuvuosi;
    private String levy_yhtio;
    private int myynti;
    private String kuva;

    public Albumi(int ean, String nimi, String artisti, int julkaisuvuosi, String levy_yhtio, int myynti, String kuva) {

        this.ean = ean;
        this.nimi = nimi;
        this.artisti = artisti;
        this.julkaisuvuosi = julkaisuvuosi;
        this.levy_yhtio = levy_yhtio;
        this.myynti = myynti;

        if (kuva == null) {
            kuva =  "/Kuvat/kuvaa_ei_loydy";
        } else {
            this.kuva = kuva;
        }

    }

    public Albumi() {

    }

    public int getEan() {
        return this.ean;
    }

    public void setEan(int ean) {
        this.ean = ean;
    }

    public String getNimi() {
        return this.nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getArtisti() {
        return this.artisti;
    }

    public void setArtisti(String artisti) {
        this.artisti = artisti;
    }

    public int getJulkaisuvuosi() {
        return this.julkaisuvuosi;
    }

    public void setJulkaisuvuosi(int julkaisuvuosi) {
        this.julkaisuvuosi = julkaisuvuosi;
    }

    public String getLevy_yhtio() {
        return this.levy_yhtio;
    }

    public void setLevy_yhtio(String levy_yhtio) {
        this.levy_yhtio = levy_yhtio;
    }

    public int getMyynti() {
        return this.myynti;
    }

    public void setMyynti(int myynti) {
        this.myynti = myynti;
    }

    public String getKuva() {
        return kuva;
    }

    public void setKuva(String kuva) {
        this.kuva = kuva;
    }
    
}
