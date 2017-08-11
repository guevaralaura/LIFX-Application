package lightcomponent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class Component extends VBox  implements Initializable{
	
	private static String name;
	private static String color;
	private static Boolean on;

	@FXML
    private Circle circle;

	@FXML
    private ImageView pic;
    
    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private Label label;
    
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
        return color;
    }
    
    public void setColor(String value) {
        color = value;
    }
        
    @FXML
    void handleColorPicker(ActionEvent event) {
		circle.setStroke(colorPicker.getValue());
		if(colorPicker.getValue().toString().equals("0x000000ff")){
    		pic.setOpacity(0.2);
    		on = false;
		}else{
			on = true;
			pic.setOpacity(1);
			color = parseHexColor(colorPicker.getValue().toString());
		}
    }
    
    String parseHexColor(String original){
		StringBuilder sb = new StringBuilder(original);
		//Remove "0x" and "ff" from front and end of color string
		sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
		return sb.toString();
    }
    

    @FXML
    void handleLight(MouseEvent event) {
		if(!on){
			pic.setOpacity(1);
			on = true;
		}else{
			pic.setOpacity(0.2);
			on = false;
		}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pic.setOpacity(0.2);
		on = false;
		
		
	}
    
}
