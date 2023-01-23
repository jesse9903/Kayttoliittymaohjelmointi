import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class AlbuminTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaAlbumi.disableProperty().bind(Bindings.isEmpty(txtfArtisti.textProperty()).
        or(Bindings.isEmpty(txtfEAN.textProperty())).or(Bindings.isEmpty(txtfJulkaisuvuosi.textProperty())).
        or(Bindings.isEmpty(txtfLevy_yhtio.textProperty())).or(Bindings.isEmpty(txtfMyynti.textProperty())).
        or(Bindings.isEmpty(txtfNimi.textProperty()))
        );

    }

    @FXML
    private Button btnTallennaAlbumi;

    @FXML
    private Pane albuminTiedot;

    @FXML
    private ImageView imgAlbuminKuva;

    @FXML
    private ImageView imgAlbuminKuvaMuokk;

    @FXML
    private Pane muokkaaAlbumia;

    @FXML
    private Text txtAlbuminEan;

    @FXML
    private Text txtAlbuminNimi;

    @FXML
    private Text txtArtisti;

    @FXML
    private Text txtJulkaisuvuosi;

    @FXML
    private Text txtLevy_yhtio;

    @FXML
    private Text txtMyynti;

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
    private VBox vboxAlbuminTiedot;

    @FXML
    private Label lblVirheEan;

    @FXML
    private Label lblVirheJulkaisuvuosi;

    @FXML
    private Label lblVirheMyynti;

    String muokattavanKuva;

    Albumi haettuAlbumi = new Albumi();

    @FXML
    void muokkaaAlbumia(ActionEvent event) {

        albuminTiedot.setVisible(false);
        muokkaaAlbumia.setVisible(true);

        dataBind(haettuAlbumi);

    }

    @FXML
    void peruutaAlbuminMuokkaus(ActionEvent event) {

        muokkaaAlbumia.setVisible(false);
        albuminTiedot.setVisible(true);

    }

    @FXML
    void poistaAlbumi(ActionEvent event) throws Exception {

        String nimi = txtAlbuminNimi.getText();
        String artistinNimi = txtArtisti.getText();
        int ean = Integer.parseInt(txtAlbuminEan.getText());

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        Database.poistaAlbumi(con, ean, nimi, artistinNimi);

        Stage thisStage = (Stage) txtAlbuminEan.getScene().getWindow();
        thisStage.close();

    }

    @FXML
    void tallennaAlbumi(ActionEvent event) throws Exception {

        lblVirheEan.setVisible(false);
        lblVirheJulkaisuvuosi.setVisible(false);
        lblVirheMyynti.setVisible(false);

        boolean virheEan = (!txtfEAN.getText().matches("[0-9]+"));
        boolean virheJulkaisuvuosi = (!txtfJulkaisuvuosi.getText().matches("[0-9]+"));
        boolean virheMyynti = (!txtfMyynti.getText().matches("[0-9]+"));

        if (virheEan) {
            lblVirheEan.setVisible(true);
        }

        if (virheJulkaisuvuosi) {
                lblVirheJulkaisuvuosi.setVisible(true);
        }

        if (virheMyynti) {
                lblVirheMyynti.setVisible(true);
        }

        if (!virheEan && !virheJulkaisuvuosi && !virheMyynti) {

            int ean = Integer.parseInt(txtfEAN.getText());
            String nimi = txtfNimi.getText();
            String artisti = txtfArtisti.getText();
            String levy_yhtio = txtfLevy_yhtio.getText();
            int julkaisuvuosi = Integer.parseInt(txtfMyynti.getText());
            int myynti = Integer.parseInt(txtfMyynti.getText());

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");
            Database.muokkaaAlbumia(con, ean, nimi, artisti, levy_yhtio, julkaisuvuosi, myynti, muokattavanKuva);

            Stage thisStage = (Stage) txtfArtisti.getScene().getWindow();
            thisStage.close();
        }

    }

    @FXML
    void vaihdaKuva(ActionEvent event) {

        FileChooser filechooser = new FileChooser();

        // Lisätään filtterit (jpg ja png)
        ExtensionFilter ex = new ExtensionFilter("JPG, PNG", "*.jpg", "*.png", "*.jpeg");
        filechooser.getExtensionFilters().add(ex);

        // Laitetaan kuvan valinta try-catch lausekkeeseen jolloin jos kuvaa ei valitakkaan niin ei tule erroria
        try {
            File file = filechooser.showOpenDialog(new Stage());

            muokattavanKuva = file.getAbsolutePath();

            Image kuva = new Image(file.toURI().toURL().toExternalForm());
            imgAlbuminKuvaMuokk.setImage(kuva);

        } catch (Exception e) {
            System.out.println("Kuvaa ei valittu");
        }

    }

    public void dataBind(Albumi albumi) {

        haettuAlbumi = albumi;
        muokattavanKuva = haettuAlbumi.getKuva();

        if (albuminTiedot.isVisible()) {

        try {

            if (albumi.getKuva() != null) {

                File kuvaPath = new File(albumi.getKuva());
                Image kuva = new Image(kuvaPath.toURI().toURL().toExternalForm());
                imgAlbuminKuva.setImage(kuva);
            
            } else {
                imgAlbuminKuva.setImage(new Image("kuvat/kuvaa_ei_loydy.jpg"));
            }

            txtAlbuminEan.setText(String.valueOf(albumi.getEan()));
            txtAlbuminNimi.setText(albumi.getNimi());
            txtArtisti.setText(albumi.getArtisti());
            txtJulkaisuvuosi.setText(String.valueOf(albumi.getJulkaisuvuosi()));
            txtLevy_yhtio.setText(albumi.getLevy_yhtio());
            txtMyynti.setText(String.valueOf(albumi.getMyynti()));

        } catch (Exception e) {
            System.out.println("Virhe haettaessa tietoja Albumin tiedot -ikkunaan");
        }

    } else {

        try {

            if (albumi.getKuva() != null) {

                File kuvaPath = new File(albumi.getKuva());
                Image kuva = new Image(kuvaPath.toURI().toURL().toExternalForm());
                imgAlbuminKuvaMuokk.setImage(kuva);
            
            } else {
                imgAlbuminKuvaMuokk.setImage(new Image("kuvat/kuvaa_ei_loydy.jpg"));
            }

            txtfEAN.setText(String.valueOf(albumi.getEan()));
            txtfNimi.setText(albumi.getNimi());
            txtfArtisti.setText(albumi.getArtisti());
            txtfJulkaisuvuosi.setText(String.valueOf(albumi.getJulkaisuvuosi()));
            txtfLevy_yhtio.setText(albumi.getLevy_yhtio());
            txtfMyynti.setText(String.valueOf(albumi.getMyynti()));

        } catch (Exception e) {
            System.out.println("Virhe haettaessa tietoja Albumin muokkaus -ikkunaan");
        }
    }
    }

}