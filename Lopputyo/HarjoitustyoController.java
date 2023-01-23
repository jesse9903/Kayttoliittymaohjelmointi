import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HarjoitustyoController implements Initializable {

    
    /** Initialisoi ikkunan
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Haetaan tiedot tietokannasta
        try {
            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        albumiLista = Database.haeAlbumit(con);
        asiakasTilausLista = Database.haeAsiakastilaukset(con);
        asiakasLista = Database.haeAsiakkaat(con);
        kayttajaLista = Database.haeKayttajat(con);
        levyTilausLista = Database.haeLevytilaukset(con);
        myyjaLista = Database.haeMyyjat(con);

        } catch (SQLException e) {
            System.out.println("Virhe yhteyden avaamisessa"); 
        }

        // Muuttaa näkymän valitun perusteella
        menuAlbumit.setOnAction(event -> {

            spAlbumit.setVisible(true);
            spTilaukset.setVisible(false);
            spAsiakkaat.setVisible(false);
            spMyyjat.setVisible(false);

            albumiHaku.setVisible(true);
            asiakastilausHaku.setVisible(false);
            levytilausHaku.setVisible(false);
            asiakasHaku.setVisible(false);
            kayttajaHaku.setVisible(false);
            myyjaHaku.setVisible(false);
            
            nakyma.setText("Albumit");

        });

        menuTilaukset.setOnAction(event -> {

            spAlbumit.setVisible(false);
            spTilaukset.setVisible(true);
            spAsiakkaat.setVisible(false);
            spMyyjat.setVisible(false);

            albumiHaku.setVisible(false);
            kayttajaHaku.setVisible(false);
            myyjaHaku.setVisible(false);
            asiakasHaku.setVisible(false);

            if (tabAsiakastilaukset.isSelected()) {
                asiakastilausHaku.setVisible(true);
                levytilausHaku.setVisible(false);

            } else {
                levytilausHaku.setVisible(true);
                asiakastilausHaku.setVisible(false);
            }

            nakyma.setText("Tilaukset");

        });

        menuAsiakkaat.setOnAction(event -> {

            spAlbumit.setVisible(false);
            spTilaukset.setVisible(false);
            spAsiakkaat.setVisible(true);
            spMyyjat.setVisible(false);

            albumiHaku.setVisible(false);
            asiakastilausHaku.setVisible(false);
            myyjaHaku.setVisible(false);
            levytilausHaku.setVisible(false);

            if (tabAsiakkaat.isSelected()) {
                asiakasHaku.setVisible(true);
                kayttajaHaku.setVisible(false);

            } else {
                kayttajaHaku.setVisible(true);
                asiakasHaku.setVisible(false);
            }
            
            nakyma.setText("Asiakkaat");

        });

        menuMyyjat.setOnAction(event -> {

            spAlbumit.setVisible(false);
            spTilaukset.setVisible(false);
            spAsiakkaat.setVisible(false);
            spMyyjat.setVisible(true);

            albumiHaku.setVisible(false);
            asiakastilausHaku.setVisible(false);
            levytilausHaku.setVisible(false);
            asiakasHaku.setVisible(false);
            kayttajaHaku.setVisible(false);
            myyjaHaku.setVisible(true);
            
            nakyma.setText("Myyjät");

        });

        // Vaihdetaan haku-kohdan textfieldin ja menubuttonin nimet valitun itemin mukaan
        menuAlbuminNimi.setOnAction(event -> {

            albuminNimiValittu = true;
            albuminArtistiValittu = false;
            albuminLevy_yhtioValittu = false;
            albuminEanValittu = false;

            menuAlbuminHaku.setText("Albumin nimi");
            txtfHaeAlbumia.setPromptText("Albumin nimi");

        });

        menuArtisti.setOnAction(event -> {

            albuminNimiValittu = false;
            albuminArtistiValittu = true;
            albuminLevy_yhtioValittu = false;
            albuminEanValittu = false;

            menuAlbuminHaku.setText("Artisti");
            txtfHaeAlbumia.setPromptText("Artisti");

        });

        menuLevy_yhtio.setOnAction(event -> {

            albuminNimiValittu = false;
            albuminArtistiValittu = false;
            albuminLevy_yhtioValittu = true;
            albuminEanValittu = false;

            menuAlbuminHaku.setText("Levy-yhtiö");
            txtfHaeAlbumia.setPromptText("Levy-yhtiö");

        });

        menuEAN.setOnAction(event -> {

            albuminNimiValittu = false;
            albuminArtistiValittu = false;
            albuminLevy_yhtioValittu = false;
            albuminEanValittu = true;

            menuAlbuminHaku.setText("EAN");
            txtfHaeAlbumia.setPromptText("EAN");

        });

        menuLevTilTunnus.setOnAction(event -> {

            levytilauksenTunnusValittu = true;
            levytilauksenHintaValittu = false;
            levytilauksenPvmValittu = false;

            menubHaeLevytilaus.setText("Tilaustunnus");
            txtfHaeLevytilaus.setPromptText("Tilaustunnus");

        });

        menuLevTilPvm.setOnAction(event -> {

            levytilauksenTunnusValittu = false;
            levytilauksenHintaValittu = false;
            levytilauksenPvmValittu = true;

            menubHaeLevytilaus.setText("Päivämäärä");
            txtfHaeLevytilaus.setPromptText("Päivämäärä");

        });

        menuLevTilHinta.setOnAction(event -> {

            levytilauksenTunnusValittu = false;
            levytilauksenHintaValittu = true;
            levytilauksenPvmValittu = false;

            menubHaeLevytilaus.setText("Hinta");
            txtfHaeLevytilaus.setPromptText("Hinta");

        });

        menuAsTilTunnus.setOnAction(event -> {

            asiakatilauksenTunnusValittu = true;
            asiakastilauksenHintaValittu = false;
            asiakastilauksenPvmValittu = false;

            menubHaeAsiakastilaus.setText("Tilaustunnus");
            txtfHaeAsTilaus.setPromptText("Tilaustunnus");

        });

        menuAsTilHinta.setOnAction(event -> {

            asiakatilauksenTunnusValittu = false;
            asiakastilauksenHintaValittu = true;
            asiakastilauksenPvmValittu = false;

            menubHaeAsiakastilaus.setText("Hinta");
            txtfHaeAsTilaus.setPromptText("Hinta");

        });

        menuAsTilPvm.setOnAction(event -> {

            asiakatilauksenTunnusValittu = false;
            asiakastilauksenHintaValittu = false;
            asiakastilauksenPvmValittu = true;

            menubHaeAsiakastilaus.setText("Päivämäärä");
            txtfHaeAsTilaus.setPromptText("Päivämäärä");

        });

        menuAsTunnus.setOnAction(event -> {

            asiakkaanTunnusValittu = true;
            asiakkaanNimiValittu = false;

            menubHaeAsiakas.setText("Asiakastunnus");
            txtfHaeAsiakas.setPromptText("Asiakastunnus");

        });

        menuAsNimi.setOnAction(event -> {

            asiakkaanTunnusValittu = false;
            asiakkaanNimiValittu = true;

            menubHaeAsiakas.setText("Asiakkaan nimi");
            txtfHaeAsiakas.setPromptText("Asiakkaan nimi");

        });

        menuKayttajanNimi.setOnAction(event -> {

            kayttajanNimiValittu = true;
            kayttajanTunnusValittu = false;

            menubKayttajanHaku.setText("Käyttäjän nimi");
            txtfHaeKayttaja.setPromptText("Käyttäjän nimi");

        });

        menuKayttajatunnus.setOnAction(event -> {

            kayttajanNimiValittu = false;
            kayttajanTunnusValittu = true;

            menubKayttajanHaku.setText("Käyttäjätunnus");
            txtfHaeKayttaja.setPromptText("Käyttäjätunnus");

        });

        menuMyyjaNimi.setOnAction(event -> {

            myyjanNimiValittu = true;
            myyjanTunnusValittu = false;

            menubHaeMyyja.setText("Myyjän nimi");
            txtfHaeMyyja.setPromptText("Myyjän nimi");

        });

        menuMyyjaTunnus.setOnAction(event -> {

            myyjanNimiValittu = false;
            myyjanTunnusValittu = true;

            menubHaeMyyja.setText("Myyjätunnus");
            txtfHaeMyyja.setPromptText("Myyjätunnus");

        });

        // Bindataan haku-napit tetxfieldeihin, jolloin nappi aukeaa kun textfield ei ole tyhjä
        btnAlbuminHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeAlbumia.textProperty()));
        btnAsTilHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeAsTilaus.textProperty()));
        btnLevTilHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeLevytilaus.textProperty()));
        btnKayttajaHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeKayttaja.textProperty()));
        btnAsiakasHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeAsiakas.textProperty()));
        btnMyyjaHaku.disableProperty().bind(Bindings.isEmpty(txtfHaeMyyja.textProperty()));
        
        // Muutetaan haku-kentät valitun tabin mukaan tabpaneissa
        tabAsiakastilaukset.setOnSelectionChanged(event -> {

            if (tabAsiakastilaukset.isSelected()) {

                asiakastilausHaku.setVisible(true);
                levytilausHaku.setVisible(false);
            } 
        });

        tabLevytilaukset.setOnSelectionChanged(event -> {

            if (tabLevytilaukset.isSelected()) {

                asiakastilausHaku.setVisible(false);
                levytilausHaku.setVisible(true);
            } 
        });

        tabAsiakkaat.setOnSelectionChanged(event -> {

            if (tabAsiakkaat.isSelected()) {

                asiakasHaku.setVisible(true);
                kayttajaHaku.setVisible(false);
            } 
        });

        tabKayttajat.setOnSelectionChanged(event -> {

            if (tabKayttajat.isSelected()) {

                asiakasHaku.setVisible(false);
                kayttajaHaku.setVisible(true);
            } 
        });

        // Annetaan ScrollPanelle sisältö sekä asetetaan se koko ikkunan leveydelle
        spAlbumit.setContent(albumit);
        spAlbumit.fitToWidthProperty().set(true);

        spTilaukset.setContent(tilaukset);
        spTilaukset.fitToWidthProperty().set(true);

        spAsiakkaat.setContent(asiakkaat);
        spAsiakkaat.fitToWidthProperty().set(true);

        spMyyjat.setContent(myyjat);
        spMyyjat.fitToWidthProperty().set(true);
        
        // Haetaan tiedot päänäkymään
        try {
            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
            ArrayList<Albumi> albumiLista = Database.haeAlbumit(con);
            
            haeAlbumitPaanakymaan(albumiLista);

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä albumeita päänäkymään");
        }

        try {
            haeLevytilauksetPaanakymaan();

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä levytilauksia päänäkymään");
        }

        try {
            haeAsiakastilauksetPaanakymaan();

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä asiakastilauksia päänäkymään");
        }

        try {
            haeAsiakkaatPaanakymaan();

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä asiakkaita päänäkymään");
        }

        try {
            haeKayttajatPaanakymaan();

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä käyttäjiä päänäkymään");
        }

        try {
            haeMyyjatPaanakymaan();

        } catch (SQLException e) {
            System.out.println("Virhe lisättäessä myyjiä päänäkymään");
        }

        // Asettaa scrollbarin nopeammaksi
        spAlbumit.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            double contentHeight = spAlbumit.getContent().getBoundsInLocal().getHeight();
            double scrollPaneHeight = spAlbumit.getHeight();
            double diff = contentHeight - scrollPaneHeight;
            if (diff < 1) diff = 1;
            double vvalue = spAlbumit.getVvalue();
            spAlbumit.setVvalue(vvalue + -deltaY/diff);
        });

    }

    @FXML
    private Pane tilaukset;

    @FXML
    private VBox albumiHaku;

    @FXML
    private GridPane albumit;

    @FXML
    private VBox asiakasHaku;

    @FXML
    private VBox myyjaHaku;

    @FXML
    private MenuButton nakyma;

    @FXML
    private MenuButton menubHaeLevytilaus;

    @FXML
    private MenuButton menubHaeAsiakastilaus;

    @FXML
    private MenuButton menubHaeAsiakas;

    @FXML
    private MenuButton menubHaeMyyja;

    @FXML
    private MenuButton menubKayttajanHaku;

    @FXML
    private Tab tabAsiakastilaukset;

    @FXML
    private Tab tabAsiakkaat;

    @FXML
    private Tab tabKayttajat;

    @FXML
    private Tab tabLevytilaukset;

    @FXML
    private VBox tilausHaku;

    @FXML
    private VBox asiakastilausHaku;

    @FXML
    private VBox levytilausHaku;

    @FXML
    private VBox kayttajaHaku;

    @FXML
    private TextField txtfHaeKayttaja;

    @FXML
    private TextField txtfHaeAsiakas;

    @FXML
    private TextField txtfHaeAsTilaus;

    @FXML
    private TextField txtfHaeMyyja;

    @FXML
    private TextField txtfHaeLevytilaus;

    @FXML
    private MenuItem menuAlbumit;

    @FXML
    private MenuItem menuAsiakkaat;

    @FXML
    private MenuItem menuMyyjat;

    @FXML
    private MenuItem menuTilaukset;

    @FXML
    private MenuItem menuAlbuminNimi;

    @FXML
    private MenuItem menuArtisti;

    @FXML
    private MenuItem menuLevy_yhtio;

    @FXML
    private MenuItem menuEAN;

    @FXML
    private MenuItem menuLevTilTunnus;

    @FXML
    private MenuItem menuLevTilPvm;

    @FXML
    private MenuItem menuLevTilHinta;

    @FXML
    private MenuItem menuAsNimi;

    @FXML
    private MenuItem menuAsTunnus;

    @FXML
    private MenuItem menuMyyjaNimi;

    @FXML
    private MenuItem menuMyyjaTunnus;

    @FXML
    private MenuItem menuKayttajanNimi;

    @FXML
    private MenuItem menuKayttajatunnus;

    @FXML
    private MenuItem menuAsTilTunnus;

    @FXML
    private MenuItem menuAsTilPvm;

    @FXML
    private MenuItem menuAsTilHinta;

    @FXML
    private ScrollPane spAlbumit;

    @FXML
    private ScrollPane spTilaukset;

    @FXML
    private ScrollPane spMyyjat;

    @FXML
    private ScrollPane spAsiakkaat;

    @FXML
    private ListView<Pane> listAsiakastilaukset;

    @FXML
    private ListView<Pane> listLevytilaukset;

    @FXML
    private ListView<Pane> listKayttajat;

    @FXML
    private ListView<Pane> listAsiakkaat;

    @FXML
    private ListView<Pane> listMyyjat;

    @FXML
    private AnchorPane myyjat;

    @FXML
    private AnchorPane asiakkaat;

    @FXML
    private MenuButton menuAlbuminHaku;

    @FXML
    private TextField txtfHaeAlbumia;

    @FXML
    private Button btnAlbuminHaku;

    @FXML
    private Button btnLevTilHaku;

    @FXML
    private Button btnAsTilHaku;

    @FXML
    private Button btnAsiakasHaku;

    @FXML
    private Button btnMyyjaHaku;

    @FXML
    private Button btnKayttajaHaku;

    @FXML
    private Label lblVirheLevTilHaku;

    @FXML
    private Label lblVirheLevTilHaku2;

    @FXML
    private Label lblVirheAsTilHaku;

    @FXML
    private Label lblVirheAsTilHaku2;

    @FXML
    private Label lblVirheAlbumiHaku;

    @FXML
    private Label lblVirheAsiakasHaku;

    @FXML
    private Label lblVirheMyyjaHaku;

    @FXML
    private Label lblVirheKayttajaHaku;

    ArrayList<Albumi> albumiLista = new ArrayList<>();
    ArrayList<Asiakastilaus> asiakasTilausLista = new ArrayList<>();
    ArrayList<Levytilaus> levyTilausLista = new ArrayList<>();
    ArrayList<Asiakas> asiakasLista = new ArrayList<>();
    ArrayList<Myyja> myyjaLista = new ArrayList<>();
    ArrayList<Kayttaja> kayttajaLista = new ArrayList<>();

    boolean albuminNimiValittu = true;
    boolean albuminArtistiValittu = false;
    boolean albuminLevy_yhtioValittu = false;
    boolean albuminEanValittu = false;

    boolean levytilauksenTunnusValittu = true;
    boolean levytilauksenHintaValittu = false;
    boolean levytilauksenPvmValittu = false;

    boolean asiakatilauksenTunnusValittu = true;
    boolean asiakastilauksenHintaValittu = false;
    boolean asiakastilauksenPvmValittu = false;

    boolean asiakkaanTunnusValittu = false;
    boolean asiakkaanNimiValittu = true;

    boolean kayttajanNimiValittu = true;
    boolean kayttajanTunnusValittu = false;

    boolean myyjanNimiValittu = true;
    boolean myyjanTunnusValittu = false;

    
    /** Avaa klikatun albumin tiedot uuteen ikkunaan
     * @param event
     * @param albumi Klikattu albumi
     * @throws IOException
     */
    @FXML
    void avaaAlbuminTiedot(MouseEvent event, Albumi albumi) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AlbuminTiedotFXML.fxml"));
        Parent root = loader.load();
        AlbuminTiedotController albuminTiedotController = loader.getController();

        albuminTiedotController.dataBind(albumi);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Albumin tiedot");
        stage.setResizable(false);
        stage.show();

    }

    
    /** Avaa klikatun levytilauksen tiedot uuteen ikkunaan
     * @param levytilaus Klikattu levytilaus
     * @throws IOException
     */
    public void avaaLevytilauksenTiedot(Levytilaus levytilaus) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("LevytilauksenTiedotFXML.fxml"));
        Parent root = loader.load();
        LevytilauksenTiedotController levytilauksenTiedotController = loader.getController();

        levytilauksenTiedotController.databind(levytilaus);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Levytilauksen tiedot");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /** Avaa klikatun asiakastilauksen tiedot uuteen ikkunaan
     * @param asiakastilaus Klikattu asiakastilaus
     * @throws IOException
     */
    public void avaaAsiakastilauksenTiedot(Asiakastilaus asiakastilaus) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AsiakastilauksenTiedotFXML.fxml"));
        Parent root = loader.load();
        AsiakastilauksenTiedotController asiakastilauksenTiedotController = loader.getController();

        asiakastilauksenTiedotController.databind(asiakastilaus);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Asiakastilauksen tiedot");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /** Avaa klikatun asiakkaan tiedot uuteen ikkunaan
     * @param asiakas Klikattu asiakas
     * @throws IOException
     */
    public void avaaAsiakkaanTiedot(Asiakas asiakas) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AsiakkaanTiedotFXML.fxml"));
        Parent root = loader.load();
        AsiakkaanTiedotController asiakkaanTiedotController = loader.getController();

        asiakkaanTiedotController.databind(asiakas);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Asiakkaan tiedot");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /** Avaa klikatun käyttäjän tiedot uuteen ikkunaan
     * @param kayttaja Klikattu käyttäjä
     * @throws IOException
     */
    public void avaaKayttajanTiedot(Kayttaja kayttaja) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("KayttajanTiedotFXML.fxml"));
        Parent root = loader.load();
        KayttajanTiedotController kayttajanTiedotController = loader.getController();

        kayttajanTiedotController.databind(kayttaja);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Käyttäjän tiedot");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /** Avaa klikatun myyjä tiedot uuteen ikkunaan
     * @param myyja Klikattu myyjä
     * @throws IOException
     */
    public void avaaMyyjanTiedot(Myyja myyja) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyyjanTiedotFXML.fxml"));
        Parent root = loader.load();
        MyyjanTiedotController myyjanTiedotController = loader.getController();

        myyjanTiedotController.databind(myyja);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Myyjan tiedot");
        stage.setResizable(false);
        stage.show();
        
    }

    
    /** Avaa lisäys-ikkunan valitun näkymän perusteella (esim. jos näkymä on albumi, avaa albumin lisäys -ikkunan)
     * @param event
     * @throws IOException
     */
    @FXML
    void avaaLisaa(ActionEvent event) throws IOException {

        if (spAlbumit.isVisible()) {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaAlbumiFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää albumi");
            stage.setResizable(false);
            stage.show();

        } else if (spTilaukset.isVisible()) {

            if (tabLevytilaukset.isSelected()) {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaLevytilausFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää levytilaus");
            stage.setResizable(false);
            stage.show();

            } else {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaAsiakastilausFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää asiakastilaus");
            stage.setResizable(false);
            stage.show();

            }
        }

        if (spAsiakkaat.isVisible()) {

            if (tabAsiakkaat.isSelected()) {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaAsiakasFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää asiakas");
            stage.setResizable(false);
            stage.show();

            } else {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaKayttajaFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää käyttäjä");
            stage.setResizable(false);
            stage.show();

            }
        }

        if (spMyyjat.isVisible()) {

            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("LisaaMyyjaFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Lisää myyjä");
            stage.setResizable(false);
            stage.show();
            
        }

    }

    
    /** Avaa sovelluksen tiedot -ikkunan
     * @param event
     */
    @FXML
    void avaaSovelluksenTiedot(ActionEvent event) {

        Alert info = new Alert(AlertType.INFORMATION, "Tekijä: Jesse Korkiakoski \nVersio: 1.0 \nJulkaisuvuosi: 2022", ButtonType.OK);
        info.setHeaderText("Albumien Myyntijärjestelmä");
        info.setTitle("Sovelluksen tiedot");
        info.show();

    }

    
    /** Hakee albumit annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeAlbumia(ActionEvent event) throws SQLException {

        lblVirheAlbumiHaku.setVisible(false);

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        if (albuminNimiValittu) {
            ArrayList<Albumi> albumiLista = Database.hakuAlbumitNimi(con, txtfHaeAlbumia.getText());
            haeAlbumitPaanakymaan(albumiLista);
        
        } else if (albuminArtistiValittu) {
            ArrayList<Albumi> albumiLista = Database.hakuAlbumitArtisti(con, txtfHaeAlbumia.getText());
            haeAlbumitPaanakymaan(albumiLista);
        
        } else if (albuminLevy_yhtioValittu) {
            ArrayList<Albumi> albumiLista = Database.hakuAlbumitLevy_yhtio(con, txtfHaeAlbumia.getText());
            haeAlbumitPaanakymaan(albumiLista);

        } else if (albuminEanValittu) {

            boolean virheEan = (!txtfHaeAlbumia.getText().matches("[0-9]+"));

            if (virheEan) {

                lblVirheAlbumiHaku.setVisible(true);
                
            } else {
                ArrayList<Albumi> albumiLista = Database.hakuAlbumitEan(con, Integer.parseInt(txtfHaeAlbumia.getText()));
                haeAlbumitPaanakymaan(albumiLista);
            }
        }
    }

    
    /** Hakee asiakastilaukset annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeAsiakastilausta(ActionEvent event) throws SQLException {

        lblVirheAsTilHaku.setVisible(false);
        lblVirheAsTilHaku2.setVisible(false);

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        boolean virheAsiakastilaus = false;

        if (asiakatilauksenTunnusValittu) {

            virheAsiakastilaus = (!txtfHaeAsTilaus.getText().matches("[0-9]+"));

            if (virheAsiakastilaus) {
                lblVirheAsTilHaku.setText("Syötä vain numeroita");
                lblVirheAsTilHaku.setVisible(true);

            }  else {

                ArrayList<Asiakastilaus> asiakastilausLista = Database.hakuAsiakastilausTunnus(con, Integer.parseInt(txtfHaeAsTilaus.getText()));
                haeAsiakastilauksetPaanakymaan(asiakastilausLista);

            }
        
        } else if (asiakastilauksenHintaValittu) {

            virheAsiakastilaus = (!(txtfHaeAsTilaus.getText().matches("[0-9]+")));

            if (virheAsiakastilaus) {

                lblVirheAsTilHaku.setText("Syötä vain numeroita");
                lblVirheAsTilHaku.setVisible(true);

            } else {

                ArrayList<Asiakastilaus> asiakastilausLista = Database.hakuAsiakastilausHinta(con, Integer.parseInt(txtfHaeAsTilaus.getText()));
                haeAsiakastilauksetPaanakymaan(asiakastilausLista);
            }
        
        } else if (asiakastilauksenPvmValittu) {

            try {
               Date testiPvm = Date.valueOf(txtfHaeAsTilaus.getText());

            } catch (Exception e) {
                virheAsiakastilaus = true;
            }

            if (virheAsiakastilaus) {

                lblVirheAsTilHaku.setText("Syötä muodossa");
                lblVirheAsTilHaku.setVisible(true);
                lblVirheAsTilHaku2.setVisible(true);

            } else {

                Date pvm = Date.valueOf(txtfHaeAsTilaus.getText());

                ArrayList<Asiakastilaus> asiakastilausLista = Database.hakuAsiakastilausPvm(con, pvm);
                haeAsiakastilauksetPaanakymaan(asiakastilausLista);
            }
        }
    }

    
    /** Hakee asiakkaat annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeAsiakkaat(ActionEvent event) throws SQLException {

        lblVirheAsiakasHaku.setVisible(false);

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        boolean virheAsiakas = false;

        if (asiakkaanTunnusValittu) {

            virheAsiakas = (!txtfHaeAsiakas.getText().matches("[0-9]+"));

            if (virheAsiakas) {
                lblVirheAsiakasHaku.setText("Syötä vain numeroita");
                lblVirheAsiakasHaku.setVisible(true);
            
            } else {
                ArrayList<Asiakas> asiakasLista = Database.hakuAsiakasTunnus(con, Integer.parseInt(txtfHaeAsiakas.getText()));
                haeAsiakkaatPaanakymaan(asiakasLista);
            }

        } else {
            ArrayList<Asiakas> asiakasLista = Database.hakuAsiakasNimi(con, txtfHaeAsiakas.getText());
            haeAsiakkaatPaanakymaan(asiakasLista);
        }

    }

    
    /** Hakee levytilaukset annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeLevytilausta(ActionEvent event) throws SQLException {

        lblVirheLevTilHaku.setVisible(false);
        lblVirheLevTilHaku2.setVisible(false);

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        boolean virheLevytilaus = false;

        if (levytilauksenTunnusValittu) {

            virheLevytilaus = (!txtfHaeLevytilaus.getText().matches("[0-9]+"));

            if (virheLevytilaus) {
                lblVirheLevTilHaku.setText("Syötä vain numeroita");
                lblVirheLevTilHaku.setVisible(true);

            }  else {

                ArrayList<Levytilaus> levytilausLista = Database.hakuLevytilausTunnus(con, Integer.parseInt(txtfHaeLevytilaus.getText()));
                haeLevytilauksetPaanakymaan(levytilausLista);
            }
        
        } else if (levytilauksenHintaValittu) {

            virheLevytilaus = (!(txtfHaeLevytilaus.getText().matches("[0-9]+")));

            if (virheLevytilaus) {

                lblVirheLevTilHaku.setText("Syötä vain numeroita");
                lblVirheLevTilHaku.setVisible(true);

            } else {

                ArrayList<Levytilaus> levytilausLista = Database.hakuLevytilausHinta(con, Integer.parseInt(txtfHaeLevytilaus.getText()));
                haeLevytilauksetPaanakymaan(levytilausLista);
            }
        
        } else if (levytilauksenPvmValittu) {

            try {
               Date testiPvm = Date.valueOf(txtfHaeLevytilaus.getText());

            } catch (Exception e) {
                virheLevytilaus = true;
            }

            if (virheLevytilaus) {

                lblVirheLevTilHaku.setText("Syötä muodossa");
                lblVirheLevTilHaku.setVisible(true);
                lblVirheLevTilHaku2.setVisible(true);

            } else {

                Date pvm = Date.valueOf(txtfHaeLevytilaus.getText());

                ArrayList<Levytilaus> levytilausLista = Database.hakuLevytilausPvm(con, pvm);
                haeLevytilauksetPaanakymaan(levytilausLista);
            }
        }

    }

    
    /** Hakee myyjät annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeMyyja(ActionEvent event) throws SQLException {

       lblVirheMyyjaHaku.setVisible(false);

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        boolean virheMyyja = false;

        if (myyjanTunnusValittu) {

            virheMyyja = (!txtfHaeMyyja.getText().matches("[0-9]+"));

            if (virheMyyja) {
                lblVirheMyyjaHaku.setText("Syötä vain numeroita");
                lblVirheMyyjaHaku.setVisible(true);
            
            } else {
                ArrayList<Myyja> myyjaLista = Database.hakuMyyjaTunnus(con, Integer.parseInt(txtfHaeMyyja.getText()));
                haeMyyjatPaanakymaan(myyjaLista);
            }

        } else {
            ArrayList<Myyja> myyjaLista = Database.hakuMyyjaNimi(con, txtfHaeMyyja.getText());
            haeMyyjatPaanakymaan(myyjaLista);
        }

    }

    
    
    /** Hakee käyttäjät annettujen tietojen perusteella ja bindaa ne päänäkymään
     * @param event
     * @throws SQLException
     */
    @FXML
    void haeKayttaja(ActionEvent event) throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        if (kayttajanNimiValittu) {
            ArrayList<Kayttaja> kayttajaLista = Database.hakuKayttajaNimi(con, txtfHaeKayttaja.getText());
            haeKayttajatPaanakymaan(kayttajaLista);

        } else {
            ArrayList<Kayttaja> kayttajaLista = Database.hakuKayttajaTunnus(con, txtfHaeKayttaja.getText());
            haeKayttajatPaanakymaan(kayttajaLista);
        }

    }

    
    /** Sulkee sovelluksen
     * @param event
     */
    @FXML
    void suljeSovellus(ActionEvent event) {

        Stage thisStage = (Stage) btnAlbuminHaku.getScene().getWindow();
        thisStage.close();

    }
    
    /** Lataa tiedot uudestaan sivulle
     * @param event
     * @throws SQLException
     */
    @FXML
    void paivitaSivu(ActionEvent event) throws SQLException {

        if (spAlbumit.isVisible()) {

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
            ArrayList<Albumi> albumiLista = Database.haeAlbumit(con);
            
            haeAlbumitPaanakymaan(albumiLista);
        }

        if (spTilaukset.isVisible()) {
            
            if (tabLevytilaukset.isSelected()) {
                haeLevytilauksetPaanakymaan();

            } else {
                haeAsiakastilauksetPaanakymaan();
            }
        }

        if (spAsiakkaat.isVisible()) {

            if (tabAsiakkaat.isSelected()) {
                haeAsiakkaatPaanakymaan();

            } else {
                haeKayttajatPaanakymaan();
            }
        }

        if (spMyyjat.isVisible()) {
            haeMyyjatPaanakymaan();
        }

        // Asettaa scrollbarin nopeammaksi
        spAlbumit.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            double contentHeight = spAlbumit.getContent().getBoundsInLocal().getHeight();
            double scrollPaneHeight = spAlbumit.getHeight();
            double diff = contentHeight - scrollPaneHeight;
            if (diff < 1) diff = 1;
            double vvalue = spAlbumit.getVvalue();
            spAlbumit.setVvalue(vvalue + -deltaY/diff);
        });

    }

    
    /** Hakee albumit päänäkymään
     * @param albumiLista Lista haetuista albumeista
     * @throws SQLException
     */
    public void haeAlbumitPaanakymaan(ArrayList<Albumi> albumiLista) throws SQLException {

        GridPane albumit = new GridPane();
        spAlbumit.setContent(albumit);
        spAlbumit.fitToWidthProperty().set(true);

        albumit.setPadding(new Insets(50, 10, 10, 10));

        albumit.setHgap(30);
        albumit.setVgap(40);

        int sarake = 0;
        int rivi = 0;

        for (int i = 0; i < albumiLista.size(); i++) {

            Albumi lisattava = albumiLista.get(i);

            File kuvaPath;
            Image kuva;
            ImageView imgvKuva = new ImageView();

            try {

                if (lisattava.getKuva() != null) {

                    kuvaPath = new File(lisattava.getKuva());
                    kuva = new Image(kuvaPath.toURI().toURL().toExternalForm());
                    imgvKuva = new ImageView(kuva);
                
                 } else {
                    imgvKuva.setImage(new Image("kuvat/kuvaa_ei_loydy.jpg"));
                 }

            } catch (Exception e) {
                System.out.println("Virhe haettaessa kuvaa");
            }

            Font fontNimi = new Font("default", 14);
            Font fontArtisti = new Font("default", 12);

            Label lblNimi = new Label(lisattava.getNimi());
            lblNimi.setFont(fontNimi);

            Label lblArtisti = new Label(lisattava.getArtisti());
            lblArtisti.setFont(fontArtisti);
            lblArtisti.setTextFill(Color.GREY);

            imgvKuva.setFitWidth(190);
            imgvKuva.setFitHeight(190);

            VBox vbox = new VBox();
            vbox.setPrefWidth(100);
            vbox.setPrefHeight(200);
            vbox.setAlignment(Pos.CENTER);
            vbox.setCursor(Cursor.HAND);

            vbox.setOnMouseClicked((event) -> {

                Albumi albumi = new Albumi();
                String albuminNimi;
                VBox klikattu = (VBox) event.getSource();

                // Haetaan albumin nimi Labelista
                albuminNimi = klikattu.getChildren().get(1).toString();

                // Haetaan bindattava albumi albumiListasta
                for (int j = 0; j < albumiLista.size(); j++) {
                    if (albuminNimi.contains("'" + albumiLista.get(j).getNimi() + "'")) {

                        albumi = albumiLista.get(j);

                        try {
                            avaaAlbuminTiedot(event, albumi);
            
                            } catch (IOException e) {
                                System.out.println("Virhe avattaessa albumin tietoja");
                            }
                        }
                }
            });

            vbox.getChildren().add(imgvKuva);
            vbox.getChildren().add(lblNimi);
            vbox.getChildren().add(lblArtisti);

            albumit.add(vbox, sarake, rivi, 1, 1);

            if (sarake == 2) {
                sarake = 0;
                rivi++;
            
            } else {
                sarake++;
            }
        }
    }

    
    /** Hakee levytilaukset päänäkymään
     * @throws SQLException
     */
    public void haeLevytilauksetPaanakymaan() throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        listLevytilaukset.getItems().clear();

        levyTilausLista = Database.haeLevytilaukset(con);

        for (int i = 0; i < levyTilausLista.size(); i++) {

            Levytilaus levytilaus = levyTilausLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(levytilaus.getTilaustunnus()));
            lblTunnus.setFont(font);

            Label lblHinta = new Label(String.valueOf(levytilaus.getHinta()));
            lblHinta.setFont(font);

            Label lblPvm = new Label(levytilaus.getTilauspvm().toString());
            lblPvm.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblHinta, lblPvm);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblHinta.setLayoutX(147);
            lblPvm.setLayoutX(290);
            
            listLevytilaukset.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Levytilaus klikattuLevytilaus = new Levytilaus();
                String tilaustunnus;
                Pane klikattu = (Pane) event.getSource();

                tilaustunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < levyTilausLista.size(); j++) {

                    if (tilaustunnus.contains("'" + levyTilausLista.get(j).getTilaustunnus() + "'")) {

                        klikattuLevytilaus = levyTilausLista.get(j);

                        try {
                            avaaLevytilauksenTiedot(klikattuLevytilaus);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa levytilauksen tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee levytilaukset päänäkymään
     * @param levytilausLista Lista haetuista levytilauksista
     * @throws SQLException
     */
    public void haeLevytilauksetPaanakymaan (ArrayList<Levytilaus> levytilausLista) throws SQLException {

        listLevytilaukset.getItems().clear();

        for (int i = 0; i < levytilausLista.size(); i++) {

            Levytilaus levytilaus = levytilausLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(levytilaus.getTilaustunnus()));
            lblTunnus.setFont(font);

            Label lblHinta = new Label(String.valueOf(levytilaus.getHinta()));
            lblHinta.setFont(font);

            Label lblPvm = new Label(levytilaus.getTilauspvm().toString());
            lblPvm.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblHinta, lblPvm);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblHinta.setLayoutX(147);
            lblPvm.setLayoutX(290);
            
            listLevytilaukset.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Levytilaus klikattuLevytilaus = new Levytilaus();
                String tilaustunnus;
                Pane klikattu = (Pane) event.getSource();

                tilaustunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < levytilausLista.size(); j++) {

                    if (tilaustunnus.contains("'" + levytilausLista.get(j).getTilaustunnus() + "'")) {

                        klikattuLevytilaus = levytilausLista.get(j);

                        try {
                            avaaLevytilauksenTiedot(klikattuLevytilaus);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa levytilauksen tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee asiakastilaukset päänäkymään
     * @throws SQLException
     */
    public void haeAsiakastilauksetPaanakymaan() throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        listAsiakastilaukset.getItems().clear();

        asiakasTilausLista = Database.haeAsiakastilaukset(con);

        for (int i = 0; i < asiakasTilausLista.size(); i++) {

            Asiakastilaus asiakastilaus = asiakasTilausLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(asiakastilaus.getTilaustunnus()));
            lblTunnus.setFont(font);

            Label lblHinta = new Label(String.valueOf(asiakastilaus.getHinta()));
            lblHinta.setFont(font);

            Label lblPvm = new Label(asiakastilaus.getTilauspvm().toString());
            lblPvm.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblHinta, lblPvm);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblHinta.setLayoutX(147);
            lblPvm.setLayoutX(290);
            
            listAsiakastilaukset.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Asiakastilaus klikattuAsiakastilaus = new Asiakastilaus();
                String tilaustunnus;
                Pane klikattu = (Pane) event.getSource();

                tilaustunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < asiakasTilausLista.size(); j++) {

                    if (tilaustunnus.contains("'" + asiakasTilausLista.get(j).getTilaustunnus() + "'")) {

                        klikattuAsiakastilaus = asiakasTilausLista.get(j);

                        try {
                            avaaAsiakastilauksenTiedot(klikattuAsiakastilaus);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa asiakastilauksen tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee asiakastilaukset päänäkymään
     * @param asiakastilausLista Lista haetuista asiakastilauksista
     * @throws SQLException
     */
    public void haeAsiakastilauksetPaanakymaan(ArrayList<Asiakastilaus> asiakastilausLista) throws SQLException {

        listAsiakastilaukset.getItems().clear();

        for (int i = 0; i < asiakastilausLista.size(); i++) {

            Asiakastilaus asiakastilaus = asiakastilausLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(asiakastilaus.getTilaustunnus()));
            lblTunnus.setFont(font);

            Label lblHinta = new Label(String.valueOf(asiakastilaus.getHinta()));
            lblHinta.setFont(font);

            Label lblPvm = new Label(asiakastilaus.getTilauspvm().toString());
            lblPvm.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblHinta, lblPvm);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblHinta.setLayoutX(147);
            lblPvm.setLayoutX(290);
            
            listAsiakastilaukset.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Asiakastilaus klikattuAsiakastilaus = new Asiakastilaus();
                String tilaustunnus;
                Pane klikattu = (Pane) event.getSource();

                tilaustunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < asiakastilausLista.size(); j++) {

                    if (tilaustunnus.contains("'" + asiakastilausLista.get(j).getTilaustunnus() + "'")) {

                        klikattuAsiakastilaus = asiakastilausLista.get(j);

                        try {
                            avaaAsiakastilauksenTiedot(klikattuAsiakastilaus);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa asiakastilauksen tietoja");
                        }
                    }
                }
            });   
        }
    }


    
    /** Hakee asiakkaat päänäkymään
     * @throws SQLException
     */
    public void haeAsiakkaatPaanakymaan() throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        listAsiakkaat.getItems().clear();

        asiakasLista = Database.haeAsiakkaat(con);

        for (int i = 0; i < asiakasLista.size(); i++) {

            Asiakas asiakas = asiakasLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(asiakas.getAsiakastunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(asiakas.getEtunimi() + " " + asiakas.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblNimi.setLayoutX(147);
            
            listAsiakkaat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Asiakas klikattuAsiakas = new Asiakas();
                String asiakastunnus;
                Pane klikattu = (Pane) event.getSource();

                asiakastunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < asiakasLista.size(); j++) {

                    if (asiakastunnus.contains("'" + asiakasLista.get(j).getAsiakastunnus() + "'")) {

                        klikattuAsiakas = asiakasLista.get(j);

                        try {
                            avaaAsiakkaanTiedot(klikattuAsiakas);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa asiakkaan tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee asiakkaat päänäkymään
     * @param asiakasLista Lista haetuista asiakkaista
     * @throws SQLException
     */
    public void haeAsiakkaatPaanakymaan(ArrayList<Asiakas> asiakasLista) throws SQLException {

        listAsiakkaat.getItems().clear();

        for (int i = 0; i < asiakasLista.size(); i++) {

            Asiakas asiakas = asiakasLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(asiakas.getAsiakastunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(asiakas.getEtunimi() + " " + asiakas.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblNimi.setLayoutX(147);
            
            listAsiakkaat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Asiakas klikattuAsiakas = new Asiakas();
                String asiakastunnus;
                Pane klikattu = (Pane) event.getSource();

                asiakastunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < asiakasLista.size(); j++) {

                    if (asiakastunnus.contains("'" + asiakasLista.get(j).getAsiakastunnus() + "'")) {

                        klikattuAsiakas = asiakasLista.get(j);

                        try {
                            avaaAsiakkaanTiedot(klikattuAsiakas);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa asiakkaan tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee käyttäjät päänäkymään
     * @throws SQLException
     */
    public void haeKayttajatPaanakymaan() throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        listKayttajat.getItems().clear();

        kayttajaLista = Database.haeKayttajat(con);

        for (int i = 0; i < kayttajaLista.size(); i++) {

            Kayttaja kayttaja = kayttajaLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(kayttaja.getKayttajatunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(kayttaja.getEtunimi() + " " + kayttaja.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(9);
            lblNimi.setLayoutX(148);
            
            listKayttajat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Kayttaja klikattuKayttaja = new Kayttaja();
                String kayttajatunnus;
                Pane klikattu = (Pane) event.getSource();

                kayttajatunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < kayttajaLista.size(); j++) {

                    if (kayttajatunnus.contains("'" + kayttajaLista.get(j).getKayttajatunnus() + "'")) {

                        klikattuKayttaja = kayttajaLista.get(j);

                        try {
                            avaaKayttajanTiedot(klikattuKayttaja);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa käyttäjän tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee käyttäjä päänäkymään
     * @param kayttajaLista Lista haetuista käyttäjistä
     * @throws SQLException
     */
    public void haeKayttajatPaanakymaan(ArrayList<Kayttaja> kayttajaLista) throws SQLException {

        listKayttajat.getItems().clear();

        for (int i = 0; i < kayttajaLista.size(); i++) {

            Kayttaja kayttaja = kayttajaLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(kayttaja.getKayttajatunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(kayttaja.getEtunimi() + " " + kayttaja.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(9);
            lblNimi.setLayoutX(148);
            
            listKayttajat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Kayttaja klikattuKayttaja = new Kayttaja();
                String kayttajatunnus;
                Pane klikattu = (Pane) event.getSource();

                kayttajatunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < kayttajaLista.size(); j++) {

                    if (kayttajatunnus.contains("'" + kayttajaLista.get(j).getKayttajatunnus() + "'")) {

                        klikattuKayttaja = kayttajaLista.get(j);

                        try {
                            avaaKayttajanTiedot(klikattuKayttaja);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa käyttäjän tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee myyjät päänäkymään
     * @throws SQLException
     */
    public void haeMyyjatPaanakymaan() throws SQLException {

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
        
        listMyyjat.getItems().clear();

        myyjaLista = Database.haeMyyjat(con);

        for (int i = 0; i < myyjaLista.size(); i++) {

            Myyja myyja = myyjaLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(myyja.getMyyjatunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(myyja.getEtunimi() + " " + myyja.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblNimi.setLayoutX(147);
            
            listMyyjat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Myyja klikattuMyyja = new Myyja();
                String myyjatunnus;
                Pane klikattu = (Pane) event.getSource();

                myyjatunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < myyjaLista.size(); j++) {

                    if (myyjatunnus.contains("'" + myyjaLista.get(j).getMyyjatunnus() + "'")) {

                        klikattuMyyja = myyjaLista.get(j);

                        try {
                            avaaMyyjanTiedot(klikattuMyyja);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa myyjän tietoja");
                        }
                    }
                }
            });   
        }
    }

    
    /** Hakee myyjät päänäkymään
     * @param myyjaLista Lista haetuista myyjistä
     * @throws SQLException
     */
    public void haeMyyjatPaanakymaan(ArrayList<Myyja> myyjaLista) throws SQLException {

        listMyyjat.getItems().clear();

        for (int i = 0; i < myyjaLista.size(); i++) {

            Myyja myyja = myyjaLista.get(i);

            Font font = new Font("default", 14);
            
            Label lblTunnus = new Label(String.valueOf(myyja.getMyyjatunnus()));
            lblTunnus.setFont(font);

            Label lblNimi = new Label(String.valueOf(myyja.getEtunimi() + " " + myyja.getSukunimi()));
            lblNimi.setFont(font);

            Pane paneListItem = new Pane(lblTunnus, lblNimi);

            paneListItem.setCursor(Cursor.HAND);

            lblTunnus.setLayoutX(25);
            lblNimi.setLayoutX(147);
            
            listMyyjat.getItems().add(paneListItem);

            paneListItem.setOnMouseClicked(event -> {

                Myyja klikattuMyyja = new Myyja();
                String myyjatunnus;
                Pane klikattu = (Pane) event.getSource();

                myyjatunnus = klikattu.getChildren().get(0).toString();

                for (int j = 0; j < myyjaLista.size(); j++) {

                    if (myyjatunnus.contains("'" + myyjaLista.get(j).getMyyjatunnus() + "'")) {

                        klikattuMyyja = myyjaLista.get(j);

                        try {
                            avaaMyyjanTiedot(klikattuMyyja);

                        } catch (IOException e) {
                            System.out.println("Virhe avattaessa myyjän tietoja");
                        }
                    }
                }
            });   
        }
    }

}
