package com.mattsmeets.macrokey.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

public class JsonConfig {

    private File file;
    private JSONService jsonService;

    public JsonConfig(String parentFolder, String fileName) {
        this.file = new File(parentFolder + "/macrokey/" + fileName);

        this.jsonService = new JSONService();
    }

    public void initializeFile() throws IOException {
        File parentFolder = new File(this.file.getParent());

        if (!parentFolder.exists()) {
            parentFolder.mkdirs();
        }

        if (!this.file.exists()) {
            this.file.createNewFile();
        }
    }

    private JsonElement getJSONElement() throws IOException {
        return this.jsonService.loadJSONElementFromFile(file);
    }

    public JsonObject getJSONObject() throws IOException {
        JsonElement jsonElement = getJSONElement();

        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject();
        }

        return null;
    }

    public <T> T bindJsonToObject(Class<T> classToBind) throws IOException {
        return bindJsonElementToObject(classToBind, getJSONElement());
    }

    public <T> T bindJsonElementToObject(Class<T> classToBind, JsonElement element) {
        return new GsonBuilder()
                .create()
                .fromJson(element, classToBind);
    }

    public <T> void saveObjectToJson(T object) throws IOException {
        this.jsonService.saveObjectsToFile(object, this.file);
    }
}
