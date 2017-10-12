package lifx;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class Light {
	//Light class will control individual lights
	private String id;
	private String label;
	private String selector;
	
	private int power;
	private float hue;
	private float saturation;
	private float kelvin;
	private float brightness;
	
	/**Requires a JsonObject containing only the light information to create.*/
	public Light(JsonObject light){
		id = LightFunctions.removeQuotes(light.get("id").toString());
		label = LightFunctions.removeQuotes(light.get("label").toString());
		selector = "id:" + id;
	}
	
	
	
	/** Requests new information from LIFX server for the light
	 * and updates that information in the class. This is to be done before each
	  time a public or private "get" or "is" method is used.
	 */
	private void updateInfo(){
		/* lightJsonObject is all the information in the light.
		 * colorInformationObject contains information on the color (hue, saturation, kelvin)
		 * because the light object contains objects within it (eg: color)
		 * */
		String information = LightFunctions.listLights(selector);
		JsonElement lightJsonElement = new JsonParser().parse(information);
		JsonArray lightJsonArray = lightJsonElement.getAsJsonArray();
		JsonObject lightJsonObject = lightJsonArray.get(0).getAsJsonObject();
		JsonObject colorInformationObject = lightJsonObject.get("color").getAsJsonObject();
		if (LightFunctions.removeQuotes(lightJsonObject.get("power").toString()).equals("on")) power = 1;
		else power = 0;
		brightness = Float.valueOf(lightJsonObject.get("brightness").toString());
		hue = Float.valueOf(colorInformationObject.get("hue").toString());
		saturation = Float.valueOf(colorInformationObject.get("saturation").toString());
		kelvin = Float.valueOf(colorInformationObject.get("kelvin").toString());
		lightJsonElement = null; //Clean it up! Garbage collector will throw away the memory used later
	}
	
	/**Turns on power for this specific light.*/
	public void turnOn(){
		LightFunctions.turnOn(selector);
	}
	
	/**Turns off power for this specific light.*/
	public void turnOff(){
		LightFunctions.turnOff(selector);
	}
	
	/**Sets the color of this light. Input is a hex colour without # before it */
	public void setColor(String hexColor){
		LightFunctions.setColour(selector, hexColor);
	}
	
	/**Brightness level between 0.0 and 1.0*/
	public void setBrightness(float brightnessLevel){
		LightFunctions.setBrightness(selector, brightnessLevel);
	}
	
	/**Returns id of Light object.
	To use as a selector use String selector = "id:" + Light.getId()*/
	public String getId(){
		return id;
	}
	
	/**Returns name/label of Light object.
	 To use as a selector use String selector = "label:" + Light.getName()*/
	public String getName(){
		return label;
	}
	
	public int getPower(){
		updateInfo();
		return power;
	}
	
	public float getHue(){
		updateInfo();
		return hue;
	}
	
	public float getSaturation(){
		updateInfo();
		return saturation;
	}
	
	public float getKelvin(){
		updateInfo();
		return kelvin;
	}
	
	public float getBrightness(){
		updateInfo();
		return brightness;
	}
	

}


/*
{
	"id": "...",
	"uuid"...
	"label": "Desk Lamp",
	"connected": "true",
	"power": "off",
	"color": {
		"hue": 0,
		"saturation": 0,
		"kelvin": 6000
	},
	"brightness": 1, (float?)
	"group": {
		"id"
		"name"
	}
	"location": {
		"id"
		"name"
	}
	product: {
		name: Color 1000
		...
		...
		capabilities : {
			has_color: true
		}
	}
}
*/