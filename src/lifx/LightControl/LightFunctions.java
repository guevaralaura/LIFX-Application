package lifx.LightControl;

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
	private static String token = "cd0ed572badca8064cbdb078695975aa730217c5c8ec578e65e639df50d585c8"; //Laura
	private static TreeMap<String, String> map;
	public Location[] Location;
	public int numLocations;
	public int numLights;
	
	public static void setColour(String selector, String hexColour){
		//"color" uses put request
		String colorData = "#" + hexColour;
		String command = "color=" + colorData;
		int responseCode = setState(selector, command);
	}
	
	public static void turnOn(String selector){
		int responseCode = setState(selector, "power=on");
	}
	
	public static void turnOff(String selector){
		int responseCode = setState(selector, "power=off");
	}
	
	/**Brightness level between 0.0 and 1.0*/
	public static void setBrightness(String selector, float brightnessLevel){
		setState(selector, "brightness=" + Float.toString(brightnessLevel));
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
			    //System.out.println(line);
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
	* Along with the properties for each light.
	* Selector must be either "all", or selector:id (eg: "group_id:e3608...2ae2a")
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

	/**Removes quotation marks from the first and last character of a given String*/
	public static String removeQuotes(String quoteString){
		if (quoteString.startsWith("\"") && quoteString.endsWith("\""))  return quoteString.substring(1, quoteString.length()-1);
		else return quoteString;
	}

}
