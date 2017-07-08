package lifx.LightControl;

import com.google.gson.JsonObject;

import lifx.LightFunctions;

public class Light implements LifxController {
	//Light class will control individual lights
	private String id;
	private String label;
	
	/**Requires a JsonObject containing only the light information to create.*/
	public Light(JsonObject light){
		id = removeQuotes(light.get("id").toString());
		label = removeQuotes(light.get("label").toString());
	}
	
	
	private String removeQuotes(String quoteString){ //TODO: Fix this, interfaces? Use with everything so public to Location, Group, Light in LightFunctions, but not to outside LightFunctions
		if (quoteString.startsWith("\"") && quoteString.endsWith("\""))  return quoteString.substring(1, quoteString.length()-1);
		else return quoteString;
	}
	
	/**Turns on power for this specific light.*/
	public void turnOn(){
		LightFunctions.turnOn("id:" + id);
	}
	
	/**Turns off power for this specific light.*/
	public void turnOff(){
		LightFunctions.turnOff("id:" + id);
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