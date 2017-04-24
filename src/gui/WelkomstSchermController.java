package gui;

import com.sun.javaws.jnl.InformationDesc;
import domein.DomeinController;
import excepties.GeenToegangException;
import gui.utils.CloseConfirmation;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class WelkomstSchermController extends Pane {

    @FXML
    private Label lblWelkom;
    @FXML
    private Label lblMaakEenKeuze;
    @FXML
    private Button btnStop;
    @FXML
    private ImageView imgStop;
    private Locale taal;
    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow1;
    @FXML
    private Button btnSpeelSpel;
    @FXML
    private Button btnConfNieuwSpel;
    @FXML
    private Button btnWijzigSpel;
    private DomeinController domcon;
    private ResourceBundel rb;

    public WelkomstSchermController(Locale deTaal, DomeinController domCon) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelkomstScherm.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        rb = new ResourceBundel();
        taal = deTaal;
        domcon = domCon;
        lblWelkom.setText(rb.geefTekst("con_welkom") + " " + domcon.getSpeler().getGebruikersNaam());
    }

    @FXML
    private void btnStopOnAction(ActionEvent event) {
        new CloseConfirmation().show();
    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.geefTekst("con_afmeldenTerugkeren"), ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(rb.geefTekst("con_afmeldenTerug"));
        alert.setTitle("Sokoban");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            domcon.meldAf();
            Stage stage = (Stage) btnArrowBack.getScene().getWindow();
            Scene scene = new Scene(new LoginRegisterkeuzeController(taal));
            stage.setScene(scene);
            stage.setTitle("Sokoban");
            stage.show();
        } else {
            alert.close();
        }
    }

    @FXML
    private void btnSpeelSpelOnAction(ActionEvent event) {
        Stage stage = (Stage) btnStop.getScene().getWindow();
        Scene scene = new Scene(new KiesSpelController(domcon, taal));
        stage.setScene(scene);
        stage.setTitle("Sokoban");
        stage.show();

    }

    @FXML
    private void btnConfNieuwSpelOnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) btnStop.getScene().getWindow();
            Scene scene = new Scene(new ConfigureerSchermController(domcon, taal));
            stage.setScene(scene);
            stage.setTitle("Sokoban");
            stage.show();
        } catch (GeenToegangException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(rb.geefTekst(e.getMessage()));
            alert.show();
        }
    }

    @FXML
    private void btnWijzigSpelOnAction(ActionEvent event) {
        try {
            Stage stage = (Stage) btnStop.getScene().getWindow();
            Scene scene = new Scene(new WijzigEenSpelController(domcon, taal));
            stage.setScene(scene);
            stage.setTitle("Sokoban");
            stage.show();
        } catch (GeenToegangException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(rb.geefTekst(e.getMessage()));
            alert.show();
        }
    }

}
