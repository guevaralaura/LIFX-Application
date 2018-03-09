package lifx;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import java.io.File;

/**
 *
 * @author LGuevara
 */
public class Main extends Application {
    public static Stage pStage;

    @Override
    public void start(Stage primaryStage) {
        try {
            VBox root = FXMLLoader.load(getClass().getClassLoader().getResource("lifx/UI.fxml"));
            Scene scene = new Scene(root, root.getPrefWidth() - 12, root.getPrefHeight() - 12);

            scene.getStylesheets().add(getClass().getResource("UI.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("../lightComponent/lightUI.css").toExternalForm());
            primaryStage.setScene(scene);
            //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icons/icon.png")));
            primaryStage.setTitle("Lifx Device Controller");
            primaryStage.show();
            primaryStage.setResizable(false);
            setPrimaryStage(primaryStage);
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    //on closing, delete data.json
                    File d = new File("data.json");
                    d.delete();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPrimaryStage(Stage pStage) {
        Main.pStage = pStage;
    }
}

