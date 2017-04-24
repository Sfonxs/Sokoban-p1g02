package gui;

import domein.DomeinController;
import excepties.GeenToegangException;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import resources.ResourceBundel;

public class WijzigEenSpelController extends Pane
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
    private Button btnStoppenEnOpslaan;
    @FXML
    private Label lblTitel;
    private ObservableList<String> spelKeuzes = FXCollections.observableArrayList();
    private ObservableList<String> spelBordKeuzes = FXCollections.observableArrayList();

    private DomeinController domCon;
    private Locale taal;
    private ResourceBundel rb;
    private String spelNaam;
    private int spelbordLevel;
    private int level;
    private String[] spelNamen;
    private int tellerWijzigingen;
    private int selectedType;

    @FXML
    private ListView<String> lvSpel;
    @FXML
    private ScrollPane spSpel;
    @FXML
    private ScrollPane spSpelbord;
    @FXML
    private ListView<String> lvSpelbord;
    @FXML
    private Label lblSpelbord;
    @FXML
    private Button btnSpelbordDelete;
    @FXML
    private Label lblSpelbordVerwijderd;
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
    @FXML
    private Label lblOpgeslagen;

    public WijzigEenSpelController(DomeinController domCon, Locale deTaal) throws GeenToegangException
    {

        this.domCon = domCon;
        this.taal = deTaal;
        this.rb = new ResourceBundel();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WijzigEenSpel.fxml"));
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

        spelNamen = domCon.startWijzigspel();
        for (int i = 0; i < spelNamen.length; i++)
        {
            spelKeuzes.add(spelNamen[i]);

        }

        lvSpel.setItems(spelKeuzes);

    }

    @FXML
    private void btnArrowBackOnAction(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.geefTekst("wijzig_spel_btnback_message"), ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(rb.geefTekst("wijzig_spel_btnback_header"));
        alert.setTitle(rb.geefTekst("wijzig_spel_btnback_header"));
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES)
        {
            if (lblOpgeslagen.isVisible())
            {
                domCon.resetActiefspel();
                Stage stage = (Stage) btnArrowBack.getScene().getWindow();
                stage.setTitle("Sokoban");
                Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
                stage.setScene(scene);
                stage.show();
            } else
            {
                domCon.slaActiefSpelBordOp();
                domCon.resetActiefspel();
                Stage stage = (Stage) btnArrowBack.getScene().getWindow();
                stage.setTitle("Sokoban");
                Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
                stage.setScene(scene);
                stage.show();
            }

        } else
        {
            domCon.resetActiefspel();
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
    private void btnStoppenEnOpslaanOnAction(ActionEvent event)
    {
        domCon.slaActiefSpelBordOp();
        domCon.resetActiefspel();
        Stage stage = (Stage) getScene().getWindow();
        stage.setTitle("Sokoban");
        Scene scene = new Scene(new WelkomstSchermController(taal, domCon));
        stage.setScene(scene);
        stage.show();

    }

    private void showSpelbordInGrid()
    {
        gridSpelbord.getChildren().clear();
        Image image;
        ImageView pic;
        String[][] velden = domCon.kiesSpelBord(spelbordLevel);
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
                } else if (velden[y][x].equalsIgnoreCase("@"))
                {
                    image = new Image("/resources/images/CrateDoel.png");
                }else if (velden[y][x].equalsIgnoreCase("$"))
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

                    domCon.veranderSpelBordItem(xx, yy, selectedType);
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
        btnStoppenEnOpslaan.setVisible(visibility);

    }

    @FXML
    private void lvSpelOnMouseClicked(MouseEvent event)
    {

        lblSpelbordVerwijderd.setVisible(false);
        btnSpelbordDelete.setVisible(false);
        spelNaam = lvSpel.getSelectionModel().getSelectedItem();
        spSpelbord.setVisible(true);
        lvSpelbord.setVisible(true);
        lblSpelbord.setVisible(true);
        spelBordKeuzes.clear();
        try
        {
            int aantalSpelborden = domCon.kiesSpel(spelNaam);
            int level = 1;
            for (int j = 0; j < aantalSpelborden; j++)
            {
                spelBordKeuzes.add(rb.geefTekst("lvSpel_spelbord") + " " + level);
                level++;

            }
        } catch (OnbestaandSpelException e)
        {
            e.getMessage();
        }

        lvSpelbord.setItems(spelBordKeuzes);
        lvSpelbord.getSelectionModel().select(rb.geefTekst("lvSpel_spelbord1"));
        showSpelbordInGrid();

    }

    @FXML
    private void lvSpelbordOnMouseClicked(MouseEvent event)
    {
        lblOpgeslagen.setVisible(false);
        btnSpelbordDelete.setVisible(true);
        spelbordLevel = lvSpelbord.getSelectionModel().getSelectedIndex();

        lblTitel.setText(rb.geefTekst("lvSpelbord_lbl").replace("{SPELNAAM}", spelNaam) + " " + spelbordLevel + 1);
        toggleVisibilityMainComponents(true);
        showSpelbordInGrid();
    }

    @FXML
    private void btnSpelbordDeleteOnAction(ActionEvent event)
    {
        try
        {
            domCon.verwijderActiefSpelbord();
            Stage stage = (Stage) btnStop.getScene().getWindow();
            Scene scene = new Scene(new WijzigEenSpelController(domCon, taal));
            stage.setScene(scene);
            stage.setTitle("Sokoban");
            stage.show();
        } catch (GeenToegangException e)
        {
            // zal niet voorkomen omdat de speler al admin was toen hij dit scherm opende
            throw new RuntimeException(e);
        }
//        lblSpelbordVerwijderd.setText(lvSpelbord.getSelectionModel().getSelectedItem() + " " + rb.geefTekst("spelbord_delete1") + lvSpel.getSelectionModel().getSelectedItem() + " " + rb.geefTekst("spelbord_delete2"));
//        lblSpelbordVerwijderd.setVisible(true);
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
    private void gridSpelbordOnMouseClicked(MouseEvent event)
    {
        tellerWijzigingen++;
        lblOpgeslagen.setText(tellerWijzigingen + " wijziging(en) gemaakt.");
        lblOpgeslagen.setVisible(true);
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
