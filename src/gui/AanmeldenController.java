package gui;

import gui.utils.CloseConfirmation;
import domein.DomeinController;
import domein.Speler;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class AanmeldenController extends Pane {
    
    @FXML
    private PasswordField txfWachtwoord;
    @FXML
    private TextField txfGebruikersnaam;
    @FXML
    private Button btnArrow;
    
    private DomeinController domCon;
    private String gebruikersNaam;
    private String wachtwoord;
    private Speler speler;
    @FXML
    private ImageView imgArrow;
    private Locale taal;
    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow1;
    private ResourceBundel rb;
    
    public AanmeldenController(Locale deTaal) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Aanmelden.fxml"));
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
    public AanmeldenController(Locale deTaal, DomeinController domcon) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Aanmelden.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        taal = deTaal;
        domCon = domcon;
        
    }
    
    @FXML
    private void btnArrowOnAction(ActionEvent event) {
        
        domCon = new DomeinController();
        if (txfGebruikersnaam.getText().equals("") || txfWachtwoord.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(rb.geefTekst("aanmelden_alert1_fout"));
            alert.setHeaderText(rb.geefTekst("gui_opnieuw"));
            alert.setTitle(rb.geefTekst("aanmelden_alert1_titel"));
            alert.show();
        } else {
            setGebruikersNaam(txfGebruikersnaam.getText());
            setWachtwoord(txfWachtwoord.getText());
            domCon.meldAan(gebruikersNaam, wachtwoord);
            setSpeler(domCon.getSpeler());
                     
            if (speler == null) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR);
                alert2.setContentText(rb.geefTekst("aanmelden_alert2_fout"));
                alert2.setHeaderText(rb.geefTekst("gui_opnieuw"));
                alert2.setTitle(rb.geefTekst("gui_error"));
                alert2.show();
            } else {
                
                Stage stage = (Stage) btnArrow.getScene().getWindow();                
                Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
                stage.setScene(scene);                
                stage.setTitle("Sokoban");                
                stage.show();
            }
        }
    }
    
    public void setGebruikersNaam(String gebruikersNaam) {
        this.gebruikersNaam = gebruikersNaam;
    }
    
    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }
    
    public void setSpeler(Speler speler) {
        this.speler = speler;
    }
    
    private void btnStopOnAction(ActionEvent event) {
        new CloseConfirmation().show();
    }
    
    @FXML
    private void btnArrowBackOnAction(ActionEvent event) {
        Stage stage = (Stage) btnArrow.getScene().getWindow();        
        Scene scene = new Scene(new LoginRegisterkeuzeController(taal));
        stage.setScene(scene);        
        stage.setTitle("Sokoban");
        stage.show();
    }

    @FXML
    private void txfWachtwoordOnKeyPressed(KeyEvent event) {
        if(event.getCode().getName().equalsIgnoreCase("ENTER"))
        {
            btnArrowOnAction(null);
        }
    }
    
}
