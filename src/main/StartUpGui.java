package main;

import gui.StartschermController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * OM TE STARTEN => "Clean and Build project" => Rechts-klik hier => "Run file"
 */
public class StartUpGui extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
       stage.getIcons().add(new Image("/resources/images/Icon.gif")); //geeft nullpointer
        
        Scene scene = new Scene(new StartschermController());
        stage.setScene(scene);
        stage.setTitle("Sokoban");
        stage.setResizable(false);
        stage.show();     
    }

    public Stage getStage() {
        return stage;
    }

    public static void main(String... args) {
        Application.launch(StartUpGui.class, args);
    }
}
