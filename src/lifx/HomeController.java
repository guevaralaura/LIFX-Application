package lifx;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.TreeMap;


import javafx.event.ActionEvent;

import com.jfoenix.controls.JFXColorPicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lifx.LightFunctions;


public class HomeController implements Initializable{
	@FXML
    private Circle lightOneClr;

    @FXML
    private Button settingsBtn;

    @FXML
    private ColorPicker colorPicker;
    
    @FXML
    private JFXColorPicker clrPicker;

    @FXML
    private Circle sceneTwoClr;

    @FXML
    private Circle sceneThreeClr;

    @FXML
    private Circle sceneOneClr;

    @FXML
    private Button lightOne;

    @FXML
    private Button sceneTwo;

    @FXML
    private Button sceneOne;

    @FXML
    private Button sceneThree;
    
    @FXML
    private ImageView lightPic;
    
    @FXML
    void handleColorPicker(ActionEvent event) {
		lightOneClr.setStroke(colorPicker.getValue());
		if(colorPicker.getValue().toString().equals("0x000000ff")){
    		lightPic.setOpacity(0.2);
    		on = false;
    		LightFunctions.turnOff();
		}else{
			LightFunctions.setColour(parseHexColor(colorPicker.getValue().toString()));
		}
    }
    
    String parseHexColor(String original){
		StringBuilder sb = new StringBuilder(original);
		//Remove "0x" and "ff" from front and end of color string
		sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
		return sb.toString();
    }
    
    boolean on = false;
    @FXML
    void handleLight(ActionEvent event) {
		if(!on){
			lightPic.setOpacity(1);
			on = true;
			LightFunctions.turnOn();
		}else{
			lightPic.setOpacity(0.2);
			on = false;
			LightFunctions.turnOff();
		}
    }

    @FXML
    void handleScene(ActionEvent event) {
    	String[] arr = event.getSource().toString().split(", ");
    	String id = arr[0].substring(10);
    	
    	if(id.equalsIgnoreCase("sceneone")){
    		//LightFunctions.setColour(parseHexColor(sceneOneClr.getFill().toString()));
    		colorPicker.setValue((Color) sceneOneClr.getFill());
    		colorPicker.fireEvent(event);
    	}else if(id.equalsIgnoreCase("scenetwo")){
    		//LightFunctions.setColour(parseHexColor(sceneTwoClr.getFill().toString()));
    		colorPicker.setValue((Color) sceneTwoClr.getFill());
    		colorPicker.fireEvent(event);
    	}else{
    		//LightFunctions.setColour(parseHexColor(sceneThreeClr.getFill().toString()));
    		colorPicker.setValue((Color) sceneThreeClr.getFill());
    		colorPicker.fireEvent(event);
    	}
    	
    }

    
    @FXML
    void handleSettings(ActionEvent event) {
    	//ad sign in for lifx and get key
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//check if light is connected
/*		if (!jsonData.get("connected").equalsIgnoreCase("true")){
			Alert connection = new Alert(AlertType.ERROR);
			connection.setTitle("Connection Error");
			connection.setContentText("Lifx could not be found.");
			connection.showAndWait();
		}*/
    	//check if light is on or off
    	LightFunctions lf = new LightFunctions();
		if (LightFunctions.isOn()){
			lightPic.setOpacity(1);
			on = true;
		}else{
			lightPic.setOpacity(0.2);
			on = false;
		}
		
		
   		//set colorpicker to current color of lifx with getters and setters
    		
    }

}

