package gui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class TaalkeuzeController extends Pane {

    @FXML
    private ComboBox<String> cmbTaal;
    @FXML
    private ObservableList<String> talen = FXCollections.observableArrayList();
    @FXML
    private Button btnArrow;
    @FXML
    private ImageView imgArrow;
    @FXML
    private Button btnStop;
    private ResourceBundel rb;

    public TaalkeuzeController(Locale deTaal) {

        talen.add("Nederlands");
        talen.add("Français");
        talen.add("English");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Taalkeuze.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        cmbTaal.setItems(talen);
        rb = new ResourceBundel();
    }

    public ComboBox<String> getCmbTaal() {
        return cmbTaal;
    }

    @FXML
    private void btnStopOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wenst u het spel te verlaten?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Sokoban stoppen");
        alert.setTitle("Stop");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        } else {
            alert.close();
        }
    }

    @FXML
    private void cmbTaalOnAction(ActionEvent event) {

    }

    @FXML
    private void btnArrowOnAction(ActionEvent event) {
        Stage stage = (Stage) btnArrow.getScene().getWindow();
        stage.setTitle("Sokoban");
        try {
            if (cmbTaal.getValue().equalsIgnoreCase("Nederlands")) {
            ResourceBundel.registreerTaal("nl");
            Scene scene = new Scene(new LoginRegisterkeuzeController(new Locale("nl", "NL")));
            stage.setScene(scene);
            stage.show();
        } else if (cmbTaal.getValue().equalsIgnoreCase("Français")) {
            ResourceBundel.registreerTaal("FR");
            Scene scene = new Scene(new LoginRegisterkeuzeController(new Locale("fr", "FR")));
            stage.setScene(scene);
            stage.show();
        } else if (cmbTaal.getValue().equalsIgnoreCase("English")) {
            ResourceBundel.registreerTaal("EN");
            Scene scene = new Scene(new LoginRegisterkeuzeController(new Locale("en", "EN")));
            stage.setScene(scene);
            stage.show();
        } 
        } catch (NullPointerException e) {
             Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Gelieve een taal te kiezen.");
            alert.setHeaderText("Taalkeuze fout");
            alert.setTitle("Fout");
            alert.show();
        }
        
            }

}
