package lightcomponent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import lifx.Light;

public class Component extends VBox  implements Initializable{
	private static Light current;
	private static String name;
	private static String color;
	private static Boolean on;
	public static Button update = new Button();
	
	@FXML
    private Circle circle;

	@FXML
    private ImageView pic;
    
    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private Label label;
    
    void updateColor(){
    	update.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent we) {
				colorPicker.setValue(getCurrentColor());
				handleColorPicker(we);
			}
		});
    }
    
    
    //create instance of component
    public Component() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("component.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();            
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String value) {
        name = value;
    }
    public String getLabel() {
        return label.getText();
    }
    
    public void setLabel(String value) {
        label.setText(value);
    }
    public String getColor() {
        return circle.getStroke().toString();
    }
    
    public void setColor(Color value) {
    	colorPicker.setValue(value);
    	handleColorPicker(null);
    	
    	color = parseHexColor(value.toString());
    	current.setColor(color);
    }

        
    @FXML
    void handleColorPicker(ActionEvent event) {
		circle.setStroke(colorPicker.getValue());
		if(colorPicker.getValue().toString().equals("0x000000ff")){
    		pic.setOpacity(0.2);
    		current.turnOff();
    		on = false;
		}else{
			//pic.setOpacity(1);
			current.setColor(parseHexColor(colorPicker.getValue().toString()));
			//on = true;
		}
    }
    
    public String parseHexColor(String original){
		StringBuilder sb = new StringBuilder(original);
		//Remove "0x" and "ff" from front and end of color string
		sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
		return sb.toString();
    }
    

    @FXML
    void handleLight(MouseEvent event) {
		if(!on){
			pic.setOpacity(1);
			current.turnOn();
			on = true;
		}else{
			pic.setOpacity(0.2);
			current.turnOff();
			on = false;
		}
    }
    
    Color getCurrentColor(){
    	Color currentColor = Color.hsb(current.getHue(), current.getSaturation(), current.getBrightness());
		return currentColor;
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateColor();
		//check if light is on or off
		current  = HomeController.user.getLight(HomeController.currentId);
		if(current.getPower()==1){
			pic.setOpacity(1);
			on = true;
		}else{
			pic.setOpacity(0.2);
			on = false;
		}
		
		//check for the current color 
		Color currentColor = Color.hsb(current.getHue(), current.getSaturation(), current.getBrightness());
		color = parseHexColor(currentColor.toString());
		circle.setStroke(currentColor);
		colorPicker.setValue(currentColor);
		
		//assign name
		label.setText(current.getName());
		
	
	}
    
}
