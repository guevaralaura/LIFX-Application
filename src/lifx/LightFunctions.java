package lifx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lifx.LightControl.*;

public class LightFunctions {
	//private static String token = "c8a6bd4449506916025d6b4e924e672e3b898af8520d2ddbe5ab075ff8d737e9";
	private static String token = "cd0ed572badca8064cbdb078695975aa730217c5c8ec578e65e639df50d585c8"; //Laura
	private static TreeMap<String, String> map;
	public Location[] locations;
	public int numLocations;
	public int numLights;
	
	public LightFunctions(){
		String lifxDataString = listLights("all");
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
		locations = new Location[locationList.size()];
		//Put ArrayList into array. Efficiency?
		for (int i = 0; i < locationList.size(); i++) locations[i] = (Location) locationList.get(i);
	}
	
	public static TreeMap<String, String>  getInfo(){
		String lifxData = listLights("all");
		//System.out.println(lifxData);
		JsonElement response = new JsonParser().parse(lifxData);
		JsonArray arr = response.getAsJsonArray();
		Iterator<Entry<String, JsonElement>> iterator = arr.get(0).getAsJsonObject().entrySet().iterator();
		TreeMap<String, String> map = new TreeMap<String, String>();
		
		while(iterator.hasNext()){
			Entry<String, JsonElement> element = iterator.next();
			String key = element.getKey();
			String val = element.getValue().toString();
			//splits string to get rid of quotation marks
			if(val.startsWith("\"") && val.endsWith("\"")){
				val = val.substring(1, val.length()-1);
			}
			map.put(key, val);
		}
		System.out.println(map);
	
		
		/*		JsonObject lifxObject = response.getAsJsonObject();
		String result = lifxObject.get("power").toString();
		System.out.println(result);*/

		return map;
	}
	
	public static void setColour(String hexColour){
		//"color" uses put request
		String colorData = "#" + hexColour;
		String command = "color=" + colorData;
		int responseCode = setState("all", command);
	}
	
	public static void turnOn() {
		//"power" uses put request
		int responseCode = setState("all", "power=on");
	}
	
	public static void turnOff() {
		//"power" uses put request
		int responseCode = setState("all", "power=off");
	}
	
	public static void turnOn(String selector){
		int responseCode = setState(selector, "power=on");
	}
	
	public static void turnOff(String selector){
		int responseCode = setState(selector, "power=off");
	}
	
	//legacy
	public static boolean isOn(){
		TreeMap<String, String> jsonData = LightFunctions.getInfo();
		if (jsonData.get("power").equalsIgnoreCase("on")){
			return true;
		}		
		return false;
	}
	
	private static int setState(String selector, String data){
		//String data in format "key=value". Eg) "power=on", "brightness=2.0", "color=#123456"
		int responseCode = 0;
		try {
			//Selector (all, location_id, group_name, etc.) is part of URL
			URL url = new URL ("https://api.lifx.com/v1/lights/" + selector + "/state");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestMethod("PUT"); //Sets connection to PUT
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(data);
			writer.flush();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while ((line = reader.readLine()) != null) {
			    System.out.println(line);
			}
			writer.close();
			reader.close();
			responseCode = connection.getResponseCode(); //Gets response code from server, ensures good connection
		} catch (IOException e){
			e.printStackTrace();
		}
		return responseCode;
	}

	/** listLights
	Will return a single JSON String containing each light for the given token.
	
	Selector must be either "all", or selector:id (eg: "group_id:e3608...2ae2a")
	*/
	public static String listLights(String selector){
		String ret = null;
		try {
			//Open connection to URL as GET with token
			URL url = new URL ("https://api.lifx.com/v1/lights/" + selector); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			
			
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				System.out.println("Error in listLights?: Response Code: " + responseCode);
			}

			//BufferedReader used to get Strings from server
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer(); //Must be used for .append so all JSON can be in 1 string

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			ret = response.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret; //All JSON data got from GET, in 1 line
	}	
}
