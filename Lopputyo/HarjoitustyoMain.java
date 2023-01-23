import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// FIX: UknownHostException

public class HarjoitustyoMain extends Application {
    public static void main(String[] args) throws SQLException {

        // Connection c = Database.openConnection("jdbc:mariadb://localhost:3306?user=root&password=Kork1akosk1"); 
        Connection c = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        // Luodaan tietokanta
        createDatabase(c, "karelia_2108001");

        // Luodaan taulut
        createTable(c, "CREATE TABLE albumit (ean INT(5) PRIMARY KEY, levy_yhtiö VARCHAR(50), julkaisuvuosi INT(4), nimi VARCHAR(50), artisti VARCHAR(50), myynti INT(5), kuva VARCHAR(200));");
        createTable(c, "CREATE TABLE asiakkaat (asiakastunnus INT PRIMARY KEY AUTO_INCREMENT, etunimi VARCHAR(20), sukunimi VARCHAR(50), osoite VARCHAR(50), sähköposti VARCHAR(50), puhelinnumero VARCHAR(20), käyttäjätunnus VARCHAR(20));");
        createTable(c, "CREATE TABLE käyttäjät (käyttäjätunnus VARCHAR(20) PRIMARY KEY, etunimi VARCHAR(20), sukunimi VARCHAR(50), sähköposti VARCHAR(50), salasana VARCHAR(20), asiakastunnus INT, FOREIGN KEY (asiakastunnus) REFERENCES asiakkaat(asiakastunnus) ON DELETE CASCADE);");
        createTable(c, "CREATE TABLE myyjät (myyjätunnus INT NOT NULL PRIMARY KEY AUTO_INCREMENT, etunimi VARCHAR(20), sukunimi VARCHAR(50), osoite VARCHAR(50), sähköposti VARCHAR(50), puhelinnumero VARCHAR(20));");
        createTable(c, "CREATE TABLE tilaukset (tilaustunnus INT PRIMARY KEY AUTO_INCREMENT, tilaustyyppi VARCHAR(20) NOT NULL, myyjätunnus INT NOT NULL, levy_yhtiö VARCHAR(50), asiakastunnus INT NULL, päivämäärä DATE, hinta DOUBLE, FOREIGN KEY (myyjätunnus) REFERENCES myyjät(myyjätunnus) ON DELETE CASCADE, FOREIGN KEY (asiakastunnus) REFERENCES asiakkaat(asiakastunnus) ON DELETE CASCADE);");
        createTable(c, "CREATE TABLE tilatut_albumit (ean INT NOT NULL,tilaustunnus INT NOT NULL, määrä INT(4), FOREIGN KEY (ean) REFERENCES albumit(ean) ON DELETE CASCADE, FOREIGN KEY (tilaustunnus) REFERENCES tilaukset(tilaustunnus) ON DELETE CASCADE);");
        
        // Lisätään albumit
        addAlbumi(c, 1, "Atomic Fire", 1995, "Destroy Erase Improve", "Meshuggah", 123, "Kuvat/destroy_erase_improve.jpg");
        addAlbumi(c, 2, "Metal Blade Records", 2019, "Death Atlas", "Cattle Decapitation", 100, "Kuvat/death_atlas.jpg");
        addAlbumi(c, 3, "Atomic Fire", 2022, "Immutable", "Meshuggah", 0, "Kuvat/immutable.jpeg");
        addAlbumi(c, 4, "Self published", 2016, "The Elysian Grandeval Galériarch", "Infant Annihilator", 65, "Kuvat/the_elysian_grandeval_galeriarch.jpg");
        addAlbumi(c, 5, "Napalm Records", 2019, "Macro", "Jinjer", 78, "Kuvat/macro.jpg");
        addAlbumi(c, 6, "Metal Blade Records", 2012, "Monolith of Inhumanity", "Cattle Decapitation", 99, null);
        addAlbumi(c, 7, "Self Published", 2004, "I", "Meshuggah", 140, "Kuvat/i.jpg");
        addAlbumi(c, 8, "Metal Blade Records", 2012, "Torture", "Cannibal Corpse", 120, null);

        // Lisätään asiakkaat
        addAsiakas(c, "Matti", "Meikäläinen", "Tuulikuja 7", "matti.meikalainen@mail.com", "04412345678", "mattim");
        addAsiakas(c, "Maija", "Mehiläinen", "Keskuskuja 4", "maija.mehilainen@mail.com", "0501234555", null);
        addAsiakas(c, "Mikko", "Mattila", "Katukuja 5", "mattilanmikko@mail.com", "0505500505", "mattilanmikko21");
        addAsiakas(c, "Pirjo", "Pirjonen", "Kujatie 6", "pirjonen@mail.com", "04498765443", "pirjopirjo65");

        // Lisätään käyttäjät
        addKayttaja(c, "mattim", "Matti", "Meikäläinen", "matti.meikalainen@mail.com", "Nel0SOlut", 1);
        addKayttaja(c, "mattilanmikko21", "Mikko", "Mattila", "mattilanmikko@mail.com", "H0nd4Mi3s", 3);
        addKayttaja(c, "pirjopirjo65", "Pirjo", "Pirjonen", "pirjonen@mail.com", "M3shugg4h", 4);

        // Lisätään myyjät
        addMyyjat(c, "Mika", "Mikkonen", "Matveisenkuja 3", "mikamikkonen@mail.com", "04012345678");
        addMyyjat(c, "Milla", "Lamminen", "Lammilankatu 9", "millalammi@mail.com", "00412345678");
        addMyyjat(c, "Maria", "Kareinen", "Katukuja 9", "maria.kareinen@mail.com", "0202345672");

        // Lisätään levytilaukset
        addLevytilaus(c, 2, "Napalm Records", Date.valueOf("2021-01-02"), 1666.25);
        addLevytilaus(c, 2, "Atomic Fire", Date.valueOf("2021-04-03"), 1334.43);
        addLevytilaus(c, 3, "Metal Blade Records", Date.valueOf("2021-05-05"), 1544.45);
        addLevytilaus(c, 1, "Atomic Fire", Date.valueOf("2021-05-06"), 1223.23);

        // Lisätään asiakastilaukset
        addAsiakastilaus(c, 1, 3, Date.valueOf("2021-02-02"), 24.99);
        addAsiakastilaus(c, 1, 2, Date.valueOf("2021-03-04"), 120.00);
        addAsiakastilaus(c, 3, 4, Date.valueOf("2021-06-06"), 99.99);
        addAsiakastilaus(c, 2, 1, Date.valueOf("2022-02-02"), 123.23);

        // Lisätään tilatut albumit
        addTilattuAlbumi(c, 5, 1, 500);
        addTilattuAlbumi(c, 1, 2, 700);
        addTilattuAlbumi(c, 6, 3, 450);
        addTilattuAlbumi(c, 3, 4, 550);

        addTilattuAlbumi(c, 2, 5, 1);
        addTilattuAlbumi(c, 8, 6, 2);
        addTilattuAlbumi(c, 7, 7, 1);
        addTilattuAlbumi(c, 4, 8, 2);

        launch();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("HarjoitustyoFXML.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Albumien myyntijärjestelmä");
	    primaryStage.setResizable(false);
        primaryStage.show();

    }

