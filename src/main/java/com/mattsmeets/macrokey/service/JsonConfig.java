package com.mattsmeets.macrokey.service;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonConfig {

    private File file;
    private JSONService jsonService;

    public JsonConfig(String parentFolder, String fileName) {
        this.file = new File(parentFolder + "/macrokey/" + fileName);

        this.jsonService = new JSONService();
    }

    public void initializeFile() throws IOException {
        File parentFolder = new File(this.file.getParent());

        if(!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        if(!this.file.exists()) {
            this.file.createNewFile();
        }
    }

    private JsonElement getJSONElement() throws IOException {
        return this.jsonService.loadJSONElementFromFile(file);
    }

    public JsonObject getJSONObject() throws IOException {
        JsonElement jsonElement = getJSONElement();

        if(jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }

        return null;
    }

    public <T> T bindJsonToObject(Class<T> classToBind) throws IOException {
        return bindJsonElementToObject(classToBind, getJSONElement());
    }

    public <T> T bindJsonElementToObject(Class<T> classToBind, JsonElement element) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, classToBind);
    }

    public void saveObjectToJson(JsonElement element) throws IOException {
        this.jsonService.saveJSONElementToFile(element, this.file);
    }
}
