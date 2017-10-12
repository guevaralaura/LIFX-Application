package lifx;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lightcomponent.Component;


public class HomeController implements Initializable{
	public static String currentId;
	public static User user;
	public static List<Light> lightsList;
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
    
//    @FXML
//    private ImageView lightPic;
    
    List<Component> lightsUI = new ArrayList();
    

    String parseHexColor(String original){
		StringBuilder sb = new StringBuilder(original);
		//Remove "0x" and "ff" from front and end of color string
		sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
		return sb.toString();
    }
    
    @FXML
    void handleScene(MouseEvent event) {
    	//set all the lights to the same color
	    String[] arr = event.getSource().toString().split(", ");
	   	String id = arr[0].substring(10);
	   	Color wanted = (Color) sceneThreeClr.getFill();
	   	if(id.equalsIgnoreCase("sceneoneclr")){
			wanted = (Color) sceneOneClr.getFill();
	   	}else if(id.equalsIgnoreCase("scenetwoclr")){
	   		wanted = (Color) sceneTwoClr.getFill();
		}
	   	//System.out.println(wanted);
		for(Component each : lightsUI){
			each.setColor(wanted);
			//Component.update.fire();
	   }
    }

    
    @FXML
    void handleSettings(ActionEvent event) {
    	//add sign in for lifx and get key
    }
    boolean on = false;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//create user class and get all the lights in this account
    	user = new User();
    	getLightsList();
    	int total = user.numLights;
    	
		double x = 34;
		for(int i=0; i <total; i++){
			currentId = lightsList.get(i).getId();
			createLightUI(x);
			x+=150;
		} 
		
    }
    public void getLightsList(){
    	lightsList = user.lights;
    }
    public void createLightUI(double x){
    	Component comp = new Component();
    	comp.setLayoutX(x);
    	comp.setLayoutY(124);
        pane.getChildren().addAll(comp);
        lightsUI.add(comp);
    }
    

}

