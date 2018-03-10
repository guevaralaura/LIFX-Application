package lifx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import lightComponent.LightController;

public class HomeController implements Initializable {
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
    @FXML
    private AnchorPane lightsPane;
    @FXML
    private ScrollPane lightsScrollPane;
    @FXML
    private AnchorPane scenesPane;

    private List<LightController> lightsUI = new ArrayList();


    public static String parseHexColor(String original) {
        StringBuilder sb = new StringBuilder(original);
        //Remove "0x" and "ff" from front and end of color string
        sb.deleteCharAt(9);
        sb.deleteCharAt(8);
        sb.deleteCharAt(1);
        sb.deleteCharAt(0);
        return sb.toString();
    }

    @FXML
    void handleScene(MouseEvent event) throws IOException {
        //set all the lights to the same color
        String[] arr = event.getSource().toString().split(", ");
        String id = arr[0].substring(10);
        sceneColor = parseHexColor(sceneThreeClr.getFill().toString());
        if (id.equalsIgnoreCase("sceneoneclr")) {
            sceneColor = parseHexColor(sceneOneClr.getFill().toString());
        } else if (id.equalsIgnoreCase("scenetwoclr")) {
            sceneColor = parseHexColor(sceneTwoClr.getFill().toString());
        }

        //logic
        if (sceneColor.equals("0x000000ff")) {
            LightLogic.turnOff(-1);

        } else {
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

    @FXML
    void handleSwipeLeft(MouseEvent event) throws IOException {
        System.out.println("left");
    }

    @FXML
    void handleSwipeRight(MouseEvent event) throws IOException {
        System.out.println("right");
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
        if(total>3){
            lightsScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        }
        double x = 20;
        for (int i = 0; i < total; i++) {
            currentId = i;
            createLightUI(x);
            x += 150;
            checkConnection(lightsList.get(i));
        }
    }

    private void getLightsList() {
        lightsList = DataParser.getLights();
    }

    private void createLightUI(double x) {
        LightController lightItem = new LightController();
        lightItem.setLayoutX(x);
        lightItem.setId(String.valueOf(currentId));
        lightItem.setLayoutY(1);
        lightsPane.getChildren().addAll(lightItem);
        lightsUI.add(lightItem);
    }

    private void checkConnection(JsonObject light) {
        if (light.get("connected").getAsString().equalsIgnoreCase("false")) {
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
}
