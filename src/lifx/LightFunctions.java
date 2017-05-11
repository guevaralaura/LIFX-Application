package lifx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LightFunctions {

	public static void setColour(String hexColour){
		String colorData = "#" + hexColour;
		String command = "color=" + colorData;
		sendData(command);
	}
	
	public static void turnon() {
		String command = "power=on";
		sendData(command);
	}
	
	public static void turnoff() {
		String command = "power=off";
		sendData(command);
	}
	
	private static void sendData(String data){
		HttpURLConnection connection = createConnectionPut();
		try {
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
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private static HttpURLConnection createConnectionPut(){
		HttpURLConnection connection = null;
		try {
			String authStr = "c8a6bd4449506916025d6b4e924e672e3b898af8520d2ddbe5ab075ff8d737e9";
			URL url = new URL ("https://api.lifx.com/v1/lights/all/state");
			
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Bearer " + authStr);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
}
