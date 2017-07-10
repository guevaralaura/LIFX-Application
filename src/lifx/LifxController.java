package lifx;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lifx.LightControl.LightFunctions;
import lifx.LightControl.Location;

public class LifxController {
	
	private static String token = "cd0ed572badca8064cbdb078695975aa730217c5c8ec578e65e639df50d585c8"; //Laura
	public Location[] Location;
	public int numLocations;
	public int numLights;
	private String selector = "all";
	
	/*Following variables are true only if all lights in group are the same value for these properties
	 * -1 if they are not the same, otherwise the value is correct for all lights.
	 */
	private int power; //-1 for not the same, 0 for off, 1 for on
	private float hue;
	private float saturation;
	private float kelvin;
	private float brightness;
	
	/**Creates a new LifxController. This controller houses locations, groups, and lights.
	 * To access a location use LifxController.Location[i].foo();
	 * Similarly, group is LifxController.Location[i].Group[j].foo();
	 * Lights are LifxController.Location[i].Group[j].Light[k].foo();
	 */
	public LifxController(){
		String lifxDataString = LightFunctions.listLights("all");
		JsonElement lifxJsonElement = new JsonParser().parse(lifxDataString); //Holds everything
		JsonArray lightArray = lifxJsonElement.getAsJsonArray(); //Also everything
		//Lifx Json returns an array with each individual light in it
		
		//Put location objects in an ArrayList for first time (because dynamic)
		ArrayList<Location> locationList = new ArrayList<Location>(); //Contains type lifx.LightControl.Location for each location
		JsonObject lightObject;
		JsonObject lightLocation;
		String currentId = "pp";
		int cont = 0;
		
		numLights = lightArray.size(); //Number of lights on account
		//Go through each light, find its location, and create that location as an object
		for (int i = 0; i < numLights; i++){
			
			lightObject = lightArray.get(i).getAsJsonObject();
			lightLocation = lightObject.get("location").getAsJsonObject();
			currentId = lightLocation.get("id").toString();
			
			int locationListSize = locationList.size();
			for (int j = 0; j < locationListSize; j++)
				//If location is already accounted for then 
				if (currentId.equals(((Location) locationList.get(j)).getId())) { 
					cont = 1;
					break;
				}
			if (cont == 1) continue;

			//Creates a new Location class for the current location
			Location currentLocation = new Location(currentId,lightLocation.get("name").toString());
			if (currentLocation != null) System.out.println("Location object \"" + currentLocation.getName() + "\" created.");
			locationList.add(currentLocation);
		}
		numLocations = locationList.size();
		Location = new Location[locationList.size()];
		//Put ArrayList into array. Efficiency?
		for (int i = 0; i < locationList.size(); i++) Location[i] = (Location) locationList.get(i);
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
	
	/**Turns on power for all lights.*/
	public void turnOn(){
		LightFunctions.turnOn(selector);
	}
	
	/**Turns off power for all lights.*/
	public void turnOff(){
		LightFunctions.turnOff(selector);
	}

	/**Sets the color of all lights in this location. 
	 * Input is a hex colour without # before it */
	public void setColor(String hexColor){
		LightFunctions.setColour(selector, hexColor);
	}
	
	/**Brightness level between 0.0 and 1.0*/
	public void setBrightness(float brightnessLevel){
		LightFunctions.setBrightness(selector, brightnessLevel);
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

