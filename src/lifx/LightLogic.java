package lifx;

import java.io.*;

public class LightLogic {
    private static String scriptName = "light_logic.py";

    public static void getData() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"get");
        pb.start();


        //check file exists
        File tmpDir = new File("data.json");
        boolean exists = tmpDir.exists();
        //add delay to wait until file exists : later look how to optimize python code
        while(!exists){
            //System.out.println("loading");
            exists = tmpDir.exists();
        }
        //read from file
    }

    public void setPower(String powerStr, int id) throws IOException {
        if(id==-1){
            ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"set_power", powerStr);
            pb.start();
        }else {
            String selector = HomeController.lightsList.get(id).get("label").getAsString();
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_power", powerStr, selector);
            pb.start();
        }
    }

    public static void turnOff(int id) throws IOException {
        if(id==-1){
            ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"set_power", "off");
            pb.start();
        }else {
            String selector = HomeController.lightsList.get(id).get("label").getAsString();
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_power", "off", selector);
            pb.start();
            //Process p = pb.start();
            // BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //String ret =in.readLine();
            //System.out.println();
        }
    }

    public static void turnOn(int id) throws IOException {
        if(id==-1){
            ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"set_power", "on");
            pb.start();
        }else {
            String selector = HomeController.lightsList.get(id).get("label").getAsString();
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_power", "on", selector);
            pb.start();
        }
    }

    public static void setColor(String hexColor, int id) throws IOException {
        if(id==-1){
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_color", hexColor);
            pb.start();
        }else {
            String selector = HomeController.lightsList.get(id).get("label").getAsString();
            System.out.println(selector);
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_color", hexColor, selector);
            pb.start();
            //Process p = pb.start();
            //BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //String ret =in.readLine();
            //System.out.println(ret);
        }
    }

    public void setBrightness(double brightness, int id) throws IOException {
        String brightnessStr = String.valueOf(brightness);
        if(id==-1){
            ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"set_brightness", brightnessStr);
            pb.start();
        }else {
            String selector = HomeController.lightsList.get(id).get("label").getAsString();
            ProcessBuilder pb = new ProcessBuilder("python3", scriptName, "set_brightness", brightnessStr, selector);
            pb.start();
        }
    }

    /*public void setAll(String powerStr, String hexColor, double brightness, int id) throws IOException {
        String selector = HomeController.lightsList.get(id).get("label").getAsString();
        String brightnessStr = String.valueOf(brightness);
        ProcessBuilder pb = new ProcessBuilder("python3",scriptName,"set_all", powerStr, hexColor, brightnessStr, selector);
        pb.start();
    }*/

}
