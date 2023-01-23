import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
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

public class LisaaLevytilausController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaUusiLevytilaus.disableProperty().bind(Bindings.isEmpty(txtfEan.textProperty()).
        or(Bindings.isEmpty(txtfHinta.textProperty())).or(Bindings.isEmpty(txtfLevy_yhtio.textProperty())).
        or(Bindings.isEmpty(txtfMyyjatunnus.textProperty())).or(Bindings.isEmpty(txtfMaara.textProperty())).
        or(Bindings.isEmpty(txtfTilauspvm.textProperty()))
        );

    }

    @FXML
    private Pane lisaaLevytilaus;

    @FXML
    private TextField txtfEan;

    @FXML
    private TextField txtfHinta;

    @FXML
    private TextField txtfLevy_yhtio;

    @FXML
    private TextField txtfMaara;

    @FXML
    private TextField txtfMyyjatunnus;

    @FXML
    private TextField txtfTilauspvm;

    @FXML
    private Button btnTallennaUusiLevytilaus;

    @FXML
    private Label lblVirheEan;

    @FXML
    private Label lblVirheHinta;

    @FXML
    private Label lblVirheMaara;

    @FXML
    private Label lblVirheMyyjatunnus;

    @FXML
    private Label lblVirhePvm;

    @FXML
    private Label lblVirheEan2;

    @FXML
    private Label lblVirheMyyjatunnus2;
    
    
    /** Sulkee ikkunan
     * @param event
     */
    @FXML
    void peruutaLevytilauksenLisays(ActionEvent event) {

        Stage thisStage = (Stage) txtfEan.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa tilauksen
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaUusiLevytilaus(ActionEvent event) throws SQLException {

        lblVirheEan.setVisible(false);
        lblVirheHinta.setVisible(false);
        lblVirheMaara.setVisible(false);
        lblVirheMyyjatunnus.setVisible(false);
        lblVirhePvm.setVisible(false);
        lblVirheEan2.setVisible(false);
        lblVirheMyyjatunnus2.setVisible(false);

        boolean virheEan = (!txtfEan.getText().matches("[0-9]+"));
        boolean virheHinta = false;
        boolean virheMaara = (!txtfMaara.getText().matches("[0-9]+"));
        boolean virheMyyjatunnus = (!txtfMyyjatunnus.getText().matches("[0-9]+"));
        boolean virhePvm = false;

        LocalDate ld = null;

        try {

            ld = LocalDate.parse(txtfTilauspvm.getText());

            virhePvm = false;

        } catch (Exception e) {
            virhePvm = true;   
        }

        try {
            Double.parseDouble(txtfHinta.getText());

        } catch (Exception e) {
            virheHinta = true;
        }

        if (virheEan) {
            lblVirheEan.setVisible(true);
        }

        if (virheHinta) {
                lblVirheHinta.setVisible(true);
        }

        if (virheMaara) {
                lblVirheMaara.setVisible(true);
        }

        if (virheMyyjatunnus) {
            lblVirheMyyjatunnus.setVisible(true);
        }

        if (virhePvm) {
            lblVirhePvm.setVisible(true);
        }

        if (!virheEan && !virheHinta && !virheMaara && !virheMyyjatunnus && !virhePvm) {

            int ean = Integer.parseInt(txtfEan.getText());
            double hinta = Double.parseDouble(txtfHinta.getText());
            int maara = Integer.parseInt(txtfMaara.getText());
            int myyjatunnus = Integer.parseInt(txtfMyyjatunnus.getText());
            String pvm = txtfTilauspvm.getText();
            String levy_yhtio = txtfLevy_yhtio.getText();

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

            ArrayList<Integer> eanList =  Database.haeEnarit(con);
            ArrayList<Integer> myyjatunnusList = Database.haeMyyjatunnukset(con);

            boolean eanOlemassa = false;
            boolean myyjatunnusOlemassa = false;

            for (int i = 0; i < eanList.size(); i++) {
                if (ean == eanList.get(i)) {
                    eanOlemassa = true;
                }
            }
            
            for (int i = 0; i < myyjatunnusList.size(); i++) {
                if (myyjatunnus == myyjatunnusList.get(i)) {
                    myyjatunnusOlemassa = true;
                }
            }
            
            if (eanOlemassa && myyjatunnusOlemassa) {
            Database.LisaaLevytilaus(con, myyjatunnus, levy_yhtio, pvm, hinta, ean, maara);

            Stage thisStage = (Stage) txtfEan.getScene().getWindow();
            thisStage.close();

            } else if (!eanOlemassa) {
                lblVirheEan2.setVisible(true);
            }

            if (!myyjatunnusOlemassa) {
                lblVirheMyyjatunnus2.setVisible(true);
            }
        }
 
    }

}
