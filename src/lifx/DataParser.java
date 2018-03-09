package lifx;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataParser {

    public static List<JsonObject> getLights() {
        List<JsonObject> list = null;
        try {
            String data = new String(Files.readAllBytes(Paths.get("data.json")));
            JsonParser parser = new JsonParser();
            JsonArray obj = (JsonArray) parser.parse(data);
            list = listMaker(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static List<JsonObject> listMaker(JsonArray array) {
        List<JsonObject> lightsList = new ArrayList();
        for (int i = 0; i < array.size(); i++) {
            JsonObject light = array.get(i).getAsJsonObject();
            lightsList.add(light);
        }
        return lightsList;
    }
}