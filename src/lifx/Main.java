package lifx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
*
* @author LGuevara
*/
public class Main extends Application {
	private static Stage pStage;
	private static VBox root;

	@Override
	public void start(Stage primaryStage) {
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource("lifx/UI.fxml"));
			Scene scene = new Scene(root, root.getPrefWidth()-12,root.getPrefHeight()-12);

			scene.getStylesheets().add(getClass().getResource("modena.css").toExternalForm());	
			primaryStage.setScene(scene);
			//primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icons/icon.png")));
			primaryStage.setTitle("MacOS Lifx Controller");
			primaryStage.show();
			primaryStage.setResizable(false);
			setPrimaryStage(primaryStage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return pStage;
	}

	private void setPrimaryStage(Stage pStage) {
		Main.pStage = pStage;
	}

	public static VBox getRoot() {
		return root;
	}

	public static void setRoot(VBox newRoot) {
		root = newRoot;
	}
}

