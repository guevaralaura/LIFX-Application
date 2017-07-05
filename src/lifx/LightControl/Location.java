package lifx.LightControl;

import lifx.LightFunctions;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Location {
	//Location class will house Group class and allow controls to entire location
	private String id;
	private String label;
	
	public Location(String idIn, String labelIn){
		//Remove quotation marks from strings
		if(idIn.startsWith("\"") && idIn.endsWith("\"")) idIn = idIn.substring(1, idIn.length()-1);
		if(labelIn.startsWith("\"") && labelIn.endsWith("\"")) labelIn = labelIn.substring(1, labelIn.length()-1);
		id = idIn;
		label = labelIn;
		
		String lifxLocationString = LightFunctions.listLights("location_id:" + id);
		System.out.println(lifxLocationString);
		
		//JsonElement lifxLocationElement = new JsonParser().parse(lifxLocationString);
		
		
		
		
		
		System.out.println("Added " + getId());
	}

	public void turnOn(){
		LightFunctions.turnOn("location_id:" + id);
	}
	
	public void turnOff(){
		LightFunctions.turnOff("location_id:" + id);
	}
	
	public String getId(){
		return id;
	}
	
	public String getLabel(){
		return label;
	}
}
