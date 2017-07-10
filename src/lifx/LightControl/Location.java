package lifx.LightControl;

import lifx.LightFunctions;
import lifx.LightControl.Group;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Location {
	//Location class will house Group class and allow controls to entire location
	private String id;
	private String name;
	public Group[] groups;
	public int numLights;
	public int numGroups;
	
	public Location(String idIn, String nameIn){
		//Remove quotation marks from strings
		if(idIn.startsWith("\"") && idIn.endsWith("\"")) idIn = idIn.substring(1, idIn.length()-1);
		if(nameIn.startsWith("\"") && nameIn.endsWith("\"")) nameIn = nameIn.substring(1, nameIn.length()-1);
		id = idIn;
		name = nameIn;
		
		String lifxLocationString = LightFunctions.listLights("location_id:" + id);
		System.out.println(lifxLocationString);
		
		
		JsonElement lifxJsonElement = new JsonParser().parse(lifxLocationString); //Holds everything
		JsonArray lightArray = lifxJsonElement.getAsJsonArray(); //Also everything
		//Lifx Json returns an array with each individual light in it
		
		//Put group objects in an ArrayList for first time (because dynamic)
		ArrayList<Group> groupList = new ArrayList<Group>(); //Contains type lifx.LightControl.group for each location
		JsonObject lightObject;
		JsonObject lightLocation;
		String currentId;
		
		int cont = 0;
		
		numLights = lightArray.size(); //Number of lights in location
		//Go through each light, find its location, and create that location as an object
		for (int i = 0; i < numLights; i++){
			
			lightObject = lightArray.get(i).getAsJsonObject();
			lightLocation = lightObject.get("group").getAsJsonObject();
			currentId = lightLocation.get("id").toString();
			
			int locationListSize = groupList.size();
			for (int j = 0; j < locationListSize; j++)
				//If location is already accounted for then 
				if (currentId.equals(((Group) groupList.get(j)).getId())) { 
					cont = 1;
					break;
				}
			if (cont == 1) continue;
			
			//Creates a new Group class for the current group
			Group currentGroup = new Group(currentId,lightLocation.get("name").toString());
			if (currentGroup != null) System.out.println("Group object \"" + currentGroup.getName() + "\" created.");
			groupList.add(currentGroup);
		}
		numGroups = groupList.size();
		groups = new Group[groupList.size()];
		//Put ArrayList into array. Efficiency?
		for (int i = 0; i < groupList.size(); i++) groups[i] = groupList.get(i);
		System.out.println("Added location: " + name);
		
		groupList = null;
		lifxJsonElement = null;
		
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
	
	public String getName(){
		return name;
	}
}
