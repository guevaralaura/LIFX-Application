package lifx.LightControl;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Group{
	//Group class will house Lights and allow controls to entire group
	private String id;
	private String name;
	private String selector;
	
	public Light[] Light;
	
	private int numLights;
	/*Following variables are true only if all lights in group are the same value for these properties
	 * -1 if they are not the same, otherwise the value is correct for all lights.
	 */
	private int power; //-1 for not the same, 0 for off, 1 for on
	private float hue;
	private float saturation;
	private float kelvin;
	private float brightness;
	
	Group(String idIn, String nameIn){
		if(idIn.startsWith("\"") && idIn.endsWith("\"")) idIn = idIn.substring(1, idIn.length()-1);
		if(nameIn.startsWith("\"") && nameIn.endsWith("\"")) nameIn = nameIn.substring(1, nameIn.length()-1);
		id = idIn;
		name = nameIn;
		selector = "group_id:" + id;

		String lifxGroupString = LightFunctions.listLights(selector);
		JsonElement lifxJsonElement = new JsonParser().parse(lifxGroupString);
		JsonArray lightJsonArray = lifxJsonElement.getAsJsonArray();
		
		//Put group objects in an ArrayList for first time (because dynamic)
		ArrayList<Light> lightList = new ArrayList<Light>(); //Contains type lifx.LightControl.group for each location
		JsonObject lightObject;
		Light currentLight;
		
		//Go through each light in JSON array and create its own Light object
		int numLights = lightJsonArray.size();
		for (int i = 0; i < numLights; i++){
			lightObject = lightJsonArray.get(i).getAsJsonObject(); //Get new light from array
			currentLight = new Light(lightObject); //Create new light object
			if (currentLight != null) System.out.println("		Light object \"" + currentLight.getName() + "\" created. id:" + currentLight.getId());
			lightList.add(currentLight);
		}
		
		numLights = lightList.size();
		Light = new Light[numLights];
		//Put lights from lightList into a Light array for easier access
		for (int i = 0; i < numLights; i++) Light[i] = lightList.get(i);
		
		lightList = null;
		lifxJsonElement = null; //Delete memory
	}
	
	/** Requests new information from LIFX server for the group
	 * and updates that information in the class. This is to be done before each
	  time a public or private "get" or "is" method is used.
	 */
	private void updateInfo(){
		if (numLights <= 0) return;
		String lifxGroupString = LightFunctions.listLights(selector);
		JsonElement lifxJsonElement = new JsonParser().parse(lifxGroupString);
		JsonArray lightJsonArray = lifxJsonElement.getAsJsonArray();
		JsonObject lightJsonObject = lightJsonArray.get(0).getAsJsonObject();
		
		String powerString = LightFunctions.removeQuotes(((String) lightJsonObject.get("power").getAsString()));
		if (powerString.equals("on")) power = 1;
		else power = 0;
		hue = lightJsonObject.get("hue").getAsFloat();
		saturation = lightJsonObject.get("saturation").getAsFloat();
		kelvin = lightJsonObject.get("kelvin").getAsFloat();
		brightness = lightJsonObject.get("brightness").getAsFloat();
		
		if (numLights == 1) { lifxJsonElement = null; return; }
		//Compare each current property value to the property of the next light.
		for (int i = 1; i < numLights; i++){
			if (power != -1){ //Damn strings make this more difficult than it needs to be
				powerString = LightFunctions.removeQuotes(((String) lightJsonObject.get("power").getAsString()));
				float powerCheck = power;
				if (powerString.equals("on")) power = 1;
				else power = 0;
				if (power != powerCheck) power = -1;
			}
			if ((hue != -1) && (hue != lightJsonObject.get("hue").getAsFloat())) hue = -1;
			if ((saturation != -1) && (saturation != lightJsonObject.get("saturation").getAsFloat())) saturation = -1;
			if ((kelvin != -1) && (kelvin != lightJsonObject.get("kelvin").getAsFloat())) kelvin = -1;
			if ((brightness != -1) && (brightness != lightJsonObject.get("brightness").getAsFloat())) brightness = -1;
		}
		lifxJsonElement = null;
	}

	
	
	/**Turns on power for this group.*/
	public void turnOn(){
		LightFunctions.turnOn(selector);
	}
	
	/**Turns off power for this group.*/
	public void turnOff(){
		LightFunctions.turnOff(selector);
	}
	
	/**Sets the color of all lights in this group.
	 * Input is a hex colour without # before it */
	public void setColor(String hexColor){
		LightFunctions.setColour(selector, hexColor);
	}
	
	/**Brightness level between 0.0 and 1.0*/
	public void setBrightness(float brightnessLevel){
		LightFunctions.setBrightness(selector, brightnessLevel);
	}
		
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
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

	public int size(){
		return numLights;
	}
}
