package lightComponent;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lifx.HomeController;
import lifx.LightLogic;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static lifx.HomeController.parseHexColor;

public class LightController extends VBox  implements Initializable{

    public Button update = new Button();
    public Button setScene = new Button();

    private JsonObject currentLight;
    private Boolean on;

    @FXML
    private Circle circle;
    @FXML
    private ImageView pic;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private Label label;


    Color getCurrentColor(){
        JsonObject colorObject = currentLight.get("color").getAsJsonObject();
        return Color.hsb(colorObject.get("hue").getAsFloat(), colorObject.get("saturation").getAsFloat(), currentLight.get("brightness").getAsFloat());
    }

    void updateColor(){
        update.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent we) {
                colorPicker.setValue(getCurrentColor());
                try {
                    handleColorPicker(we);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setScene(){
        setScene.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ev) {
                colorPicker.setValue(Color.valueOf(HomeController.sceneColor));
                circle.setStroke(colorPicker.getValue());
                if(colorPicker.getValue().toString().equals("0x000000ff")){
                    pic.setOpacity(0.2);
                    on = false;
                }else{
                    pic.setOpacity(1);
                    String color = colorPicker.getValue().toString();
                    on = true;
                }
            }
        });
    }

    //create instance of component
    public LightController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("lightUI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    void handleColorPicker(ActionEvent event) throws IOException {
        //System.out.println(this.getId());
        circle.setStroke(colorPicker.getValue());
        if(colorPicker.getValue().toString().equals("0x000000ff")){
            pic.setOpacity(0.2);
            LightLogic.turnOff(Integer.valueOf(this.getId()));
            on = false;
        }else{
            pic.setOpacity(1);
            String color = colorPicker.getValue().toString();
            LightLogic.setColor(parseHexColor(color), Integer.valueOf(this.getId()));
            LightLogic.turnOn(Integer.valueOf(this.getId()));
            on = true;
        }
    }

    @FXML
    void handleLight(MouseEvent event) throws IOException {
        if(!on){
            pic.setOpacity(1);
            LightLogic.turnOn(Integer.valueOf(this.getId()));
            on = true;
        }else{
            pic.setOpacity(0.2);
            LightLogic.turnOff(Integer.valueOf(this.getId()));
            on = false;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //initialize button handlers
        updateColor();
        setScene();

        //check if light is on or off
        currentLight = HomeController.lightsList.get(HomeController.currentId);
        if(currentLight.get("power").getAsString().equals("on")){
            pic.setOpacity(1);
            on = true;
        }else{
            pic.setOpacity(0.2);
            on = false;
        }

        //check for the current color
        colorPicker.setValue(getCurrentColor());
        circle.setStroke(colorPicker.getValue());

        //assign name
        label.setText(currentLight.get("label").getAsString());
    }

}
