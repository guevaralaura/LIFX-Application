package lifx;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

//import com.jfoenix.controls.JFXColorPicker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.AmbientLight;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
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
    		StringBuilder sb = new StringBuilder(colorPicker.getValue().toString());
    		//Remove "0x" and "ff" from front and end of color string
    		sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
    		LightFunctions.setColour(sb.toString());
    		
    }
    
    boolean on = false;
    @FXML
    void handleLight(ActionEvent event) {
	    	if(!on){
	    		lightPic.setOpacity(1);
	    		on = true;
	    		LightFunctions.turnon();
	    	}else{
	    		lightPic.setOpacity(0.2);
	    		on = false;
	    		LightFunctions.turnoff();
	    	}
	    	System.out.println("Hello World!!");
    }

    @FXML
    void handleScene(ActionEvent event) {
    	//set light to getcolor of circle
    	
    }

    
    @FXML
    void handleSettings(ActionEvent event) {
    	}
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    		lightPic.setOpacity(0.2);
    }

}

