package lifx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;




public class LightFunctions {
	private static String token = "c8a6bd4449506916025d6b4e924e672e3b898af8520d2ddbe5ab075ff8d737e9";
	public TreeMap<String, String> jsonData = getInfo();
	
	public static TreeMap<String, String>  getInfo(){
		String Lifxdata = listLights("all");
		System.out.println(Lifxdata);
		JsonElement response = new JsonParser().parse(Lifxdata);
		JsonArray a = response.getAsJsonArray();
		Iterator<Entry<String, JsonElement>> o = a.get(0).getAsJsonObject().entrySet().iterator();
		TreeMap<String, String> t = new TreeMap<String, String>();
		
		while(o.hasNext()){
			Entry<String, JsonElement> element = o.next();
			String key = element.getKey();
			String val = element.getValue().toString();
			if(val.startsWith("\"") && val.endsWith("\"")){
				val = val.substring(1, val.length()-1);
			}
			t.put(key, val);
		}
		System.out.println(t);
	
		
		/*		JsonObject lifxObject = response.getAsJsonObject();
		String result = lifxObject.get("power").toString();
		System.out.println(result);*/

		return t;
	}
	
	public static void setColour(String hexColour){
		//"color" uses put request
		String colorData = "#" + hexColour;
		String command = "color=" + colorData;
		int responseCode = setState(command);
	}
	
	public static void turnon() {
		//"power" uses put request
		String command = "power=on";
		int responseCode = setState(command);
	}
	
	public static void turnoff() {
		//"power" uses put request
		String command = "power=off";
		int responseCode = setState(command);
	}
	
	private static int setState(String data){
		//String data in format "key=value". Eg) "power=on", "brightness=2.0", "color=#123456"
		int responseCode = 0;
		try {
			URL url = new URL ("https://api.lifx.com/v1/lights/all/state");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestMethod("PUT"); //Sets connection to PUT
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//responseCode = connection.getResponseCode(); //Gets response code from server, ensures good connection
			//if (responseCode > 200) return responseCode;
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
		
		System.out.println(responseCode);
		return responseCode;
	}
	
	
	private static String listLights(String selector){
		String ret = null;
		try {
			URL url = new URL ("https://api.lifx.com/v1/lights/all"); 
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", "Bearer " + token);
			connection.setRequestProperty("selector", selector);
			
			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				System.out.println("Response Code: " + responseCode);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			ret = response.toString();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}	
}
