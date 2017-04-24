
package gui.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import resources.ResourceBundel;

public class CloseConfirmation {
    
    private ResourceBundel rb;
    
    public void show(){
        rb = new ResourceBundel();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, rb.geefTekst("stop_vraag"), ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(rb.geefTekst("stop_message"));
        alert.setTitle(rb.geefTekst("stop_titel"));
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        } else {
            alert.close();
        }
    }
}