    /**
     * Luo tietokannan
     * @param c Connection
     * @param db Tietokannan nimi
     * @throws SQLException
     */
    private static void  createDatabase(Connection c, String db) throws SQLException {

        Statement stmt = c.createStatement();
        stmt.executeQuery("DROP DATABASE IF EXISTS " + db);
        System.out.println("\t>> Tietokanta " + db + " tuhottu");

        stmt.executeQuery("CREATE DATABASE " + db);
        System.out.println("\t>> Tietokanta " + db + " luotu");

        stmt.executeQuery("USE " + db);
        System.out.println("\t>> Käytetään tietokantaa " + db);

    }

    /**
     * Luo taulun
     * @param c Connection
     * @param sql taulun nimi
     * @throws SQLException
     */
    private static void createTable(Connection c, String sql) throws SQLException {

        Statement stmt = c.createStatement();
        stmt.executeQuery(sql);
        System.out.println("\t>> Taulu luotu");

    }


    
    /** Lisää albumin tietokantaan
     * @param c Connection string
     * @param ean Albumin ean
     * @param levy_yhtio Albumin levy-yhtiö
     * @param julkaisuvuosi Albumin julkaisuvuosi
     * @param nimi Albumin nimi
     * @param artisti Albumin artisti
     * @param myynti Albumin myynti
     * @param kuva Albumin kuva String-muodossa
     * @throws SQLException
     */
    private static void addAlbumi(Connection c, int ean, String levy_yhtio, int julkaisuvuosi, String nimi, String artisti, int myynti, String kuva) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO albumit (ean, levy_yhtiö, julkaisuvuosi, nimi, artisti, myynti, kuva)" + "VALUES (?, ?, ?, ?, ?, ?, ?)");

        ps.setInt(1, ean);
        ps.setString(2, levy_yhtio);
        ps.setInt(3, julkaisuvuosi);
        ps.setString(4, nimi);
        ps.setString(5, artisti);
        ps.setInt(6, myynti);
        ps.setString(7, kuva);
        ps.execute();

