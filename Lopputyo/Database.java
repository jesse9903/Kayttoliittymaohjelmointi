import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    public static Connection openConnection(String connString) throws SQLException {
        Connection con = DriverManager.getConnection(connString);
        System.out.println("\t>> Yhteys avattu");
        return con;
    }

    public static void closeConnection(Connection c) throws SQLException {
        if (c != null) {
            c.close();
        }
        System.out.println("\t>> Yhteys suljettu");
    }

    public static ArrayList<Albumi> haeAlbumit(Connection c) throws SQLException {

        ArrayList<Albumi> albumit = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT * FROM albumit");
        
        while(rs.next()) {
            Albumi albumi = new Albumi();
            albumi.setArtisti(rs.getString("artisti"));
            albumi.setEan(rs.getInt("ean"));
            albumi.setJulkaisuvuosi(rs.getInt("julkaisuvuosi"));
            albumi.setLevy_yhtio(rs.getString("levy_yhtiö"));
            albumi.setNimi(rs.getString("nimi"));
            albumi.setMyynti(rs.getInt("myynti"));

                if (rs.getString("kuva") == null) {
                    albumi.setKuva("Kuvat/kuvaa_ei_loydy.jpg");
                
                } else {
                    albumi.setKuva(rs.getString("kuva"));
                }

            albumit.add(albumi);
        }

        return albumit;

    }

    public static ArrayList<Asiakastilaus> haeAsiakastilaukset(Connection c) throws SQLException {

        ArrayList<Asiakastilaus> asiakastilaukset = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT asiakastunnus, hinta, myyjätunnus, päivämäärä, a.tilaustunnus, b.ean, b.määrä FROM tilaukset AS a, tilatut_albumit AS b WHERE tilaustyyppi = 'asiakastilaus' AND a.tilaustunnus = b.tilaustunnus");

        // boolean tilaustunnusOlemassa = false;
        
        while(rs.next()) {

            // tilaustunnusOlemassa = false;

            // // Tsekataan onko ArrayListissä samaa tilaustunnusta, jos on niin lisätään jäljellä olevat ean-koodit
            // for (int i = 0; i < asiakastilaukset.size(); i++) {
            //     if (asiakastilaukset.get(i).getTilaustunnus() == rs.getInt("tilaustunnus")) {
            //         asiakastilaukset.get(i).setEan(asiakastilaukset.get(i).getEan() + rs.getInt("tilaustunnus"));
            //         tilaustunnusOlemassa = true;
            //     }
            // }

            // if (!tilaustunnusOlemassa) { 
            Asiakastilaus asiakastilaus = new Asiakastilaus();
            asiakastilaus.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakastilaus.setHinta(rs.getDouble("hinta"));
            asiakastilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            asiakastilaus.setTilauspvm(rs.getDate("päivämäärä"));
            asiakastilaus.setTilaustunnus(rs.getInt("a.tilaustunnus"));
            asiakastilaus.setEan(rs.getInt("b.ean"));
            asiakastilaus.setMaara(rs.getInt("b.määrä"));

            asiakastilaukset.add(asiakastilaus);
            // }
        }

        return asiakastilaukset;
    }

    public static ArrayList<Levytilaus> haeLevytilaukset(Connection c) throws SQLException {

        ArrayList<Levytilaus> levytilaukset = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT a.tilaustunnus, a.levy_yhtiö, myyjätunnus, päivämäärä, a.hinta, b.ean, b.määrä FROM tilaukset AS a, tilatut_albumit AS b WHERE tilaustyyppi = 'levytilaus' AND a.tilaustunnus = b.tilaustunnus ");
        
        while(rs.next()) {
            Levytilaus levytilaus = new Levytilaus();
            levytilaus.setEan(rs.getInt("b.ean"));
            levytilaus.setHinta(rs.getDouble("a.hinta"));
            levytilaus.setLevy_yhtio(rs.getString("a.levy_yhtiö"));
            levytilaus.setMaara(rs.getInt("b.määrä"));
            levytilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            levytilaus.setTilauspvm(rs.getDate("päivämäärä").toString());
            levytilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            levytilaukset.add(levytilaus);
        }

        return levytilaukset;

    }

    public static ArrayList<Asiakas> haeAsiakkaat(Connection c) throws SQLException {

        ArrayList<Asiakas> asiakkaat = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT * FROM asiakkaat");
        
        while(rs.next()) {
            Asiakas asiakas = new Asiakas();
            asiakas.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakas.setEtunimi(rs.getString("etunimi"));
            asiakas.setSukunimi(rs.getString("sukunimi"));
            asiakas.setOsoite(rs.getString("osoite"));
            asiakas.setPuhnum(rs.getString("puhelinnumero"));
            asiakas.setSposti(rs.getString("sähköposti"));
            asiakas.setKayttajatunnus(rs.getString("käyttäjätunnus"));

            asiakkaat.add(asiakas);
        }

        return asiakkaat;

    }

    public static ArrayList<Kayttaja> haeKayttajat(Connection c) throws SQLException {

        ArrayList<Kayttaja> kayttajat = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT * FROM käyttäjät");
        
        while(rs.next()) {
            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setEtunimi(rs.getString("etunimi"));
            kayttaja.setSukunimi(rs.getString("sukunimi"));
            kayttaja.setKayttajatunnus(rs.getString("käyttäjätunnus"));
            kayttaja.setSalasana(rs.getString("salasana"));
            kayttaja.setSposti(rs.getString("sähköposti"));
            kayttaja.setAsiakastunnus(rs.getInt("asiakastunnus"));

            kayttajat.add(kayttaja);
        }

        return kayttajat;

    }

    public static ArrayList<Myyja> haeMyyjat(Connection c) throws SQLException {

        ArrayList<Myyja> myyjat = new ArrayList<>();

        Statement stmt = c.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT * FROM myyjät");
        
        while(rs.next()) {
            Myyja myyja = new Myyja();
            myyja.setEtunimi(rs.getString("etunimi"));
            myyja.setSukunimi(rs.getString("sukunimi"));
            myyja.setOsoite(rs.getString("osoite"));
            myyja.setMyyjatunnus(rs.getInt("myyjätunnus"));
            myyja.setPuhnum(rs.getString("puhelinnumero"));
            myyja.setSposti(rs.getNString("sähköposti"));

            myyjat.add(myyja);
        }

        return myyjat;

    }


    public static void lisaaAlbumi(Connection con, String ean, String nimi, String artisti, int julkaisuvuosi, String levy_yhtio, int myynti, String kuva) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO albumit (ean, nimi, artisti, julkaisuvuosi, levy_yhtiö, myynti, kuva) VALUES (?, ?, ?, ?, ?, ?, ?)");
        ps.setString(1, ean);
        ps.setString(2, nimi);
        ps.setString(3, artisti);
        ps.setInt(4, julkaisuvuosi);
        ps.setString(5, levy_yhtio);
        ps.setInt(6, myynti);
        ps.setString(7, kuva);
        ps.executeQuery();

        System.out.println("\t>> Albumi " + nimi + " lisätty");

    }

    public static void LisaaLevytilaus(Connection con, int myyjatunnus, String levy_yhtio, String pvm, double hinta, int ean, int maara) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO tilaukset (tilaustyyppi, myyjätunnus, levy_yhtiö, päivämäärä, hinta) VALUES ('Levytilaus', ?, ?, ?, ?)");
        ps.setInt(1, myyjatunnus);
        ps.setString(2, levy_yhtio);
        ps.setString(3, pvm);
        ps.setDouble(4, hinta);
        ps.executeQuery();

        ResultSet rs = stmt.executeQuery("SELECT MAX(tilaustunnus) AS tunnus FROM tilaukset WHERE tilaustyyppi = 'Levytilaus'");

        int tunnus = 0;
        
        while(rs.next()) {
            tunnus = rs.getInt("tunnus");
        }

        PreparedStatement ps2 = con.prepareStatement("INSERT INTO tilatut_albumit (ean, tilaustunnus, määrä) VALUES (?, ?, ?)");
        ps2.setInt(1, ean);
        ps2.setInt(2, tunnus);
        ps2.setInt(3, maara);
        ps2.executeQuery();

        System.out.println("\t>> Lisätty tilaus " + tunnus);

    }

    public static void LisaaAsiakastilaus(Connection con, int myyjatunnus, int asiakastunnus, String pvm, double hinta, int ean, int maara) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO tilaukset (tilaustyyppi, myyjätunnus, asiakastunnus, päivämäärä, hinta) VALUES ('Asiakastilaus', ?, ?, ?, ?)");
        ps.setInt(1, myyjatunnus);
        ps.setInt(2, asiakastunnus);
        ps.setString(3, pvm);
        ps.setDouble(4, hinta);
        ps.executeQuery();

        ResultSet rs = stmt.executeQuery("SELECT MAX(tilaustunnus) AS tunnus FROM tilaukset WHERE tilaustyyppi = 'Asiakastilaus'");

        int tunnus = 0;
        
        while(rs.next()) {
            tunnus = rs.getInt("tunnus");
        }

        PreparedStatement ps2 = con.prepareStatement("INSERT INTO tilatut_albumit (ean, tilaustunnus, määrä) VALUES (?, ?, ?)");
        ps2.setInt(1, ean);
        ps2.setInt(2, tunnus);
        ps2.setInt(3, maara);
        ps2.executeQuery();

        System.out.println("\t>> Lisätty tilaus " + tunnus);

    }

    public static void lisaaAsiakas(Connection con, String etunimi, String sukunimi, String osoite, String puhnum, String sposti) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO asiakkaat (etunimi, sukunimi, osoite, puhelinnumero, sähköposti) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, puhnum);
        ps.setString(5, sposti);
        ps.executeQuery();

        System.out.println("\t>> Asiakas " + etunimi + " " + sukunimi + " lisätty");

    }

    public static void lisaaAsiakas(Connection con, String etunimi, String sukunimi, String osoite, String puhnum, String sposti, String kayttajatunnus, String salasana) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO asiakkaat (etunimi, sukunimi, osoite, puhelinnumero, sähköposti, käyttäjätunnus) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, puhnum);
        ps.setString(5, sposti);
        ps.setString(6, kayttajatunnus);
        ps.executeUpdate();

        System.out.println("\t>> Asiakas " + etunimi + " " + sukunimi + " lisätty");

        Statement stmt2 = con.createStatement();
        ResultSet rs = stmt2.executeQuery("SELECT MAX(asiakastunnus) FROM asiakkaat");

        int asiakastunnus = 0;

        while(rs.next()) {
            asiakastunnus = rs.getInt("MAX(asiakastunnus)");
        }

        PreparedStatement ps2 = con.prepareStatement("INSERT INTO käyttäjät (käyttäjätunnus, salasana, etunimi, sukunimi, sähköposti, asiakastunnus) VALUES (?, ?, ?, ?, ?, ?)");
        ps2.setString(1, kayttajatunnus);
        ps2.setString(2, salasana);
        ps2.setString(3, etunimi);
        ps2.setString(4, sukunimi);
        ps2.setString(5, sposti);
        ps2.setInt(6, asiakastunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Käyttäjät " + kayttajatunnus + " lisätty");

    }

    public static void lisaaKayttaja(Connection con, String kayttajatunnus, String salasana, String etunimi, String sukunimi, String sposti) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps1 = con.prepareStatement("INSERT INTO asiakkaat (etunimi, sukunimi, sähköposti, käyttäjätunnus) VALUES (?, ?, ?, ?)");
        ps1.setString(1, etunimi);
        ps1.setString(2, sukunimi);  
        ps1.setString(3, sposti);
        ps1.setString(4, kayttajatunnus);
        ps1.executeUpdate();

        System.out.println("\t>> Asiakas " + etunimi + " " + sukunimi + " lisätty");

        ResultSet rs = stmt.executeQuery("SELECT MAX(asiakastunnus) AS asiakastunnus FROM asiakkaat");

        int asiakastunnus = 0;

        while(rs.next()) {
            asiakastunnus = rs.getInt("asiakastunnus");
        }

        PreparedStatement ps2 = con.prepareStatement("INSERT INTO käyttäjät (käyttäjätunnus, salasana, etunimi, sukunimi, sähköposti, asiakastunnus) VALUES (?, ?, ?, ?, ?, ?)");
        ps2.setString(1, kayttajatunnus);
        ps2.setString(2, salasana);
        ps2.setString(3, etunimi);
        ps2.setString(4, sukunimi);
        ps2.setString(5, sposti);
        ps2.setInt(6, asiakastunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Käyttäjä " + kayttajatunnus + " lisätty");

    }

    public static void lisaaMyyja(Connection con, String etunimi, String sukunimi, String osoite, String sposti, String puhnum) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("INSERT INTO myyjät (etunimi, sukunimi, osoite, sähköposti, puhelinnumero) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, sposti);
        ps.setString(5, puhnum);
        ps.executeUpdate();

        System.out.println("\t>> Myyjä " + etunimi + " " + sukunimi + " lisätty");

    }

    public static void poistaAlbumi(Connection con, int ean, String nimi, String artistinNimi) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM albumit WHERE ean = " + ean);

        System.out.println("\t>> Albumi " + nimi + " - " + artistinNimi + " poistettu" );
        
    }

    public static void poistaLevytilaus(Connection con, int tilaustunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM tilaukset WHERE tilaustunnus = " + tilaustunnus);

        System.out.println("\t>> Tilaus " + tilaustunnus + " poistettu" );
        
    }
    
    public static void poistaAsiakastilaus(Connection con, int tilaustunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM tilaukset WHERE tilaustunnus = " + tilaustunnus);

        System.out.println("\t>> Tilaus " + tilaustunnus + " poistettu" );
        
    }

    public static void poistaAsiakas(Connection con, int asiakastunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM asiakkaat WHERE asiakastunnus = " + asiakastunnus);

        System.out.println("\t>> Asiakas " + asiakastunnus + " poistettu" );
        
    }
    
    public static void poistaKayttaja(Connection con, String kayttajatunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM käyttäjät WHERE käyttäjätunnus = " + "'" + kayttajatunnus + "'");

        System.out.println("\t>> Asiakas " + kayttajatunnus + " poistettu" );
        
    }

    public static void poistaMyyja(Connection con, String myyjatunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        stmt.executeQuery("DELETE FROM myyjät WHERE myyjätunnus = " + myyjatunnus);

        System.out.println("\t>> Myyjä " + myyjatunnus + " poistettu" );
        
    }
    
    public static void muokkaaAlbumia(Connection con, int ean, String nimi, String artisti, String levy_yhtio, int julkaisuvuosi, int myynti, String kuva) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE albumit " +  "SET levy_yhtiö = ?, julkaisuvuosi = ?, nimi = ?, artisti = ?, myynti = ?, kuva = ? " + "WHERE ean = ?;");

        ps.setString(1, levy_yhtio);
        ps.setInt(2, julkaisuvuosi);
        ps.setString(3, nimi);
        ps.setString(4, artisti);
        ps.setInt(5, myynti);
        ps.setString(6, kuva);
        ps.setInt(7, ean);
        ps.executeQuery();

        System.out.println("\t>> Albumia " + nimi + " - " + artisti + " muokattu");

    }

    public static void muokkaaLevytilausta(Connection con, int tilaustunnus, int myyjatunnus, String pvm, String levy_yhtio, double hinta, int ean, int maara) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE tilaukset SET myyjätunnus = ?, päivämäärä = ?, levy_yhtiö = ?, hinta = ? WHERE tilaustunnus = ?");
        ps.setInt(1, myyjatunnus);
        ps.setString(2, pvm);
        ps.setString(3, levy_yhtio);
        ps.setDouble(4, hinta);
        ps.setInt(5, tilaustunnus);
        ps.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement("UPDATE tilatut_albumit SET ean = ?, määrä = ? WHERE tilaustunnus = ?");
        ps2.setInt(1, ean);
        ps2.setInt(2, maara);
        ps2.setInt(3, tilaustunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Tilausta " + tilaustunnus + " muutettu");

    }

    public static void muokkaaAsiakastilausta(Connection con, int tilaustunnus, int myyjatunnus, String pvm, int asiakastunnus, double hinta, int ean, int maara) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE tilaukset SET myyjätunnus = ?, päivämäärä = ?, asiakastunnus = ?, hinta = ? WHERE tilaustunnus = ?");
        ps.setInt(1, myyjatunnus);
        ps.setString(2, pvm);
        ps.setInt(3, asiakastunnus);
        ps.setDouble(4, hinta);
        ps.setInt(5, tilaustunnus);
        ps.executeUpdate();

        PreparedStatement ps2 = con.prepareStatement("UPDATE tilatut_albumit SET ean = ?, määrä = ? WHERE tilaustunnus = ?");
        ps2.setInt(1, ean);
        ps2.setInt(2, maara);
        ps2.setInt(3, tilaustunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Tilausta " + tilaustunnus + " muutettu");

    }

    public static void muokkaaAsiakasta(Connection con, int asiakastunnus, String etunimi, String sukunimi, String osoite, String sposti, String puh) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE asiakkaat SET etunimi = ?, sukunimi = ?, osoite = ?, sähköposti = ?, puhelinnumero = ? WHERE asiakastunnus = ?");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, sposti);
        ps.setString(5, puh);
        ps.setInt(6, asiakastunnus);
        ps.executeUpdate();

        System.out.println("\t>> Asiakasta " + asiakastunnus + " muutettu");
    }

    public static void muokkaaKayttajaa(Connection con, String kayttajatunnus, String etunimi, String sukunimi, String sposti, String salasana, String vanhaKayttajatunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE käyttäjät SET käyttäjätunnus = ?, etunimi = ?, sukunimi = ?, sähköposti = ?, salasana = ? WHERE käyttäjätunnus = ?");
        ps.setString(1, kayttajatunnus);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        ps.setString(4, sposti);
        ps.setString(5, salasana);
        ps.setString(6, vanhaKayttajatunnus);
        ps.executeUpdate();

        System.out.println("\t>> Käyttäjää " + vanhaKayttajatunnus + " muutettu");

        PreparedStatement ps2 = con.prepareStatement("UPDATE asiakkaat SET käyttäjätunnus = ?, etunimi = ?, sukunimi = ?, sähköposti = ? WHERE käyttäjätunnus = ?");
        ps2.setString(1, kayttajatunnus);
        ps2.setString(2, etunimi);
        ps2.setString(3, sukunimi);
        ps2.setString(4, sposti);
        ps2.setString(5, vanhaKayttajatunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Asiakasta " + etunimi + " " + sukunimi + " muokattu");
    }

    public static void muokkaaKayttajaa(Connection con, String kayttajatunnus, String etunimi, String sukunimi, String sposti, String vanhaKayttajatunnus) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE käyttäjät SET käyttäjätunnus = ?, etunimi = ?, sukunimi = ?, sähköposti = ? WHERE käyttäjätunnus = ?");
        ps.setString(1, kayttajatunnus);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        ps.setString(4, sposti);
        ps.setString(5, vanhaKayttajatunnus);
        ps.executeUpdate();

        System.out.println("\t>> Käyttäjää " + vanhaKayttajatunnus + " muutettu");

        PreparedStatement ps2 = con.prepareStatement("UPDATE asiakkaat SET käyttäjätunnus = ?, etunimi = ?, sukunimi = ?, sähköposti = ? WHERE käyttäjätunnus = ?");
        ps2.setString(1, kayttajatunnus);
        ps2.setString(2, etunimi);
        ps2.setString(3, sukunimi);
        ps2.setString(4, sposti);
        ps2.setString(5, vanhaKayttajatunnus);
        ps2.executeUpdate();

        System.out.println("\t>> Asiakasta " + etunimi + " " + sukunimi + " muokattu");
    }

    public static void muokkaaMyyjaa(Connection con, int myyjatunnus, String etunimi, String sukunimi, String osoite, String sposti, String puh) throws SQLException {

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("UPDATE myyjät SET etunimi = ?, sukunimi = ?, osoite = ?, sähköposti = ?, puhelinnumero = ? WHERE myyjätunnus = ?");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, sposti);
        ps.setString(5, puh);
        ps.setInt(6, myyjatunnus);
        ps.executeUpdate();

        System.out.println("\t>> Myyjää " + myyjatunnus + " muutettu");
    }
    
    public static ArrayList<Integer> haeEnarit(Connection con)  throws SQLException {

        ArrayList<Integer> eanList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT ean FROM albumit");

        while(rs.next()) {

            Integer ean = rs.getInt("ean");
            eanList.add(ean);
        }

        return eanList;
    }

    public static ArrayList<Integer> haeMyyjatunnukset(Connection con) throws SQLException {

        ArrayList<Integer> myyjatunnusList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT myyjätunnus FROM myyjät");

        while(rs.next()) {

            Integer myyjatunnus = rs.getInt("myyjätunnus");
            myyjatunnusList.add(myyjatunnus);
        }

        return myyjatunnusList;
    }

    public static ArrayList<Integer> haeAsiakastunnukset(Connection con) throws SQLException {

        ArrayList<Integer> asiakastunnusList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT asiakastunnus FROM asiakkaat");

        while(rs.next()) {

            Integer asiakastunnus = rs.getInt("asiakastunnus");
            asiakastunnusList.add(asiakastunnus);
        }

        return asiakastunnusList;
    }

    public static ArrayList<String> haeKayttajatunnukset(Connection con) throws SQLException {

        ArrayList<String> kayttajatunnusList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT käyttäjätunnus FROM käyttäjät");

        while(rs.next()) {

            String kayttajatunnus = rs.getString("käyttäjätunnus");
            kayttajatunnusList.add(kayttajatunnus);
        }

        return kayttajatunnusList;
    }
    
    public static ArrayList<String> haeKayttajienSpostit(Connection con) throws SQLException {

        ArrayList<String> spostiList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT sähköposti FROM käyttäjät");

        while(rs.next()) {

            String sposti = rs.getString("sähköposti");
            spostiList.add(sposti);
        }

        return spostiList;
    }

    public static ArrayList<String> haeAsiakkaidenSpostit(Connection con) throws SQLException {

        ArrayList<String> spostiList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT sähköposti FROM asiakkaat");

        while(rs.next()) {

            String sposti = rs.getString("sähköposti");
            spostiList.add(sposti);
        }

        return spostiList;
    }

    public static ArrayList<String> haeMyyjienSpostit(Connection con) throws SQLException {

        ArrayList<String> spostiList = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        ResultSet rs = stmt.executeQuery("SELECT sähköposti FROM myyjät");

        while(rs.next()) {

            String sposti = rs.getString("sähköposti");
            spostiList.add(sposti);
        }

        return spostiList;
    }

    public static ArrayList<Albumi> hakuAlbumitNimi (Connection con, String albuminNimi) throws SQLException {

        ArrayList<Albumi> albumit = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM albumit WHERE nimi LIKE ?");
        ps.setString(1, "%" + albuminNimi + "%");
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Albumi albumi = new Albumi();
            albumi.setArtisti(rs.getString("artisti"));
            albumi.setEan(rs.getInt("ean"));
            albumi.setNimi(rs.getString("nimi"));
            albumi.setJulkaisuvuosi(rs.getInt("julkaisuvuosi"));
            albumi.setKuva(rs.getString("kuva"));
            albumi.setLevy_yhtio(rs.getString("levy_yhtiö"));
            albumi.setMyynti(rs.getInt("myynti"));

            albumit.add(albumi);
        }

        return albumit;

    }

    public static ArrayList<Albumi> hakuAlbumitArtisti (Connection con, String artisti) throws SQLException {

        ArrayList<Albumi> albumit = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM albumit WHERE artisti LIKE ?");
        ps.setString(1, "%" + artisti + "%");
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Albumi albumi = new Albumi();
            albumi.setArtisti(rs.getString("artisti"));
            albumi.setEan(rs.getInt("ean"));
            albumi.setNimi(rs.getString("nimi"));
            albumi.setJulkaisuvuosi(rs.getInt("julkaisuvuosi"));
            albumi.setKuva(rs.getString("kuva"));
            albumi.setLevy_yhtio(rs.getString("levy_yhtiö"));
            albumi.setMyynti(rs.getInt("myynti"));

            albumit.add(albumi);
        }

        return albumit;

    }

    public static ArrayList<Albumi> hakuAlbumitLevy_yhtio (Connection con, String levy_yhtio) throws SQLException {

        ArrayList<Albumi> albumit = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM albumit WHERE levy_yhtiö LIKE ?");
        ps.setString(1, "%" + levy_yhtio + "%");
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Albumi albumi = new Albumi();
            albumi.setArtisti(rs.getString("artisti"));
            albumi.setEan(rs.getInt("ean"));
            albumi.setNimi(rs.getString("nimi"));
            albumi.setJulkaisuvuosi(rs.getInt("julkaisuvuosi"));
            albumi.setKuva(rs.getString("kuva"));
            albumi.setLevy_yhtio(rs.getString("levy_yhtiö"));
            albumi.setMyynti(rs.getInt("myynti"));

            albumit.add(albumi);
        }

        return albumit;

    }

    public static ArrayList<Albumi> hakuAlbumitEan (Connection con, int ean) throws SQLException {

        ArrayList<Albumi> albumit = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM albumit WHERE ean LIKE ?");
        ps.setString(1, "%" + ean + "%");
        
        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Albumi albumi = new Albumi();
            albumi.setArtisti(rs.getString("artisti"));
            albumi.setEan(rs.getInt("ean"));
            albumi.setNimi(rs.getString("nimi"));
            albumi.setJulkaisuvuosi(rs.getInt("julkaisuvuosi"));
            albumi.setKuva(rs.getString("kuva"));
            albumi.setLevy_yhtio(rs.getString("levy_yhtiö"));
            albumi.setMyynti(rs.getInt("myynti"));

            albumit.add(albumi);
        }

        return albumit;

    }

    public static ArrayList<Levytilaus> hakuLevytilausTunnus (Connection con, int tunnus) throws SQLException {

        ArrayList<Levytilaus> levytilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE tilaukset.tilaustunnus LIKE ? AND tilaustyyppi = 'levytilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + tunnus + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Levytilaus levytilaus = new Levytilaus();
            levytilaus.setEan(rs.getInt("ean"));
            levytilaus.setHinta(rs.getDouble("hinta"));
            levytilaus.setLevy_yhtio(rs.getString("levy_yhtiö"));
            levytilaus.setMaara(rs.getInt("määrä"));
            levytilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            levytilaus.setTilauspvm(rs.getDate("päivämäärä"));
            levytilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            levytilaukset.add(levytilaus); 
        }

        return levytilaukset;
    }

    public static ArrayList<Levytilaus> hakuLevytilausPvm (Connection con, Date pvm) throws SQLException {

        ArrayList<Levytilaus> levytilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE päivämäärä LIKE ? AND tilaustyyppi = 'levytilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + pvm + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Levytilaus levytilaus = new Levytilaus();
            levytilaus.setEan(rs.getInt("ean"));
            levytilaus.setHinta(rs.getDouble("hinta"));
            levytilaus.setLevy_yhtio(rs.getString("levy_yhtiö"));
            levytilaus.setMaara(rs.getInt("määrä"));
            levytilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            levytilaus.setTilauspvm(rs.getDate("päivämäärä"));
            levytilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            levytilaukset.add(levytilaus); 
        }

        return levytilaukset;
    }

    public static ArrayList<Levytilaus> hakuLevytilausHinta (Connection con, int hinta) throws SQLException {

        ArrayList<Levytilaus> levytilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE hinta LIKE ? AND tilaustyyppi = 'levytilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + hinta + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Levytilaus levytilaus = new Levytilaus();
            levytilaus.setEan(rs.getInt("ean"));
            levytilaus.setHinta(rs.getDouble("hinta"));
            levytilaus.setLevy_yhtio(rs.getString("levy_yhtiö"));
            levytilaus.setMaara(rs.getInt("määrä"));
            levytilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            levytilaus.setTilauspvm(rs.getDate("päivämäärä"));
            levytilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            levytilaukset.add(levytilaus); 
        }

        return levytilaukset;
    }

    public static ArrayList<Asiakastilaus> hakuAsiakastilausTunnus (Connection con, int tunnus) throws SQLException {

        ArrayList<Asiakastilaus> asiakastilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE tilaukset.tilaustunnus LIKE ? AND tilaustyyppi = 'asiakastilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + tunnus + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Asiakastilaus asiakastilaus = new Asiakastilaus();
            asiakastilaus.setEan(rs.getInt("ean"));
            asiakastilaus.setHinta(rs.getDouble("hinta"));
            asiakastilaus.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakastilaus.setMaara(rs.getInt("määrä"));
            asiakastilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            asiakastilaus.setTilauspvm(rs.getDate("päivämäärä"));
            asiakastilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            asiakastilaukset.add(asiakastilaus);
        }

        return asiakastilaukset;
    }

    public static ArrayList<Asiakastilaus> hakuAsiakastilausPvm (Connection con, Date pvm) throws SQLException {

        ArrayList<Asiakastilaus> asiakastilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE päivämäärä LIKE ? AND tilaustyyppi = 'asiakastilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + pvm + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Asiakastilaus asiakastilaus = new Asiakastilaus();
            asiakastilaus.setEan(rs.getInt("ean"));
            asiakastilaus.setHinta(rs.getDouble("hinta"));
            asiakastilaus.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakastilaus.setMaara(rs.getInt("määrä"));
            asiakastilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            asiakastilaus.setTilauspvm(rs.getDate("päivämäärä"));
            asiakastilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            asiakastilaukset.add(asiakastilaus);
        }

        return asiakastilaukset;
    }

    public static ArrayList<Asiakastilaus> hakuAsiakastilausHinta (Connection con, int hinta) throws SQLException {

        ArrayList<Asiakastilaus> asiakastilaukset = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM tilaukset, tilatut_albumit WHERE hinta LIKE ? AND tilaustyyppi = 'asiakastilaus' AND tilaukset.tilaustunnus = tilatut_albumit.tilaustunnus");
        ps.setString(1, "%" + hinta + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Asiakastilaus asiakastilaus = new Asiakastilaus();
            asiakastilaus.setEan(rs.getInt("ean"));
            asiakastilaus.setHinta(rs.getDouble("hinta"));
            asiakastilaus.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakastilaus.setMaara(rs.getInt("määrä"));
            asiakastilaus.setMyyjatunnus(rs.getInt("myyjätunnus"));
            asiakastilaus.setTilauspvm(rs.getDate("päivämäärä"));
            asiakastilaus.setTilaustunnus(rs.getInt("tilaustunnus"));

            asiakastilaukset.add(asiakastilaus);

        }

        return asiakastilaukset;
    }

    public static ArrayList<Asiakas> hakuAsiakasTunnus (Connection con, int tunnus) throws SQLException {

        ArrayList<Asiakas> asiakkaat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM asiakkaat WHERE asiakastunnus LIKE ?");
        ps.setString(1, "%" + tunnus + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Asiakas asiakas = new Asiakas();
            asiakas.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakas.setEtunimi(rs.getString("etunimi"));
            asiakas.setSukunimi(rs.getString("sukunimi"));
            asiakas.setKayttajatunnus(rs.getString("käyttäjätunnus"));
            asiakas.setOsoite(rs.getString("osoite"));
            asiakas.setPuhnum(rs.getString("puhelinnumero"));
            asiakas.setSposti(rs.getString("sähköposti"));

            asiakkaat.add(asiakas);
        }

        return asiakkaat;
    }

    public static ArrayList<Asiakas> hakuAsiakasNimi (Connection con, String nimi) throws SQLException {

        ArrayList<Asiakas> asiakkaat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM asiakkaat WHERE etunimi LIKE ? OR sukunimi LIKE ? OR CONCAT(etunimi, ' ', sukunimi) LIKE ?");
        ps.setString(1, "%" + nimi + "%");
        ps.setString(2, "%" + nimi + "%");
        ps.setString(3, "%" + nimi + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Asiakas asiakas = new Asiakas();
            asiakas.setAsiakastunnus(rs.getInt("asiakastunnus"));
            asiakas.setEtunimi(rs.getString("etunimi"));
            asiakas.setSukunimi(rs.getString("sukunimi"));
            asiakas.setKayttajatunnus(rs.getString("käyttäjätunnus"));
            asiakas.setOsoite(rs.getString("osoite"));
            asiakas.setPuhnum(rs.getString("puhelinnumero"));
            asiakas.setSposti(rs.getString("sähköposti"));

            asiakkaat.add(asiakas);
        }

        return asiakkaat;
    }

    public static ArrayList<Kayttaja> hakuKayttajaTunnus (Connection con, String kayttajatunnus) throws SQLException {

        ArrayList<Kayttaja> kayttajat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM käyttäjät WHERE käyttäjätunnus LIKE ?");
        ps.setString(1, "%" + kayttajatunnus + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setAsiakastunnus(rs.getInt("asiakastunnus"));
            kayttaja.setEtunimi(rs.getString("etunimi"));
            kayttaja.setSukunimi(rs.getString("sukunimi"));
            kayttaja.setKayttajatunnus(rs.getString("käyttäjätunnus"));
            kayttaja.setSposti(rs.getString("sähköposti"));
            kayttaja.setSalasana(rs.getString("salasana"));

            kayttajat.add(kayttaja);
        }

        return kayttajat;
    }

    public static ArrayList<Kayttaja> hakuKayttajaNimi (Connection con, String nimi) throws SQLException {

        ArrayList<Kayttaja> kayttajat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM käyttäjät WHERE etunimi LIKE ? OR sukunimi LIKE ? OR CONCAT(etunimi, ' ', sukunimi) LIKE ? ");
        ps.setString(1, "%" + nimi + "%");
        ps.setString(2, "%" + nimi + "%");
        ps.setString(3, "%" + nimi + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setAsiakastunnus(rs.getInt("asiakastunnus"));
            kayttaja.setEtunimi(rs.getString("etunimi"));
            kayttaja.setSukunimi(rs.getString("sukunimi"));
            kayttaja.setKayttajatunnus(rs.getString("käyttäjätunnus"));
            kayttaja.setSposti(rs.getString("sähköposti"));
            kayttaja.setSalasana(rs.getString("salasana"));

            kayttajat.add(kayttaja);

        }

        return kayttajat;
    }

    public static ArrayList<Myyja> hakuMyyjaTunnus (Connection con, int tunnus) throws SQLException {

        ArrayList<Myyja> myyjat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM myyjät WHERE myyjätunnus LIKE ?");
        ps.setString(1, "%" + tunnus + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Myyja myyja = new Myyja();
            myyja.setEtunimi(rs.getString("etunimi"));
            myyja.setSukunimi(rs.getString("sukunimi"));
            myyja.setMyyjatunnus(rs.getInt("myyjätunnus"));
            myyja.setOsoite(rs.getString("osoite"));
            myyja.setPuhnum(rs.getString("puhelinnumero"));
            myyja.setSposti(rs.getString("sähköposti"));
            
            myyjat.add(myyja);

        }

        return myyjat;
    }

    public static ArrayList<Myyja> hakuMyyjaNimi (Connection con, String nimi) throws SQLException {

        ArrayList<Myyja> myyjat = new ArrayList<>();

        Statement stmt = con.createStatement();
        stmt.executeQuery("USE karelia_2108001");

        PreparedStatement ps = con.prepareStatement("SELECT * FROM myyjät WHERE etunimi LIKE ? OR sukunimi LIKE ? OR CONCAT(etunimi, ' ', sukunimi) LIKE ?");
        ps.setString(1, "%" + nimi + "%");
        ps.setString(2, "%" + nimi + "%");
        ps.setString(3, "%" + nimi + "%");

        ResultSet rs = ps.executeQuery();

        while(rs.next()) {

            Myyja myyja = new Myyja();
            myyja.setEtunimi(rs.getString("etunimi"));
            myyja.setSukunimi(rs.getString("sukunimi"));
            myyja.setMyyjatunnus(rs.getInt("myyjätunnus"));
            myyja.setOsoite(rs.getString("osoite"));
            myyja.setPuhnum(rs.getString("puhelinnumero"));
            myyja.setSposti(rs.getString("sähköposti"));
            
            myyjat.add(myyja);

        }

        return myyjat;
    }

}
