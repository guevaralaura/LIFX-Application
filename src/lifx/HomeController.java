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
    }
    
    boolean on = false;
    @FXML
    void handleLight(ActionEvent event) {
    	if(!on){
    		lightPic.setOpacity(1);
    		on = true;
    	}else{
    		lightPic.setOpacity(0.2);
    		on = false;
    	}
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

