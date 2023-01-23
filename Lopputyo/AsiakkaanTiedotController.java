import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AsiakkaanTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaAsiakas.disableProperty().bind(Bindings.isEmpty(txtfAsiakastunnus.textProperty()).
        or(Bindings.isEmpty(txtfKayttajatunnus.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
        or(Bindings.isEmpty(txtfOsoite.textProperty())).or(Bindings.isEmpty(txtfPuh.textProperty())).
        or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfSukunimi.textProperty()))
        );
        
    }
    
    @FXML
    private Pane asiakkaanTiedot;

    @FXML
    private Button btnTallennaAsiakas;

    @FXML
    private Pane muokkaaAsiakasta;

    @FXML
    private Text txtAsiakastunnus;

    @FXML
    private Text txtEtunimi;

    @FXML
    private Text txtKayttajatunnus;

    @FXML
    private Text txtOsoite;

    @FXML
    private Text txtPuhelinnumero;

    @FXML
    private Text txtSposti;

    @FXML
    private Text txtSukunimi;

    @FXML
    private TextField txtfAsiakastunnus;

    @FXML
    private TextField txtfEtunimi;

    @FXML
    private TextField txtfKayttajatunnus;

    @FXML
    private TextField txtfOsoite;

    @FXML
    private TextField txtfPuh;

    @FXML
    private TextField txtfSposti;

    @FXML
    private TextField txtfSukunimi;

    @FXML
    private Label lblVirhePuh;

    Asiakas haettuAsiakas = new Asiakas();

    
    /** Vaihtaa muokkaus-näkymään 
     * @param event
     */
    @FXML
    void muokkaaAsiakasta(ActionEvent event) {

        asiakkaanTiedot.setVisible(false);
        muokkaaAsiakasta.setVisible(true);

        databind(haettuAsiakas);

    }

    
    /** Vaihtaa tiedot-näkymään
     * @param event
     */
    @FXML
    void peruutaAsiakkaanMuokkaus(ActionEvent event) {

        muokkaaAsiakasta.setVisible(false);
        asiakkaanTiedot.setVisible(true);

    }

    
    /** Poistaa asiakkaan
     * @param event
     * @throws SQLException
     */
    @FXML
    void poistaAsiakas(ActionEvent event) throws SQLException {

        int asiakastunnus = Integer.parseInt(txtAsiakastunnus.getText());
        
        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

        Database.poistaAsiakas(con, asiakastunnus);

        Stage thisStage = (Stage) txtfAsiakastunnus.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa asiakkaan
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaAsiakas(ActionEvent event) throws SQLException {

        lblVirhePuh.setVisible(false);

        boolean virhePuh = (!txtfPuh.getText().matches("[0-9]+"));

        if (virhePuh) {
            lblVirhePuh.setVisible(true);
        }

        if (!virhePuh) {

            int asiakastunnus = Integer.parseInt(txtfAsiakastunnus.getText());
            String etunimi = txtfEtunimi.getText();
            String sukunimi = txtfSukunimi.getText();
            String osoite = txtfOsoite.getText();
            String sposti = txtfSposti.getText();
            String puh = txtfPuh.getText();

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

            Database.muokkaaAsiakasta(con, asiakastunnus, etunimi, sukunimi, osoite, sposti, puh);

            Stage thisStage = (Stage) txtfAsiakastunnus.getScene().getWindow();
            thisStage.close();

        }

    }

    
    /** Bindaa tiedot päänäkymään
     * @param asiakas Bindattava asiakas
     */
    public void databind(Asiakas asiakas) {

        haettuAsiakas = asiakas;

        if (asiakkaanTiedot.isVisible()) {

            txtAsiakastunnus.setText(String.valueOf(asiakas.getAsiakastunnus()));
            txtEtunimi.setText(String.valueOf(asiakas.getEtunimi()));
            txtSukunimi.setText(asiakas.getSukunimi());
            txtKayttajatunnus.setText(String.valueOf(asiakas.getKayttajatunnus()));
            txtOsoite.setText(String.valueOf(asiakas.getOsoite()));
            txtPuhelinnumero.setText(asiakas.getPuhnum());
            txtSposti.setText(String.valueOf(asiakas.getSposti()));

            if (asiakas.getKayttajatunnus() == null) {
                txtKayttajatunnus.setText("-");
            }

            if (asiakas.getPuhnum() == null) {
                txtPuhelinnumero.setText("-");
            }

            if (asiakas.getOsoite() == null) {
                txtOsoite.setText("-");
            }

        } else {

            txtfAsiakastunnus.setText(String.valueOf(asiakas.getAsiakastunnus()));
            txtfEtunimi.setText(String.valueOf(asiakas.getEtunimi()));
            txtfSukunimi.setText(asiakas.getSukunimi());
            txtfKayttajatunnus.setText(String.valueOf(asiakas.getKayttajatunnus()));
            txtfOsoite.setText(String.valueOf(asiakas.getOsoite()));
            txtfPuh.setText(asiakas.getPuhnum());
            txtfSposti.setText(String.valueOf(asiakas.getSposti()));

            if (asiakas.getKayttajatunnus() == null) {
                txtfKayttajatunnus.setText("-");
            }

            if (asiakas.getPuhnum() == null) {
                txtfPuh.setText("-");
            }

            if (asiakas.getOsoite() == null) {
                txtfOsoite.setText("-");
            }
        }
    }

}