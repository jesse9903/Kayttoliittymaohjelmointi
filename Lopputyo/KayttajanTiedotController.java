import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class KayttajanTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaKayttaja.disableProperty().bind(Bindings.isEmpty(txtfSukunimi.textProperty()).
        or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
        or(Bindings.isEmpty(txtfKayttajatunnus.textProperty()))
        );
        
    }

    @FXML
    private Pane kayttajanTiedot;

    @FXML
    private Pane muokkaaKayttajaa;

    @FXML
    private Button btnTallennaKayttaja;

    @FXML
    private Text txtKayttajatunnus;

    @FXML
    private Text txtEtunimi;

    @FXML
    private Text txtSposti;

    @FXML
    private Text txtSukunimi;

    @FXML
    private TextField txtfKayttajatunnus;

    @FXML
    private TextField txtfEtunimi;

    @FXML
    private TextField txtfSposti;

    @FXML
    private TextField txtfPuh;

    @FXML
    private TextField txtfSukunimi;

    @FXML
    private Label lblVirheKayttajatunnus;

    @FXML
    private Label lblVirheSposti;

    @FXML
    private Label lblVirheSalasana1;

    @FXML
    private Label lblVirheSalasana2;

    @FXML
    private Label lblVirheSalasana3;

    @FXML
    private PasswordField pswfSalasana;

    Kayttaja haettuKayttaja = new Kayttaja();

    String vanhaKayttajatunnus;
    String vanhaSposti;

    
    /** Vaihtaa muokkaus-näkymään
     * @param event
     */
    @FXML
    void muokkaaKayttajaa(ActionEvent event) {

        kayttajanTiedot.setVisible(false);
        muokkaaKayttajaa.setVisible(true);

        databind(haettuKayttaja);

    }

    
    /** Vaihtaa tiedot-näkymään
     * @param event
     */
    @FXML
    void peruutaKayttajanMuokkaus(ActionEvent event) {

        muokkaaKayttajaa.setVisible(false);
        kayttajanTiedot.setVisible(true);
        
    }

    
    /** Poistaa käyttäjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void poistaKayttaja(ActionEvent event) throws SQLException {

        String kayttajatunnus = txtKayttajatunnus.getText();
        
        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

        Database.poistaKayttaja(con, kayttajatunnus);

        Stage thisStage = (Stage) txtEtunimi.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa käyttäjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaKayttaja(ActionEvent event) throws SQLException {

        lblVirheSalasana1.setVisible(false);
        lblVirheSalasana2.setVisible(false);
        lblVirheSalasana3.setVisible(false);
        lblVirheKayttajatunnus.setVisible(false);
        lblVirheSposti.setVisible(false);

        boolean virheKayttajatunnus = false;
        boolean virheSposti = false;

        String pattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        boolean virheSalasana = false;

        if (!pswfSalasana.getText().isEmpty()) {
            virheSalasana = (!pswfSalasana.getText().matches(pattern));
        }

        if (virheSalasana) {
            lblVirheSalasana1.setVisible(true);
            lblVirheSalasana2.setVisible(true);
            lblVirheSalasana3.setVisible(true);
        }

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        ArrayList<String> spostiList = Database.haeKayttajienSpostit(con);
        ArrayList<String> kayttajatunnusList = Database.haeKayttajatunnukset(con);

        if (!txtfSposti.getText().equals(vanhaSposti)) {
            for (String sposti : spostiList) {
                if (sposti.equals(txtfSposti.getText())) {
                    virheSposti = true;
                }
            } 
        }

        if (!txtfKayttajatunnus.getText().equals(vanhaKayttajatunnus)) {
            for (String kayttajatunnus : kayttajatunnusList) {
                if (kayttajatunnus.equals(txtfKayttajatunnus.getText())) {
                    virheKayttajatunnus = true;
                }
            }
        }

        if (virheKayttajatunnus) {
            lblVirheKayttajatunnus.setVisible(true);
        }

        if (virheSposti) {
            lblVirheSposti.setVisible(true);
        }

        if (!virheSalasana && !virheKayttajatunnus && !virheSposti) {

            String etunimi = txtfEtunimi.getText();
            String sukunimi = txtfSukunimi.getText();
            String sposti = txtfSposti.getText();
            String kayttajatunnus = txtfKayttajatunnus.getText();
            String salasana = pswfSalasana.getText();

            if (!salasana.isEmpty()) {
                Database.muokkaaKayttajaa(con, kayttajatunnus, etunimi, sukunimi, sposti, salasana, vanhaKayttajatunnus);

            } else {
                Database.muokkaaKayttajaa(con, kayttajatunnus, etunimi, sukunimi, sposti, vanhaKayttajatunnus);
            }

            Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
            thisStage.close();

            }
        }

    
    /** Bindaa tiedot päänäkymään
     * @param kayttaja Bindattava käyttäjä
     */
    public void databind(Kayttaja kayttaja) {

        haettuKayttaja = kayttaja;
        vanhaKayttajatunnus = kayttaja.getKayttajatunnus();
        vanhaSposti = kayttaja.getSposti();

        if (kayttajanTiedot.isVisible()) {

            txtKayttajatunnus.setText(kayttaja.getKayttajatunnus());
            txtEtunimi.setText(kayttaja.getEtunimi());
            txtSukunimi.setText(kayttaja.getSukunimi());
            txtSposti.setText(kayttaja.getSposti());

        } else {

            txtfKayttajatunnus.setText(kayttaja.getKayttajatunnus());
            txtfEtunimi.setText(kayttaja.getEtunimi());
            txtfSukunimi.setText(kayttaja.getSukunimi());
            txtfSposti.setText(kayttaja.getSposti());

        }
    }

}