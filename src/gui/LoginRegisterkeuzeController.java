package gui;

import gui.utils.CloseConfirmation;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class LoginRegisterkeuzeController extends Pane {

    @FXML
    private Button btnStop;
    @FXML
    private Button btnAanmelden;
    @FXML
    private Button btnRegistreren;
    private Locale taal;
    @FXML
    private ImageView imgStop;
    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow1;
    @FXML
    private VBox vbLRKeuze;
    private ResourceBundel rb;
    private AanmeldenController ac;
    
    private RegistrerenController rc;
    @FXML
    private ImageView imgLogo;
   
    

    public LoginRegisterkeuzeController(Locale deTaal) {

        ac = new AanmeldenController(deTaal);
        rc = new RegistrerenController(deTaal);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginRegisterkeuze.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        taal = deTaal;
        rb = new ResourceBundel();  
    }

    @FXML
    private void btnAanmeldenOnAction(ActionEvent event) {
        vbLRKeuze.getChildren().add(ac);
        btnAanmelden.setDisable(true);
        btnRegistreren.setDisable(true);
    }

    @FXML
    private void btnRegistrerenOnAction(ActionEvent event) {
        vbLRKeuze.getChildren().add(rc);
        btnAanmelden.setDisable(true);
        btnRegistreren.setDisable(true);
        
    }

    @FXML
    private void btnStopOnAction(ActionEvent event) {
        new CloseConfirmation().show();

    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event) {
        Stage stage = (Stage) btnAanmelden.getScene().getWindow();        
        stage.setTitle("Sokoban");       
        Scene scene = new Scene(new TaalkeuzeController(taal));       
        stage.setScene(scene);      
        stage.show();
    }

}
