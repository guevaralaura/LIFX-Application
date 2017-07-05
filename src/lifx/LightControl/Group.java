package lifx.LightControl;

import lifx.LightFunctions;

public class Group {
	//Group class will house Lights and allow controls to entire group
	private String id;
	private String name;
	
	Group(String idIn, String nameIn){
		if(idIn.startsWith("\"") && idIn.endsWith("\"")) idIn = idIn.substring(1, idIn.length()-1);
		if(nameIn.startsWith("\"") && nameIn.endsWith("\"")) nameIn = nameIn.substring(1, nameIn.length()-1);
		id = idIn;
		name = nameIn;
		System.out.println("Added Group: " + name);
	}
	
	public void turnOn(){
		LightFunctions.turnOn("group_id:" + id);
	}
	
	public void turnOff(){
		LightFunctions.turnOff("group_id:" + id);
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
}
