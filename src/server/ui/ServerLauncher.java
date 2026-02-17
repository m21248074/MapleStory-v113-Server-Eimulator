package server.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class ServerLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 載入 FXML 檔案
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
        Parent root = loader.load();

        Image icon = new Image(getClass().getResourceAsStream("/image/Icon.png"));
        primaryStage.getIcons().add(icon);

        primaryStage.setTitle("楓之谷伺服端控制台");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        System.exit(0); 
    }

    public static void main(String[] args) {
        launch(args);
    }
}