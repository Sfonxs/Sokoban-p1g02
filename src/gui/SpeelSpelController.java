package gui;

import gui.utils.CloseConfirmation;
import domein.DomeinController;
import java.io.IOException;
import java.util.List;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import resources.ResourceBundel;

public class SpeelSpelController extends Pane
{

    private DomeinController domcon;
    private Locale taal;
    private ResourceBundel rb;
    private String[][] spelbordVelden;
    private GridPane pane;

    private KeyEvent shortcutKeyEvent;
    private EventHandler selectedEventHandler;
    private List<EventHandler> eventHandlers;
    private Image image;
    private Image image2;
    private ImageView pic;
    private ImageView pic1;
    private String spelbordNaam;
    private String keyPressed;
    private Avatar avatar;

    @FXML
    private Button btnArrowBack;
    @FXML
    private ImageView imgArrow1;
    @FXML
    private Button btnStop;
    @FXML
    private ImageView imgStop;
    @FXML
    private Pane panePane;
    @FXML
    private Button btnReset;
    @FXML
    private Label lblWins;
    @FXML
    private Label lblStappen;

    public SpeelSpelController(DomeinController domCon, Locale deTaal, String spelBordNaam, Avatar avatar)
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("SpeelSpel.fxml"));
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

        panePane.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent ke)
            {

                setKeyPressed(ke.getCode().getName());

                if (keyPressed.equalsIgnoreCase("Z") || keyPressed.equalsIgnoreCase("UP"))
                {

                    domcon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_boven"));
                    spelbordVelden = domcon.geefSpelBordVelden();
                    pane.getChildren().clear();
                    maakSpelBord();

                } else if (keyPressed.equalsIgnoreCase("D") || keyPressed.equalsIgnoreCase("RIGHT"))
                {
                    domcon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_rechts"));
                    spelbordVelden = domcon.geefSpelBordVelden();
                    pane.getChildren().clear();
                    maakSpelBord();
                } else if (keyPressed.equalsIgnoreCase("Q") || keyPressed.equalsIgnoreCase("LEFT"))
                {
                    domcon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_links"));
                    spelbordVelden = domcon.geefSpelBordVelden();
                    pane.getChildren().clear();
                    maakSpelBord();
                } else if (keyPressed.equalsIgnoreCase("S") || keyPressed.equalsIgnoreCase("DOWN"))
                {
                    domcon.verplaatsVentje(rb.geefTekst("speel_spel_verplaats_onder"));
                    spelbordVelden = domcon.geefSpelBordVelden();
                    pane.getChildren().clear();
                    maakSpelBord();
                }

            }
        });
        this.avatar = avatar;
        if (this.avatar == null)
        {
            this.avatar = Avatar.Beast;
        }
        domcon = domCon;
        taal = deTaal;
        rb = new ResourceBundel();
        spelbordNaam = spelBordNaam;
        domcon.speelVolgendSpelbord();
        spelbordVelden = domcon.geefSpelBordVelden();
        maakSpelBord();

    }

    private void maakSpelBord()
    {
        lblStappen.setText(rb.geefTekst("maak_spelbord_stappen") + " " + domcon.geefAantalStappenInActiefSpelbord());
        pane = new GridPane();
        for (int y = 0; y < spelbordVelden.length; y++)
        {
            String[] rij = spelbordVelden[y];
            for (int x = 0; x < rij.length; x++)
            {
                if (rij[x].equalsIgnoreCase("+"))
                {
                    image = new Image("/resources/images/Wall.png");
                } else if (rij[x].equalsIgnoreCase("#"))
                {
                    image = new Image("/resources/images/Crate.png");
                } else if (rij[x].equalsIgnoreCase("D"))
                {
                    image = new Image("/resources/images/Doel.png");
                } else if (rij[x].equalsIgnoreCase("$"))
                {
                    image = new Image(avatar.getResourceUrl());
                } else if (rij[x].equalsIgnoreCase("@"))
                {
                    image = new Image("/resources/images/CrateDoel.png");
                } else
                {
                    image = new Image("/resources/images/Veld.png");
                }
                imageStuffFix(image, x, y);

            }
        }

        panePane.getChildren().add(pane);

        if (domcon.isSpelbordVoltooid())
        {
            int[] lol = domcon.geefSpelbordResultaat();

            lblWins.setText(rb.geefTekst("maak_spelbord_voltooid") + " " + (lol[0] + 1) + rb.geefTekst("maak_spelbord_totaal") + " " + lol[1]);

            domcon.speelVolgendSpelbord();
            if (domcon.isActiefSpelVoltooid())
            {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(rb.geefTekst("maak_spelbord_alert_context"));
                alert.setHeaderText(rb.geefTekst("maak_spelbord_alert_header"));
                alert.setTitle(rb.geefTekst("maak_spelbord_alert_titel"));
                alert.showAndWait();
                btnArrowBackOnAction(null);
            } else
            {
                Alert alertVoltooid = new Alert(Alert.AlertType.CONFIRMATION, rb.geefTekst("voltooid_alert").replace("{SPELBORDNAAM}", spelbordNaam), ButtonType.YES, ButtonType.NO);
                alertVoltooid.setContentText(rb.geefTekst("voltooid_alert_context"));
                alertVoltooid.setHeaderText(rb.geefTekst("voltooid_alert_header").replace("{SPELBORDNAAM}", spelbordNaam));
                alertVoltooid.setTitle(rb.geefTekst("voltooid_titel"));
                alertVoltooid.showAndWait();

                if (alertVoltooid.getResult() == ButtonType.YES)
                {
                    spelbordVelden = domcon.geefSpelBordVelden();
                    maakSpelBord();

                } else
                {
                    btnArrowBackOnAction(null);
                }
            }

        }
    }

    private void imageStuffFix(Image image, int x, int y)
    {

        pic = new ImageView();
        pic.setFitWidth(50);
        pic.setFitHeight(50);
        pic.setLayoutX(x);
        pic.setLayoutY(y);
        pic.setImage(image);
        GridPane.setConstraints(pic, x, y);
        pane.getChildren().addAll(pic);
    }

    public void setKeyPressed(String keyPressed)
    {
        this.keyPressed = keyPressed;
    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event)
    {
        domcon.resetActiefspel();
        Stage stage = (Stage) btnArrowBack.getScene().getWindow();
        stage.setTitle("Sokoban");
        Scene scene = new Scene(new KiesSpelController(domcon, taal));
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void btnStopOnAction(ActionEvent event)
    {
        new CloseConfirmation().show();
    }

    @FXML
    private void btnResetOnAction(ActionEvent event)
    {
        domcon.resetActiefSpelbord();
        spelbordVelden = domcon.geefSpelBordVelden();
        maakSpelBord();

    }

}
