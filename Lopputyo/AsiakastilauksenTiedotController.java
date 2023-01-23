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

public class AsiakastilauksenTiedotController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Bindataan textfieldit tallennusnappiin, jolloin nappi on disabloitu kun joku kentistä on tyhjä
        btnTallennaAsiakastilaus.disableProperty().bind(Bindings.isEmpty(txtfEan.textProperty()).
        or(Bindings.isEmpty(txtfHinta.textProperty())).or(Bindings.isEmpty(txtfAsiakastunnus.textProperty())).
        or(Bindings.isEmpty(txtfMyyjatunnus.textProperty())).or(Bindings.isEmpty(txtfMaara.textProperty())).
        or(Bindings.isEmpty(txtfTilauspvm.textProperty()))
        );
        
    }    
    
    @FXML
    private Pane asiakastilauksenTiedot;

    @FXML
    private Button btnTallennaAsiakastilaus;

    @FXML
    private Pane muokkaaAsiakastilausta;

    @FXML
    private Text txtAsiakastunnus;

    @FXML
    private Text txtEAN;

    @FXML
    private Text txtHinta;

    @FXML
    private Text txtMaara;

    @FXML
    private Text txtMyyjatunnus;

    @FXML
    private Text txtTilauspvm;

    @FXML
    private Text txtTilaustunnus;

    @FXML
    private TextField txtfAsiakastunnus;

    @FXML
    private TextField txtfEan;

    @FXML
    private TextField txtfHinta;

    @FXML
    private TextField txtfMaara;

    @FXML
    private TextField txtfMyyjatunnus;

    @FXML
    private TextField txtfTilauspvm;

    @FXML
    private TextField txtfTilaustunnus;

    @FXML
    private Label lblVirheAsiakastunnus;

    @FXML
    private Label lblVirheAsiakastunnus2;

    @FXML
    private Label lblVirheEan;

    @FXML
    private Label lblVirheEan2;

    @FXML
    private Label lblVirheHinta;

    @FXML
    private Label lblVirheMaara;

    @FXML
    private Label lblVirheMyyjatunnus;

    @FXML
    private Label lblVirheMyyjatunnus2;

    @FXML
    private Label lblVirhePvm;

    Asiakastilaus haettuAsiakastilaus = new Asiakastilaus();

    
    /** Vaihtaa muokkaus-näkymään
     * @param event
     */
    @FXML
    void muokkaaAsiakastilausta(ActionEvent event) {

        asiakastilauksenTiedot.setVisible(false);
        muokkaaAsiakastilausta.setVisible(true);

        databind(haettuAsiakastilaus);

    }

    
    /** Vaihtaa tiedot-näkymään
     * @param event
     */
    @FXML
    void peruutaAsiakastilauksenMuokkaus(ActionEvent event) {

        muokkaaAsiakastilausta.setVisible(false);
        asiakastilauksenTiedot.setVisible(true);

    }

    
    /** Poistaa tilauksen
     * @param event
     * @throws SQLException
     */
    @FXML
    void poistaAsiakastilaus(ActionEvent event) throws SQLException {

        int tilaustunnus = Integer.parseInt(txtTilaustunnus.getText());
        
        Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1"); 

        Database.poistaAsiakastilaus(con, tilaustunnus);

        Stage thisStage = (Stage) txtfEan.getScene().getWindow();
        thisStage.close();

    }

    
    /** Tallentaa tilauksen
     * @param event
     * @throws SQLException
     */
    @FXML
    void tallennaAsiakastilaus(ActionEvent event) throws SQLException {

        lblVirheEan.setVisible(false);
        lblVirheHinta.setVisible(false);
        lblVirheMaara.setVisible(false);
        lblVirheMyyjatunnus.setVisible(false);
        lblVirhePvm.setVisible(false);
        lblVirheEan2.setVisible(false);
        lblVirheMyyjatunnus2.setVisible(false);
        lblVirheAsiakastunnus.setVisible(false);
        lblVirheAsiakastunnus2.setVisible(false);

        boolean virheEan = (!txtfEan.getText().matches("[0-9]+"));
        boolean virheHinta = false;
        boolean virheMaara = (!txtfMaara.getText().matches("[0-9]+"));
        boolean virheMyyjatunnus = (!txtfMyyjatunnus.getText().matches("[0-9]+"));
        boolean virheAsiakastunnus = (!txtfAsiakastunnus.getText().matches("[0-9]+"));
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

        if (virheAsiakastunnus) {
            lblVirheAsiakastunnus.setVisible(true);
        }

        if (virhePvm) {
            lblVirhePvm.setVisible(true);
        }

        if (!virheEan && !virheHinta && !virheMaara && !virheMyyjatunnus && !virhePvm && !virheAsiakastunnus) {

            int ean = Integer.parseInt(txtfEan.getText());
            double hinta = Double.parseDouble(txtfHinta.getText());
            int maara = Integer.parseInt(txtfMaara.getText());
            int myyjatunnus = Integer.parseInt(txtfMyyjatunnus.getText());
            String pvm = txtfTilauspvm.getText();
            int asiakastunnus = Integer.parseInt(txtfAsiakastunnus.getText());
            int tilaustunnus = Integer.parseInt(txtfTilaustunnus.getText());

            Connection con = Database.openConnection("jdbc:mariadb://maria.northeurope.cloudapp.azure.com:3306?user=opiskelija&password=opiskelija1");

            ArrayList<Integer> eanList =  Database.haeEnarit(con);
            ArrayList<Integer> myyjatunnusList =  Database.haeMyyjatunnukset(con);
            ArrayList<Integer> asiakastunnusList =  Database.haeAsiakastunnukset(con);

            boolean eanOlemassa = false;
            boolean myyjatunnusOlemassa = false;
            boolean asiakastunnusOlemassa = false;

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

            for (int i = 0; i < asiakastunnusList.size(); i++) {
                if (asiakastunnus == asiakastunnusList.get(i)) {
                    asiakastunnusOlemassa = true;
                }
            }
            
            if (eanOlemassa && myyjatunnusOlemassa && asiakastunnusOlemassa) {

                Database.muokkaaAsiakastilausta(con, tilaustunnus, myyjatunnus, pvm, asiakastunnus, hinta, ean, maara);

                Stage thisStage = (Stage) txtfEan.getScene().getWindow();
                thisStage.close();
            
            } else if (!eanOlemassa) {
                lblVirheEan2.setVisible(true);
            }

            if (!myyjatunnusOlemassa) {
                lblVirheMyyjatunnus2.setVisible(true);
            }

            if (!asiakastunnusOlemassa) {
                lblVirheAsiakastunnus2.setVisible(true);
            }
        }
    }

    
    /** Bindaa tiedot näkymään
     * @param asiakastilaus Bindattava tilaus
     */
    public void databind(Asiakastilaus asiakastilaus) {

        haettuAsiakastilaus = asiakastilaus;

        if (asiakastilauksenTiedot.isVisible()) {

            txtEAN.setText(String.valueOf(asiakastilaus.getEan()));
            txtHinta.setText(String.valueOf(asiakastilaus.getHinta()));
            txtAsiakastunnus.setText(String.valueOf(asiakastilaus.getAsiakastunnus()));
            txtMaara.setText(String.valueOf(asiakastilaus.getMaara()));
            txtMyyjatunnus.setText(String.valueOf(asiakastilaus.getMyyjatunnus()));
            txtTilauspvm.setText(asiakastilaus.getTilauspvm().toString());
            txtTilaustunnus.setText(String.valueOf(asiakastilaus.getTilaustunnus()));

        } else {

            txtfEan.setText(String.valueOf(asiakastilaus.getEan()));
            txtfHinta.setText(String.valueOf(asiakastilaus.getHinta()));
            txtfAsiakastunnus.setText(String.valueOf(asiakastilaus.getAsiakastunnus()));
            txtfMaara.setText(String.valueOf(asiakastilaus.getMaara()));
            txtfMyyjatunnus.setText(String.valueOf(asiakastilaus.getMyyjatunnus()));
            txtfTilauspvm.setText(asiakastilaus.getTilauspvm().toString());
            txtfTilaustunnus.setText(String.valueOf(asiakastilaus.getTilaustunnus()));

        }
    }

}
