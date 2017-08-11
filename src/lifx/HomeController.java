package lifx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;




import com.jfoenix.controls.JFXColorPicker;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lightcomponent.Component;


public class HomeController implements Initializable{
	
	public LifxController LC;
	
	@FXML
	private AnchorPane pane;
	
    @FXML
    private Button settingsBtn;

    @FXML
    private Circle sceneTwoClr;

    @FXML
    private Circle sceneThreeClr;

    @FXML
    private Circle sceneOneClr;
    
    @FXML
    private ImageView lightPic;
    
    List<Component> lights = new ArrayList();

    @FXML
    void handleScene(MouseEvent event) {
    	String[] arr = event.getSource().toString().split(", ");
    	String id = arr[0].substring(10);
//    	for(Component each : lights){
//        	if(id.equalsIgnoreCase("sceneone")){
//        		//LightFunctions.setColour(parseHexColor(sceneOneClr.getFill().toString()));
//        		each.setColor(sceneOneClr.getFill().toString());
//        	}else if(id.equalsIgnoreCase("scenetwo")){
//        		//LightFunctions.setColour(parseHexColor(sceneTwoClr.getFill().toString()));
//        		each.setColor(sceneTwoClr.getFill().);
//        	}else{
//        		//LightFunctions.setColour(parseHexColor(sceneThreeClr.getFill().toString()));
//        		each.setColor((Color)sceneThreeClr.getFill());
//        	}
//    	}

    	
    }

    
    @FXML
    void handleSettings(ActionEvent event) {
    	//ad sign in for lifx and get key
    }
    boolean on = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//check if light is connected
/*		if (!jsonData.get("connected").equalsIgnoreCase("true")){
			Alert connection = new Alert(AlertType.ERROR);
			connection.setTitle("Connection Error");
			connection.setContentText("Lifx could not be found.");
			connection.showAndWait();
		}*/
    	//how many k
    	
    	//check if light is on or off
    	LC = new LifxController();
//    	LightFunctions lf = new LightFunctions();
//		if (LC.Location[0].Group[0].Light[0].getPower() == 1){
//			lightPic.setOpacity(1);
//			on = true;
//		}else{
//			lightPic.setOpacity(0.2);
//			on = false;
//		}
		
		int total = LC.numLights;
		
		double x = 34;
		for(int i=0; i <total; i++){
			createLightUI(i, x);
			x+=150;
		}
		
		
		
   		//set colorpicker to current color of lifx with getters and setters
    		
    }
    
    public void createLightUI(int id, double x){
    	Component comp = new Component();
    	comp.setName(String.valueOf(id));
    	comp.setLayoutX(x);
    	comp.setLayoutY(124);
        pane.getChildren().addAll(comp);
        lights.add(comp);
    }
    

}

