package lightcomponent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Example  extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Component customControl = new Component();
        
        stage.setScene(new Scene(customControl));
        stage.setTitle("Custom Control");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }

}
