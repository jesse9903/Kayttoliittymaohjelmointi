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
import javafx.stage.Stage;

public class LisaaKayttajaController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaUusiKayttaja.disableProperty().bind(Bindings.isEmpty(txtfSukunimi.textProperty()).
        or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
        or(Bindings.isEmpty(txtfKayttajatunnus.textProperty())).or(Bindings.isEmpty(pswfSalasana.textProperty()))
        );

    }

    @FXML
    private Button btnTallennaUusiKayttaja;

    @FXML
    private Pane lisaaKayttaja;

    @FXML
    private TextField txtfEtunimi;

    @FXML
    private TextField txtfKayttajatunnus;

    @FXML
    private TextField txtfSposti;

    @FXML
    private TextField txtfSukunimi;

    @FXML
    private PasswordField pswfSalasana;

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

    

    
    /** Sulkee ikkunan
     * @param event
     */
    @FXML
    void peruutaKayttajanLisays(ActionEvent event) {

        Stage thisStage = (Stage) btnTallennaUusiKayttaja.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa käyttäjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaUusiKayttaja(ActionEvent event) throws SQLException {

        lblVirheSalasana1.setVisible(false);
        lblVirheSalasana2.setVisible(false);
        lblVirheSalasana3.setVisible(false);
        lblVirheKayttajatunnus.setVisible(false);
        lblVirheSposti.setVisible(false);

        boolean virheKayttajatunnus = false;
        boolean virheSposti = false;

        String pattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        boolean virheSalasana = (!pswfSalasana.getText().matches(pattern));

        if (virheSalasana) {
            lblVirheSalasana1.setVisible(true);
            lblVirheSalasana2.setVisible(true);
            lblVirheSalasana3.setVisible(true);
        }

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        ArrayList<String> spostiList = Database.haeKayttajienSpostit(con);
        ArrayList<String> kayttajatunnusList = Database.haeKayttajatunnukset(con);

        for (String sposti : spostiList) {
            if (sposti.equals(txtfSposti.getText())) {
                virheSposti = true;
            }
        }

        for (String kayttajatunnus : kayttajatunnusList) {
            if (kayttajatunnus.equals(txtfKayttajatunnus.getText())) {
                virheKayttajatunnus = true;
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

            Database.lisaaKayttaja(con, kayttajatunnus, salasana, etunimi, sukunimi, sposti);    

            Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
            thisStage.close();

            }
        }

}
