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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LevytilauksenTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
     
        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaLevytilaus.disableProperty().bind(Bindings.isEmpty(txtfEan.textProperty()).
        or(Bindings.isEmpty(txtfHinta.textProperty())).or(Bindings.isEmpty(txtfLevy_yhtio.textProperty())).
        or(Bindings.isEmpty(txtfMyyjatunnus.textProperty())).or(Bindings.isEmpty(txtfMaara.textProperty())).
        or(Bindings.isEmpty(txtfTilauspvm.textProperty()))
        );

    }

    @FXML
    private Button btnTallennaLevytilaus;

    @FXML
    private Pane levytilauksentiedot;

    @FXML
    private Pane muokkaalevytilausta;

    @FXML
    private Text txtEAN;

    @FXML
    private Text txtHinta;

    @FXML
    private Text txtLevy_yhtio;

    @FXML
    private Text txtMaara;

    @FXML
    private Text txtMyyjatunnus;

    @FXML
    private Text txtPvm;

    @FXML
    private Text txtTilaustunnus;

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
    private TextField txtfTilaustunnus;

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

    Levytilaus haettuLevytilaus = new Levytilaus();

    
    /** Vaihtaa muokkaus-näkymään
     * @param event
     */
    @FXML
    void muokkaaLevytilausta(ActionEvent event) {

        levytilauksentiedot.setVisible(false);
        muokkaalevytilausta.setVisible(true);

        databind(haettuLevytilaus);

    }

    
    /** Vaihtaa tiedot-näkymään
     * @param event
     */
    @FXML
    void peruutaLevytilauksenMuokkaus(ActionEvent event) {

        muokkaalevytilausta.setVisible(false);
        levytilauksentiedot.setVisible(true);

    }

    
    /** Poistaa tilauksen
     * @param event
     * @throws SQLException
     */
    @FXML
    void poistaLevytilaus(ActionEvent event) throws SQLException {

        int tilaustunnus = Integer.parseInt(txtTilaustunnus.getText());
        
        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

        Database.poistaLevytilaus(con, tilaustunnus);

        Stage thisStage = (Stage) txtfEan.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa tilauksen
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaLevytilaus(ActionEvent event) throws SQLException {

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
            int tilaustunnus = Integer.parseInt(txtfTilaustunnus.getText());

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

            ArrayList<Integer> eanList =  Database.haeEnarit(con);
            ArrayList<Integer> myyjatunnusList =  Database.haeMyyjatunnukset(con);

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

                Database.muokkaaLevytilausta(con, tilaustunnus, myyjatunnus, pvm, levy_yhtio, hinta, ean, maara);

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

    
    /** Bindaa tiedot näkymään
     * @param levytilaus Bindattava tilaus
     */
    public void databind(Levytilaus levytilaus) {

        haettuLevytilaus = levytilaus;

        if (levytilauksentiedot.isVisible()) {

            txtEAN.setText(String.valueOf(levytilaus.getEan()));
            txtHinta.setText(String.valueOf(levytilaus.getHinta()));
            txtLevy_yhtio.setText(levytilaus.getLevy_yhtio());
            txtMaara.setText(String.valueOf(levytilaus.getMaara()));
            txtMyyjatunnus.setText(String.valueOf(levytilaus.getMyyjatunnus()));
            txtPvm.setText(levytilaus.getTilauspvm().toString());
            txtTilaustunnus.setText(String.valueOf(levytilaus.getTilaustunnus()));

        } else {

            txtfEan.setText(String.valueOf(levytilaus.getEan()));
            txtfHinta.setText(String.valueOf(levytilaus.getHinta()));
            txtfLevy_yhtio.setText(levytilaus.getLevy_yhtio());
            txtfMaara.setText(String.valueOf(levytilaus.getMaara()));
            txtfMyyjatunnus.setText(String.valueOf(levytilaus.getMyyjatunnus()));
            txtfTilauspvm.setText(levytilaus.getTilauspvm().toString());
            txtfTilaustunnus.setText(String.valueOf(levytilaus.getTilaustunnus()));

        }
    }
    
}
