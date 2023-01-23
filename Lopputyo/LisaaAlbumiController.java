import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class LisaaAlbumiController implements Initializable {

    @FXML
    private ImageView imgAlbuminKuva;

    @FXML
    private Pane lisaaAlbumi;

    @FXML
    private TextField txtfArtisti;

    @FXML
    private TextField txtfEAN;

    @FXML
    private TextField txtfJulkaisuvuosi;

    @FXML
    private TextField txtfLevy_yhtio;

    @FXML
    private TextField txtfMyynti;

    @FXML
    private TextField txtfNimi;

    @FXML
    private Button btnTallennaUusiAlbumi;

    @FXML
    private Label lblVirhe1;

    @FXML
    private Label lblVirhe2;

    @FXML
    private Label lblVirhe3;

    private String lisattavanKuva;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
         // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
         btnTallennaUusiAlbumi.disableProperty().bind(Bindings.isEmpty(txtfArtisti.textProperty()).
         or(Bindings.isEmpty(txtfEAN.textProperty())).or(Bindings.isEmpty(txtfJulkaisuvuosi.textProperty())).
         or(Bindings.isEmpty(txtfLevy_yhtio.textProperty())).or(Bindings.isEmpty(txtfMyynti.textProperty())).
         or(Bindings.isEmpty(txtfNimi.textProperty()))
         );

        

    }

    
    /** Lisää kuvan albumille
     * @param event
     * @throws IOException
     */
    @FXML
    void lisaaKuva(ActionEvent event) throws IOException {
        
        FileChooser filechooser = new FileChooser();

        // Lisätään filtterit (jpg ja png)
        ExtensionFilter ex = new ExtensionFilter("JPG, PNG", "*.jpg", "*.png", "*.jpeg");
        filechooser.getExtensionFilters().add(ex);

        // Laitetaan kuvan valinta try-catch lausekkeeseen jolloin jos kuvaa ei valitakkaan niin ei tule erroria
        try {
            File file = filechooser.showOpenDialog(new Stage());

            lisattavanKuva = file.getAbsolutePath();

            Image kuva = new Image(file.toURI().toURL().toExternalForm());
            imgAlbuminKuva.setImage(kuva);

        } catch (Exception e) {
            System.out.println("Kuvaa ei valittu");
        }
    }

    /**
     * Sulkee Albumin lisäys -ikkunan
     */
    @FXML
    void peruutaAlbuminLisays(ActionEvent event) {

        Stage stage = (Stage) btnTallennaUusiAlbumi.getScene().getWindow();
        stage.close();

    }

    
    /** Tallentaa albumin
     * @param event
     * @throws Exception
     */
    @FXML
    void tallennaUusiAlbumi(ActionEvent event) throws Exception {

        lblVirhe1.setVisible(false);
        lblVirhe2.setVisible(false);
        lblVirhe3.setVisible(false);

        boolean virheEan = (!txtfEAN.getText().matches("[0-9]+"));       
        boolean virheJulkaisuvuosi = (!txtfJulkaisuvuosi.getText().matches("[0-9]+")); 
        boolean virheMyynti = (!txtfMyynti.getText().matches("[0-9]+")); 

        if (virheEan) {
            lblVirhe1.setVisible(true);
        }
        
        if (virheJulkaisuvuosi) {
            lblVirhe2.setVisible(true);
        }
        
        if (virheMyynti) {
            lblVirhe3.setVisible(true);
        }

        if (!virheEan && !virheJulkaisuvuosi && !virheMyynti) {
           
            String ean = txtfEAN.getText();
            String nimi = txtfNimi.getText();
            String artisti = txtfArtisti.getText();
            String levy_yhtio = txtfLevy_yhtio.getText();
            int julkaisuvuosi = Integer.parseInt(txtfJulkaisuvuosi.getText());
            int myynti = Integer.parseInt(txtfMyynti.getText());

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

            Database.lisaaAlbumi(con, ean, nimi, artisti, julkaisuvuosi, levy_yhtio, myynti, lisattavanKuva);
        
            Stage thisStage = (Stage) txtfArtisti.getScene().getWindow();
            thisStage.close();
        }

    }

}
