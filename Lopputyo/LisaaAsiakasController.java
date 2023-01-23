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

// VALMIS

public class LisaaAsiakasController implements Initializable {

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
    private PasswordField pswfSalasana;

    @FXML
    private Button btnTallennaUusiAsiakas;

    @FXML
    private Pane lisaaAsiakas;

    @FXML
    private Label lblVirhePuh;

    @FXML
    private Label lblVirheSal;

    @FXML
    private Label lblVirheSal1;

    @FXML
    private Label lblVirheSal2;

    @FXML
    private Label lblVirheSal3;

    @FXML
    private Label lblVirheSal4;

    @FXML
    private Label lblVirheKayttajatunnus;

    @FXML
    private Label lblVirheSposti;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaUusiAsiakas.disableProperty().bind(Bindings.isEmpty(txtfSukunimi.textProperty()).
        or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
        or(Bindings.isEmpty(txtfOsoite.textProperty())).or(Bindings.isEmpty(txtfPuh.textProperty()))
        );

    }

    
    /** Sulkee ikkunan
     * @param event
     */
    @FXML
    void peruutaAsiakkaanLisays(ActionEvent event) {

         Stage thisStage = (Stage) btnTallennaUusiAsiakas.getScene().getWindow();
         thisStage.close();

    }


    
    /** Tallentaa asiakkaan
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaUusiAsiakas(ActionEvent event) throws SQLException {

        lblVirheSal.setVisible(false);
        lblVirheSal1.setVisible(false);
        lblVirheSal2.setVisible(false);
        lblVirhePuh.setVisible(false);
        lblVirheSal3.setVisible(false);
        lblVirheSal4.setVisible(false);
        lblVirheKayttajatunnus.setVisible(false);
        lblVirheSposti.setVisible(false);

        boolean virheKayttajatunnus = false;
        boolean virheSposti = false;

        boolean virhePuh = (!txtfPuh.getText().matches("[0-9]+"));

        String pattern = "^(?=.*[0-9])(?=.*[A-Z]).{8,}$";
        boolean virheSalasana = false;
        
        if (!pswfSalasana.getText().isEmpty()) {
            virheSalasana = (!pswfSalasana.getText().matches(pattern));
        }

        if (virhePuh) {
            lblVirhePuh.setVisible(true);
        }

        if (virheSalasana) {
            lblVirheSal.setVisible(true);
            lblVirheSal1.setVisible(true);
            lblVirheSal2.setVisible(true);
        }

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        ArrayList<String> spostiList = Database.haeAsiakkaidenSpostit(con);
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

        if (!virhePuh && !virheSalasana && !virheKayttajatunnus && !virheSposti) {

            String etunimi = txtfEtunimi.getText();
            String sukunimi = txtfSukunimi.getText();
            String osoite = txtfOsoite.getText();
            String puhnum = txtfPuh.getText();
            String sposti = txtfSposti.getText();
            String kayttajatunnus = txtfKayttajatunnus.getText();
            String salasana = pswfSalasana.getText();

            if (kayttajatunnus.isEmpty()) {

            Database.lisaaAsiakas(con, etunimi, sukunimi, osoite, puhnum, sposti);

            Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
            thisStage.close();

            } else if (salasana.isEmpty()) {
                lblVirheSal3.setVisible(true);
                lblVirheSal4.setVisible(true);
            
            } else {

                Database.lisaaAsiakas(con, etunimi, sukunimi, osoite, puhnum, sposti, kayttajatunnus, salasana);

                Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
                thisStage.close();
            }
        }
    }

}
