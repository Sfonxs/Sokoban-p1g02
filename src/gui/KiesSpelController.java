package gui;

import domein.DomeinController;
import excepties.OnbestaandSpelException;
import gui.utils.CloseConfirmation;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class KiesSpelController extends Pane {

    @FXML
    private Label lblKiesEenSpel;
    @FXML
    private Separator sepSeperatorHorizontal;
    @FXML
    private Button btnArrow;
    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow;
    @FXML
    private Button btnStop;
    @FXML
    private ImageView imgStop;
    private ObservableList<String> spelKeuzes = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> cmbSpelKeuze;

    private String[] spelNamen;
    private DomeinController domcon;
    private Locale taal;
    private ResourceBundel rb;

    @FXML
    private Label lblKiesEenSpel1;
    @FXML
    private Separator sepSeperatorHorizontal1;
    @FXML
    private GridPane gridAvatars;
    
    private Avatar selectedAvatar;
    @FXML
    private Pane panePane;

    public KiesSpelController(DomeinController domCon, Locale deTaal) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("KiesSpel.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        domcon = domCon;
        taal = deTaal;
        rb = new ResourceBundel();

        spelNamen = domcon.startSpelSpelen();
        for (int i = 0; i < spelNamen.length; i++) {
            spelKeuzes.add(spelNamen[i]);
        }
        cmbSpelKeuze.setItems(spelKeuzes);

        selectedAvatar = Avatar.Beast;

        Avatar[] values = Avatar.values();
        for (int i = 0; i < values.length; i++) {
            ImageView imageView = new ImageView(new Image(values[i].getResourceUrl()));
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);
            imageView.setStyle("-fx-cursor:hand");
            GridPane.setConstraints(imageView, 0, i, 1, 1, HPos.CENTER, VPos.BOTTOM);
            gridAvatars.add(imageView, i, 0);
            
            final int j = i;
            imageView.setOnMouseClicked((MouseEvent event) -> {
                selectedAvatar = values[j];
                lblKiesEenSpel1.setText(rb.geefTekst("kies_spel_spelerkeuze") + " " + selectedAvatar.name().toLowerCase());
            });
        }
    }

    @FXML
    private void btnArrowOnAction(ActionEvent event) {

        try {
            domcon.speelSpel(cmbSpelKeuze.getValue());
            Stage stage = (Stage) btnArrow.getScene().getWindow();
            Scene scene = new Scene(new SpeelSpelController(domcon, taal, cmbSpelKeuze.getValue(), selectedAvatar));
            stage.setScene(scene);

        } catch (IllegalArgumentException | OnbestaandSpelException ex) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setHeaderText(rb.geefTekst("gui_error"));
            alert1.setTitle(rb.geefTekst("gui_error"));
            alert1.setContentText(ex.getMessage());
            alert1.showAndWait();
        } catch (NullPointerException ex) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText(rb.geefTekst("kies_spel_keuze_fout"));
            alert1.showAndWait();
        }
    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event) {
        Stage stage = (Stage) btnArrow.getScene().getWindow();        
        stage.setTitle("Sokoban");       
        Scene scene = new Scene(new WelkomstSchermController(taal, domcon));       
        stage.setScene(scene);      
        stage.show();
    }
    
    @FXML
    private void btnStopOnAction(ActionEvent event) {
        new CloseConfirmation().show();
    }



}
