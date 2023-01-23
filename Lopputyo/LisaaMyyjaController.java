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
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LisaaMyyjaController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

         // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
         btnTallennaUusiMyyja.disableProperty().bind(Bindings.isEmpty(txtfSukunimi.textProperty()).
         or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
         or(Bindings.isEmpty(txtfOsoite.textProperty())).or(Bindings.isEmpty(txtfPuh.textProperty()))
         );
        
    }

    @FXML
    private Button btnTallennaUusiMyyja;

    @FXML
    private Pane lisaaMyyjaa;

    @FXML
    private TextField txtfEtunimi;

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

    @FXML
    private Label lblVirheSposti;

    
    /** Sulkee ikkunan
     * @param event
     */
    @FXML
    void peruutaMyyjanLisays(ActionEvent event) {

        Stage thisStage = (Stage) btnTallennaUusiMyyja.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa myyjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaUusiMyyja(ActionEvent event) throws SQLException {

        lblVirhePuh.setVisible(false);
        lblVirheSposti.setVisible(false);

        boolean virhePuh = (!txtfPuh.getText().matches("[0-9]+"));
        boolean virheSposti = false;

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        ArrayList<String> spostiList = Database.haeMyyjienSpostit(con);

        for (String sposti : spostiList) {
            if (sposti.equals(txtfSposti.getText())) {
                virheSposti = true;
            }
        } 

        if (virheSposti) {
            lblVirheSposti.setVisible(true);
        }

        if (virhePuh) {
            lblVirhePuh.setVisible(true);
        }

        if (!virhePuh && !virheSposti) {

            String etunimi = txtfEtunimi.getText();
            String sukunimi = txtfSukunimi.getText();
            String sposti = txtfSposti.getText();
            String puhnum = txtfPuh.getText();
            String osoite = txtfOsoite.getText();

            Database.lisaaMyyja(con, etunimi, sukunimi, osoite, sposti, puhnum);

            Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
            thisStage.close();

            }

    }

}
