package com.mattsmeets.macrokey.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.serializer.CommandSerializer;

import java.io.*;

public class JSONService {

    /**
     * JSON File Reader
     *
     * @param file A JSON file
     * @return The JSON contents of the give file
     * @throws IOException When the file is not found, or not readable/
     */
    public JsonElement loadJSONElementFromFile(File file) throws IOException {
        JsonParser parser = new JsonParser();
        return parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }

    public void saveJSONElementToFile(JsonElement element, File file) throws IOException {
        Writer writer = new FileWriter(file);

        Gson gson = new GsonBuilder().create();
        gson.toJson(element, writer);
        writer.close();
    }

    public <T> void saveObjectsToFile(T element, File file) throws IOException {
        System.out.println("saveObjToFile");
        Writer writer = new FileWriter(file);

        Gson gson = new GsonBuilder()
                .create();
        gson.toJson(element, writer);
        writer.close();
    }
}
