package gui;

import domein.DomeinController;
import excepties.BestaandSpelException;
import excepties.GeenToegangException;
import excepties.OngeldigSpelbordException;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class ConfigureerSchermController extends Pane
{

    @FXML
    private Button btnArrowBack;
    @FXML
    private Button btnStop;
    @FXML
    private GridPane gridSpelbord;
    @FXML
    private GridPane gridKeuze;
    @FXML
    private Button btnVolgendSpelbord;
    @FXML
    private Button btnStoppenEnOpslaan;
    @FXML
    private TextField txfNaam;
    @FXML
    private Text lblKiesNaam;
    @FXML
    private Button btnKiesNaam;
    @FXML
    private Label lblTitel;

    private DomeinController domCon;
    private Locale taal;
    private ResourceBundel rb;
    private String spelNaam;
    private int level;

    private int selectedType;
    @FXML
    private ImageView imgMuur;
    @FXML
    private ImageView imgKist;
    @FXML
    private ImageView imgDoel;
    @FXML
    private ImageView imgVeldLeeg;
    @FXML
    private ImageView imgVentje;
    @FXML
    private ImageView imgMuurSelected;
    @FXML
    private ImageView imgKistSelected;
    @FXML
    private ImageView imgDoelSelected;
    @FXML
    private ImageView imgVeldSelected;
    @FXML
    private ImageView imgVentjeSelected;

    public ConfigureerSchermController(DomeinController domCon, Locale deTaal) throws GeenToegangException
    {

        domCon.startConfigurerenSpel();
        this.domCon = domCon;
        this.taal = deTaal;
        this.rb = new ResourceBundel();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfigureerScherm.fxml"));
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
        toggleVisibilityMainComponents(false);
    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.geefTekst("configureer_btnback_alert_message"), ButtonType.YES, ButtonType.NO);
        alert.setContentText(rb.geefTekst("configureer_btnback_alert_message"));
        alert.setHeaderText(rb.geefTekst("configureer_btnback_alert_content"));
        alert.setTitle(rb.geefTekst("configureer_btnback_alert_titel"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            if (txfNaam.getText() == null || txfNaam.getText().equals(""))
            {
                Stage stage = (Stage) btnArrowBack.getScene().getWindow();
                stage.setTitle("Sokoban");
                Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
                stage.setScene(scene);
                stage.show();
            } else
            {
                domCon.slaNieuwSpelOp();
                Stage stage = (Stage) btnArrowBack.getScene().getWindow();
                stage.setTitle("Sokoban");
                Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
                stage.setScene(scene);
                stage.show();
            }

        } else
        {
            Stage stage = (Stage) btnArrowBack.getScene().getWindow();
            stage.setTitle("Sokoban");
            Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void btnStopOnAction(ActionEvent event)
    {
        new CloseConfirmation().show();
    }

    @FXML
    private void btnVolgendSpelbordOnAction(ActionEvent event)
    {
        try
        {
            domCon.startMakenNieuwSpelbord();
            level++;
            lblTitel.setText(rb.geefTekst("configureer_titel_volgendspelbord").replace("{SPELNAAM}", spelNaam) + level);
            showSpelbordInGrid();
        } catch (OngeldigSpelbordException e)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, rb.geefTekst(e.getMessage()));
            alert.show();
        }
    }

    @FXML
    private void btnStoppenEnOpslaanOnAction(ActionEvent event)
    {
        domCon.slaNieuwSpelOp();
        Stage stage = (Stage) getScene().getWindow();
        stage.setTitle("Sokoban");
        Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void btnKiesNaamOnAction(ActionEvent event)
    {
        spelNaam = txfNaam.getText();
        try
        {
            domCon.maakNieuwSpel(spelNaam);
            try
            {
                domCon.startMakenNieuwSpelbord();
            } catch (OngeldigSpelbordException e)
            {
                // ignore
            }
            level = 0;
            lblTitel.setText(rb.geefTekst("configureer_titel_volgendspelbord").replace("{SPELNAAM}", spelNaam) + level);
            toggleVisibilityMainComponents(true);
            showSpelbordInGrid();

        } catch (BestaandSpelException ex)
        {
            Alert bestaand = new Alert(Alert.AlertType.ERROR);
            bestaand.setContentText(rb.geefTekst("configureer_naam_fout"));
            bestaand.setHeaderText(rb.geefTekst("gui_error"));
            bestaand.setTitle(rb.geefTekst("gui_error"));
            bestaand.show();
        }
    }

    private void showSpelbordInGrid()
    {
        gridSpelbord.getChildren().clear();
        Image image;
        ImageView pic;
        String[][] velden = domCon.geefSpelbordInOpbouwVelden();
        for (int y = 0; y < velden.length; y++)
        {
            for (int x = 0; x < velden[y].length; x++)
            {
                if (velden[y][x].equalsIgnoreCase("+"))
                {
                    image = new Image("/resources/images/Wall.png");
                } else if (velden[y][x].equalsIgnoreCase("#"))
                {
                    image = new Image("/resources/images/Crate.png");
                } else if (velden[y][x].equalsIgnoreCase("D"))
                {
                    image = new Image("/resources/images/Doel.png");
                }  else if (velden[y][x].equalsIgnoreCase("@"))
                {
                    image = new Image("/resources/images/CrateDoel.png");
                } else if (velden[y][x].equalsIgnoreCase("$"))
                {
                    image = new Image("/resources/images/beastG.png");
                } else
                {
                    image = new Image("/resources/images/Veld.png");
                }
                pic = new ImageView();
                pic.setFitHeight(35);
                pic.setFitWidth(35);
                pic.setLayoutX(x);
                pic.setLayoutY(y);
                pic.setImage(image);
                GridPane.setConstraints(pic, x, y);
                gridSpelbord.add(pic, x, y);
                final int xx = x;
                final int yy = y;
                pic.setOnMouseClicked((MouseEvent event) ->
                {
                    domCon.zetSpelbordItem(xx, yy, selectedType);
                    showSpelbordInGrid();
                });
            }
        }
    }

    private void toggleVisibilityMainComponents(boolean visibility)
    {
        lblTitel.setVisible(visibility);
        gridKeuze.setVisible(visibility);
        gridSpelbord.setVisible(visibility);
        btnVolgendSpelbord.setVisible(visibility);
        btnStoppenEnOpslaan.setVisible(visibility);
        lblKiesNaam.setVisible(!visibility);
        txfNaam.setVisible(!visibility);
        btnKiesNaam.setVisible(!visibility);
    }

    private void setVision(boolean muur, boolean kist, boolean doel, boolean veld, boolean ventje)
    {
        imgMuurSelected.setVisible(muur);
        imgKistSelected.setVisible(kist);
        imgDoelSelected.setVisible(doel);
        imgVeldSelected.setVisible(veld);
        imgVentjeSelected.setVisible(ventje);
    }

    @FXML
    private void imgMuurOnMouseClicked(MouseEvent event)
    {
        selectedType = 1;
        setVision(true, false, false, false, false);
    }

    @FXML
    private void imgKistOnMouseClicked(MouseEvent event)
    {
        selectedType = 2;
        setVision(false, true, false, false, false);
    }

    @FXML
    private void imgDoelOnMouseClicked(MouseEvent event)
    {
        selectedType = 3;
        setVision(false, false, true, false, false);
    }

    @FXML
    private void imgVeldLeegOnMouseClicked(MouseEvent event)
    {
        selectedType = 5;
        setVision(false, false, false, true, false);
    }

    @FXML
    private void imgVentjeOnMouseClicked(MouseEvent event)
    {
        selectedType = 4;
        setVision(false, false, false, false, true);
    }
}
