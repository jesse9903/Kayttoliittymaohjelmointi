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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MyyjanTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaMyyja.disableProperty().bind(Bindings.isEmpty(txtfSukunimi.textProperty()).
        or(Bindings.isEmpty(txtfSposti.textProperty())).or(Bindings.isEmpty(txtfEtunimi.textProperty())).
        or(Bindings.isEmpty(txtfOsoite.textProperty())).or(Bindings.isEmpty(txtfPuh.textProperty()))
        );
        
    }

    @FXML
    private Pane muokkaaMyyjaa;

    @FXML
    private Pane myyjanTiedot;

    @FXML
    private Button btnTallennaMyyja;

    @FXML
    private Text txtEtunimi;

    @FXML
    private Text txtMyyjatunnus;

    @FXML
    private Text txtOsoite;

    @FXML
    private Text txtPuhelinnumero;

    @FXML
    private Text txtSposti;

    @FXML
    private Text txtSukunimi;

    @FXML
    private TextField txtfEtunimi;

    @FXML
    private TextField txtfMyyjatunnus;

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

    Myyja haettuMyyja = new Myyja();
    String vanhaSposti;

    
    /** Vaihtaa muokkaus-näkymään
     * @param event
     */
    @FXML
    void muokkaaMyyjaa(ActionEvent event) {

        myyjanTiedot.setVisible(false);
        muokkaaMyyjaa.setVisible(true);

        databind(haettuMyyja);

    }

    
    /** Vaihtaa tiedot-näkymään
     * @param event
     */
    @FXML
    void peruutaMyyjanMuokkaus(ActionEvent event) {

        muokkaaMyyjaa.setVisible(false);
        myyjanTiedot.setVisible(true);

    }

    
    /** Poistaa myyjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void poistaMyyja(ActionEvent event) throws SQLException {

        String myyjatunnus = txtMyyjatunnus.getText();
        
        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

        Database.poistaMyyja(con, myyjatunnus);

        Stage thisStage = (Stage) txtEtunimi.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa myyjän
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaMyyja(ActionEvent event) throws SQLException {

        lblVirhePuh.setVisible(false);
        lblVirheSposti.setVisible(false);

        boolean virhePuh = (!txtfPuh.getText().matches("[0-9]+"));
        boolean virheSposti = false;

        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

        ArrayList<String> spostiList = Database.haeMyyjienSpostit(con);

        if (!txtfSposti.getText().equals(vanhaSposti)) {
            for (String sposti : spostiList) {
                if (sposti.equals(txtfSposti.getText())) {
                    virheSposti = true;
                }
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
            Integer myyjatunnus = Integer.parseInt(txtfMyyjatunnus.getText());
            String puh = txtfPuh.getText();
            String osoite = txtfOsoite.getText();

            Database.muokkaaMyyjaa(con, myyjatunnus, etunimi, sukunimi, osoite, sposti, puh);

            Stage thisStage = (Stage) txtfEtunimi.getScene().getWindow();
            thisStage.close();

            }
        }

    
    /** Bindaa tiedot näkymään
     * @param myyja Bindattava myyjä
     */
    public void databind(Myyja myyja) {

        haettuMyyja = myyja;
        vanhaSposti = myyja.getSposti();
    
        if (myyjanTiedot.isVisible()) {
    
            txtMyyjatunnus.setText(String.valueOf(myyja.getMyyjatunnus()));
            txtEtunimi.setText(myyja.getEtunimi());
            txtSukunimi.setText(myyja.getSukunimi());
            txtSposti.setText(myyja.getSposti());
            txtOsoite.setText(myyja.getOsoite());
            txtPuhelinnumero.setText(myyja.getPuhnum());
    
        } else {
    
            txtfMyyjatunnus.setText(String.valueOf(myyja.getMyyjatunnus()));
            txtfEtunimi.setText(myyja.getEtunimi());
            txtfSukunimi.setText(myyja.getSukunimi());
            txtfSposti.setText(myyja.getSposti());
            txtfOsoite.setText(myyja.getOsoite());
            txtfPuh.setText(myyja.getPuhnum());
    
        }
    }

}