        System.out.println("\t>> Lisätty albumi " + nimi);

    }

    
    /** Lisää asiakkaan tietokantaan
     * @param c Connection string
     * @param etunimi Asiakkaan etunimi
     * @param sukunimi Asiakkaan sukunimi
     * @param osoite Asiakkaan osoite
     * @param sposti Asiakkaan sähköposti
     * @param puhnum Asiakkaan puhelinnumero
     * @param kayttajatunnus Asiakkaan käyttäjätunnus
     * @throws SQLException
     */
    private static void addAsiakas(Connection c, String etunimi, String sukunimi, String osoite, String sposti, String puhnum, String kayttajatunnus) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO asiakkaat (etunimi, sukunimi, osoite, sähköposti, puhelinnumero, käyttäjätunnus) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, sposti);
        ps.setString(5, puhnum);
        ps.setString(6, kayttajatunnus);
        ps.execute();

        System.out.println("\t>> Lisätty asiakas " + etunimi + " " + sukunimi);

    }

    
    /** Lisää käyttäjän tietokantaan
     * @param c Connection string
     * @param kayttajatunnus Käyttäjätunnus
     * @param etunimi Käyttäjän etunimi
     * @param sukunimi Käyttäjän sukunimi 
     * @param sposti Käyttäjän sähköposti
     * @param salasana Käyttäjän salasana
     * @param asiakastunnus Käyttäjän asiakastunnus
     * @throws SQLException
     */
    private static void addKayttaja(Connection c, String kayttajatunnus, String etunimi, String sukunimi, String sposti, String salasana, int asiakastunnus) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO käyttäjät (käyttäjätunnus, etunimi, sukunimi, sähköposti, salasana, asiakastunnus) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1, kayttajatunnus);
        ps.setString(2, etunimi);
        ps.setString(3, sukunimi);
        ps.setString(4, sposti);
        ps.setString(5, salasana);
        ps.setInt(6, asiakastunnus);
        ps.execute();

        System.out.println("\t>> Lisätty käyttäjä " + kayttajatunnus);

    }

    
    /** Lisää myyjän tietokantaan
     * @param c Connection string 
     * @param etunimi Myyjän etunimi
     * @param sukunimi Myyjän sukunimi
     * @param osoite Myyjän osoite
     * @param sposti Myyjän sähköposti
     * @param puhnum Myyjän puhelinnumero
     * @throws SQLException
     */
    private static void addMyyjat(Connection c, String etunimi, String sukunimi, String osoite, String sposti, String puhnum) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO myyjät (etunimi, sukunimi, osoite, sähköposti, puhelinnumero) VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, etunimi);
        ps.setString(2, sukunimi);
        ps.setString(3, osoite);
        ps.setString(4, sposti);
        ps.setString(5, puhnum);
        ps.execute();

        System.out.println("\t>> Lisätty myyjä " + etunimi + " " + sukunimi);

    }

    
    /** Lisää tilattu albumi tietokantaan
     * @param c Connection string
     * @param ean Albumin ean
     * @param tilaustunnus Tilauksen tunnus
     * @param maara Albumien määrä
     * @throws SQLException
     */
    private static void addTilattuAlbumi(Connection c, int ean, int tilaustunnus, int maara) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO tilatut_albumit (ean, tilaustunnus, määrä) VALUES (?, ?, ?)");
        ps.setInt(1, ean);
        ps.setInt(2, tilaustunnus);
        ps.setInt(3, maara);
        ps.execute();

    }

    
    /** Lisää levytilauksen tietokantaan
     * @param c Connection string 
     * @param myyjatunnus Tilaajan myyjätunnus
     * @param levy_yhtio Levy-yhtiö mistä tilattu
     * @param pvm Tilauksen päivämäärä
     * @param hinta Tilauksen hinta
     * @throws SQLException
     */
    private static void addLevytilaus(Connection c, int myyjatunnus, String levy_yhtio, Date pvm, double hinta) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO tilaukset (tilaustyyppi, myyjätunnus, levy_yhtiö, päivämäärä, hinta) VALUES ('Levytilaus', ?, ?, ?, ?)");
        ps.setInt(1, myyjatunnus);
        ps.setString(2, levy_yhtio);
        ps.setDate(3, pvm);
        ps.setDouble(4, hinta);
        ps.execute();

        System.out.println("\t>> Lisätty uusi levytilaus");

    }

    
    /** Lisää asiakastilauksen tietokantaan
     * @param c Connection string
     * @param myyjatunnus Tilauksen käsitelleen myyjän tunnus
     * @param asiakastunnus Tilaajan asiakastunnus
     * @param pvm Tilauksen päivämäärä
     * @param hinta Tilauksen hinta
     * @throws SQLException
     */
    private static void addAsiakastilaus(Connection c, int myyjatunnus, int asiakastunnus, Date pvm, double hinta) throws SQLException {

        PreparedStatement ps = c.prepareStatement("INSERT INTO tilaukset (tilaustyyppi, myyjätunnus, asiakastunnus, päivämäärä, hinta) VALUES ('Asiakastilaus', ?, ?, ?, ?)");
        ps.setInt(1, myyjatunnus);
        ps.setInt(2, asiakastunnus);
        ps.setDate(3, pvm);
        ps.setDouble(4, hinta);
        ps.execute();

        System.out.println("\t>> Lisätty uusi asiakastilaus");

    }

}