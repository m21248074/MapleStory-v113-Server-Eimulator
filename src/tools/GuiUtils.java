package tools;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GuiUtils {

    private static final String ICON_PATH = "/image/Icon.png";

    public static ButtonType showAlert(AlertType type, String title, String header, String content, boolean needWait, Window owner) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        
        if (owner != null) {
            alert.initOwner(owner);
        }

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        try {
            stage.getIcons().add(new Image(GuiUtils.class.getResourceAsStream(ICON_PATH)));
        } catch (Exception e) {
            System.err.println("無法載入 Alert 圖示: " + e.getMessage());
        }

        Optional<ButtonType> opt = Optional.empty();

        if (needWait) 
            opt = alert.showAndWait();
        else {
            alert.show();
            return null;
        }
        ButtonType rtn = opt.get();
        return rtn;
    }
}