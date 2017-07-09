package lifx.LightControl;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lifx.LightFunctions;

public class Group{
	//Group class will house Lights and allow controls to entire group
	private String id;
	private String name;
	private String selector;
	
	public Light[] lights;
	public int numLights;
	
	Group(String idIn, String nameIn){
		if(idIn.startsWith("\"") && idIn.endsWith("\"")) idIn = idIn.substring(1, idIn.length()-1);
		if(nameIn.startsWith("\"") && nameIn.endsWith("\"")) nameIn = nameIn.substring(1, nameIn.length()-1);
		id = idIn;
		name = nameIn;
		selector = "group_id:" + id;

		String lifxGroupString = LightFunctions.listLights(selector);
		JsonElement lifxJsonElement = new JsonParser().parse(lifxGroupString);
		JsonArray lightArray = lifxJsonElement.getAsJsonArray();
		
		//Put group objects in an ArrayList for first time (because dynamic)
		ArrayList<Light> lightList = new ArrayList<Light>(); //Contains type lifx.LightControl.group for each location
		JsonObject lightObject;
		Light currentLight;
		
		//Go through each light in JSON array and create its own Light object
		int numLights = lightArray.size();
		for (int i = 0; i < numLights; i++){
			lightObject = lightArray.get(i).getAsJsonObject(); //Get new light from array
			currentLight = new Light(lightObject); //Create new light object
			if (currentLight != null) System.out.println("Light object \"" + currentLight.getName() + "\" created.");
			lightList.add(currentLight);
		}
		
		numLights = lightList.size();
		lights = new Light[numLights];
		//Put lights from lightList into a Light array for easier access
		for (int i = 0; i < numLights; i++) lights[i] = lightList.get(i);
		System.out.println("Added light \"" + name + "\" to group \"" + name + "\"");
	}
	
	public void turnOn(){
		LightFunctions.turnOn(selector);
	}
	
	public void turnOff(){
		LightFunctions.turnOff(selector);
	}
	//TODO: Figure out how inheritance works
	
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
}
