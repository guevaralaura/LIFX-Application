package lifx;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lightComponent.LightController;

public class HomeController implements Initializable{
    public static int currentId;
    public static String sceneColor;
    public static String token;
    public static List<JsonObject> lightsList;
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
    private Label topLabel;
    @FXML
    private Label lightsLabel;
    @FXML
    private Label scenesLabel;

    @FXML
    private ImageView settingsDown;
    @FXML
    private ImageView settingsUp;
    @FXML
    private ImageView settingsHover;

    @FXML
    private DialogPane settingsDialog;
    @FXML
    private TextField tokenField;

    private List<LightController> lightsUI = new ArrayList();


    public static String parseHexColor(String original){
        StringBuilder sb = new StringBuilder(original);
        //Remove "0x" and "ff" from front and end of color string
        sb.deleteCharAt(9); sb.deleteCharAt(8); sb.deleteCharAt(1); sb.deleteCharAt(0);
        return sb.toString();
    }

    @FXML
    void handleScene(MouseEvent event) throws IOException {
        //set all the lights to the same color
        String[] arr = event.getSource().toString().split(", ");
        String id = arr[0].substring(10);
        sceneColor = parseHexColor(sceneThreeClr.getFill().toString());
        if(id.equalsIgnoreCase("sceneoneclr")){
            sceneColor = parseHexColor(sceneOneClr.getFill().toString());
        }else if(id.equalsIgnoreCase("scenetwoclr")){
            sceneColor = parseHexColor(sceneTwoClr.getFill().toString());
        }

        //logic
        if(sceneColor.equals("0x000000ff")){
            LightLogic.turnOff(-1);

        }else{
            LightLogic.setColor(sceneColor, -1);
            LightLogic.turnOn(-1);
        }
        //update ui
        for (LightController light : lightsUI) {
            light.setScene.fire();
        }

    }


    @FXML
    void handleSettings(MouseEvent event) {
        settingsDown.setVisible(false);
        settingsUp.setVisible(true);
        settingsHover.setVisible(false);
        //open new dialog to enter token
    }
    @FXML
    void handleSettingsPressed(MouseEvent event) {
        settingsDown.setVisible(true);
        settingsUp.setVisible(false);
        settingsHover.setVisible(false);
    }
    @FXML
    void handleSettingsHover(MouseEvent event) {
        settingsDown.setVisible(false);
        settingsUp.setVisible(false);
        settingsHover.setVisible(true);
    }

    @FXML
    void handlePowerAllBtn(ActionEvent event) throws IOException {
        //add sign in for lifx and get key
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            LightLogic.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLightsList();
        int total = lightsList.size();
        double x = 34;
        for(int i=0; i <total; i++){
            currentId = i;
            createLightUI(x);
            x+=150;
            checkConnection(lightsList.get(i));
        }
    }

    private void getLightsList(){
        lightsList = DataParser.getLights();
    }

    private void createLightUI(double x){
        LightController lightItem = new LightController();
        lightItem.setLayoutX(x);
        lightItem.setId(String.valueOf(currentId));
        lightItem.setLayoutY(124);
        pane.getChildren().addAll(lightItem);
        lightsUI.add(lightItem);
    }

    private void checkConnection(JsonObject light){
        if(light.get("connected").getAsString().equalsIgnoreCase("false")) {
            String name = light.get("label").getAsString();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Device Not Found");
            alert.setContentText("There was an error connecting to '" + name + "'. Please check your connection or restart the device.");
            alert.show();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
        }
    }

    /*getting token input: try{
        token = new String(Files.readAllBytes(Paths.get("token.dat")));
    } catch (IOException e) {
        TextInputDialog in = new TextInputDialog();
        in.setTitle("Settings");
        in.setHeaderText("Your Token");
        in.setContentText("Please enter your token:");

        // Traditional way to get the response value.
        Optional<String> result = in.showAndWait();
        if (result.isPresent()){
        token = result.get();
    }

     <DialogPane fx:id="settingsDialog" expanded="false" layoutX="86.0" layoutY="151.0" prefHeight="113.0" prefWidth="360.0" styleClass="dialog">
               <header>
                  <Label contentDisplay="TEXT_ONLY" prefHeight="37.0" prefWidth="328.0" text="Your Token:">
                     <font>
                        <Font size="17.0" url="@../fonts/3ds Light.ttf" />
                     </font>
                  </Label>
               </header>
               <graphic>
                  <ImageView fitHeight="150.0" fitWidth="200.0" pickOnBounds="true" preserveRatio="true" />
               </graphic>
               <content>
                  <TextField prefHeight="26.0" prefWidth="328.0" promptText="Token">
                     <font>
                        <Font size="12.0" url="@../fonts/3ds Light.ttf" />
                     </font>
                  </TextField>
               </content>
               <buttonTypes>
                  <ButtonType fx:constant="APPLY" />
               </buttonTypes>
            </DialogPane>
        */


}
