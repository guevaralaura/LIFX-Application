package lifx.LightControl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Light {
	//Light class will control individual lights
	private String id;
	private String label;
	
	public Light(JsonObject light){
		id = removeQuotes(light.get("id").toString());
		label = removeQuotes(light.get("label").toString());
		
	}
	
	private String removeQuotes(String quoteString){ //TODO: Fix this, interfaces? Use with everything so public to Location, Group, Light in LightFunctions, but not to outside LightFunctions
		if (quoteString.startsWith("\"") && quoteString.endsWith("\""))  return quoteString.substring(1, quoteString.length()-1);
		else return quoteString;
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