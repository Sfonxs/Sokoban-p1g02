package gui;

import domein.DomeinController;
import excepties.GebruikersnaamAlGebruiktException;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class RegistrerenController extends Pane
{

    @FXML
    private TextField txfNaam;
    @FXML
    private TextField txfVoornaam;
    @FXML
    private TextField txfGebruikersnaam;
    @FXML
    private Button btnArrow;
    @FXML
    private ImageView imgArrow;
    @FXML
    private PasswordField txfWachtwoord;
    private DomeinController domCon;
    private Locale taal;
    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow1;
    private ResourceBundel rb;
    @FXML
    private ProgressIndicator piProgressCircleUsername;
    @FXML
    private ProgressIndicator piProgressCirclePass;
    KeyEvent event1;
    @FXML
    private Label lblUsernameRules;
    @FXML
    private Label lblPassRules1;
    @FXML
    private Label lblPassRules2;
    @FXML
    private Label lblPassRules3;
    @FXML
    private Label lblPassRules4;

    public RegistrerenController(Locale deTaal)
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Registreren.fxml"));
        loader.setResources(ResourceBundle.getBundle("resources/Messages", deTaal));
        loader.setRoot(this);
        loader.setController(this);
        try
        {
            loader.load();
        } catch (IOException ex)
        {
            throw new RuntimeException(ex);
        }
        domCon = new DomeinController();
        taal = deTaal;
        rb = new ResourceBundel();

    }

    private void btnStopOnAction(ActionEvent event)
    {
        new CloseConfirmation().show();
    }

    @FXML
    private void btnArrowOnAction(ActionEvent event)
    {

        try
        {
            domCon.registreer(txfNaam.getText(), txfGebruikersnaam.getText(), txfVoornaam.getText(), txfWachtwoord.getText());
            Alert alert3 = new Alert(Alert.AlertType.INFORMATION);
            alert3.setContentText(rb.geefTekst("registeren_alert3_message"));
            alert3.setHeaderText(rb.geefTekst("registreren_alert3_titel"));
            alert3.setTitle(rb.geefTekst("registreren_alert3_titel"));
            alert3.showAndWait();

            Stage stage = (Stage) btnArrow.getScene().getWindow();
            Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
            stage.setScene(scene);
            stage.setTitle("Sokoban");
            stage.show();

        } catch (IllegalArgumentException | GebruikersnaamAlGebruiktException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.setHeaderText(rb.geefTekst("gui_opnieuw"));
            alert.setTitle(rb.geefTekst("gui_error"));
            alert.show();
        } 
    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event)
    {
        Stage stage = (Stage) btnArrow.getScene().getWindow();
        Scene scene = new Scene(new LoginRegisterkeuzeController(taal));
        stage.setScene(scene);
        stage.setTitle("Sokoban");
        stage.show();
    }

    private void addProgress(int correct)
    {
        if (correct > 0)
        {
            if (piProgressCirclePass.getProgress() <= 0.0f)
            {
                piProgressCirclePass.setProgress(0.0f);
                piProgressCirclePass.setProgress(+(correct * 0.25f));
            }
            piProgressCirclePass.setProgress(+(correct * 0.25f));
        } else
        {
            piProgressCirclePass.setProgress(-1.0f);
        }
    }

    @FXML
    private void txfWachtwoordOnKeyReleased(KeyEvent event)
    {
        piProgressCirclePass.setVisible(true);
        if (event.getCode().getName().equalsIgnoreCase("ENTER"))
        {
            btnArrowOnAction(null);
        } else
        {

            if (txfWachtwoord.getText().length() <= 1)
            {
                piProgressCirclePass.setProgress(-1.0f);
            } else
            {
                boolean pos1 = txfWachtwoord.getText().matches(".{0,}[a-z]{1,}.{0,}");
                boolean pos2 = txfWachtwoord.getText().matches(".{0,}[A-Z]{1,}.{0,}");
                boolean pos3 = txfWachtwoord.getText().matches(".{0,}[0-9]{1,}.{0,}");
                boolean pos4 = txfWachtwoord.getText().length() >= 8;

                boolean[] arrBool =
                {
                    pos1, pos2, pos3, pos4
                };
                int teller = 0;
                for (int i = 0; i < arrBool.length; i++)
                {
                    if (arrBool[i])
                    {
                        teller++;
                    }
                }
                addProgress(teller);
            }
        }
    }

    @FXML
    private void txfGebruikersnaamOnKeyReleased(KeyEvent event)
    {
        piProgressCircleUsername.setVisible(true);
        if (txfGebruikersnaam.getText().length() >= 8)
        {

            piProgressCircleUsername.setProgress(1.0f);
        } else
        {

            piProgressCircleUsername.setProgress(-1.0f);
        }

    }

    @FXML
    private void piProgressCircleUsernameOnMouseEntered(MouseEvent event)
    {
        if (piProgressCircleUsername.getProgress() == -1.0f)
        {
            lblUsernameRules.setText(rb.geefTekst("progress_tekens"));
            lblUsernameRules.setVisible(true);
        } else
        {
            lblUsernameRules.setVisible(false);
        }
    }

    @FXML
    private void piProgressCircleUsernameOnMouseExited(MouseEvent event)
    {
        lblUsernameRules.setVisible(false);
    }

    @FXML
    private void piProgressCirclePassMouseEntered(MouseEvent event)
    {
        if (piProgressCirclePass.getProgress() < 1.0f)
        {
            if (txfWachtwoord.getText().matches(".{0,}[a-z]{1,}.{0,}"))
            {
                lblPassRules3.setStyle("-fx-border-color:green;-fx-text-fill:green;-fx-background-color:white;");
            }
            if(txfWachtwoord.getText().matches(".{0,}[A-Z]{1,}.{0,}")){
            lblPassRules2.setStyle("-fx-border-color:green;-fx-text-fill:green;-fx-background-color:white;");
            }
            if(txfWachtwoord.getText().matches(".{0,}[0-9]{1,}.{0,}"))
            {
            lblPassRules4.setStyle("-fx-border-color:green;-fx-text-fill:green;-fx-background-color:white;");
            }
            if(txfWachtwoord.getText().length() >= 8)
            {
            lblPassRules1.setStyle("-fx-border-color:green;-fx-text-fill:green;-fx-background-color:white;");
            }
            else{}
            lblPassRules1.setText(rb.geefTekst("progress_tekens"));
            lblPassRules2.setText(rb.geefTekst("progress_grote_letter"));
            lblPassRules3.setText(rb.geefTekst("progress_kleine_letter"));
            lblPassRules4.setText(rb.geefTekst("progress_cijfer"));
            lblPassRules1.setVisible(true);
            lblPassRules2.setVisible(true);
            lblPassRules3.setVisible(true);
            lblPassRules4.setVisible(true);
            txfWachtwoord.setVisible(false);
        } else
        {
            lblPassRules1.setVisible(false);
            lblPassRules2.setVisible(false);
            lblPassRules3.setVisible(false);
            lblPassRules4.setVisible(false);
        }
    }

    @FXML
    private void piProgressCirclePassMouseExited(MouseEvent event)
    {
        lblPassRules1.setVisible(false);
        lblPassRules2.setVisible(false);
        lblPassRules3.setVisible(false);
        lblPassRules4.setVisible(false);
        txfWachtwoord.setVisible(true);
    }

}
