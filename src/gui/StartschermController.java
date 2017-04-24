package gui;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class StartschermController extends Pane {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Button btnArrow;
    @FXML
    private ImageView imgArrow;
    @FXML
    private Button btnStop;
    @FXML
    private ImageView imgStop;
    private ResourceBundel rb;

    public StartschermController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Startscherm.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", new Locale("en", "EN")));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        rb = new ResourceBundel();
    }

    @FXML
    private void btnArrowOnAction(ActionEvent event) {
        Stage stage = (Stage) btnArrow.getScene().getWindow();
        stage.setTitle("Sokoban");
        Scene scene = new Scene(new TaalkeuzeController(new Locale("nl", "NL")));
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void btnStopOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Wenst u het spel te verlaten?", ButtonType.YES, ButtonType.NO);
        alert.setContentText("Wenst u het spel te verlaten?");
        alert.setHeaderText("Sokoban stoppen");
        alert.setTitle("Stop");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        } else {
            alert.close();
        }
    }
}
