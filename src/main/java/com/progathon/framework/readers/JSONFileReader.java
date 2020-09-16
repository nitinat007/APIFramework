package com.progathon.framework.readers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Author: nitinkumar
 * Created Date: 30/04/20
 * Info: Utils to read JSON file
 **/

public class JSONFileReader {
    public JsonObject readJSONFiles(String fileName) throws FileNotFoundException {

        String filePath = System.getProperty("user.dir") + fileName;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = new JsonObject();


        JsonElement jsonElement = parser.parse(new FileReader(filePath));
        jsonObject = jsonElement.getAsJsonObject();

        //convert Object to JSONObject

        return jsonObject;
    }
}
