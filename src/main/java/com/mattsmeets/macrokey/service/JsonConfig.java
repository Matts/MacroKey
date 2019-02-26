package com.mattsmeets.macrokey.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.serializer.CommandSerializer;

import java.io.File;
import java.io.IOException;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class JsonConfig {

    private File file;
    private JSONService jsonService;

    public JsonConfig(String fileName) throws IOException {
        this.file = instance.javascriptFileHelper.getFile("/" + fileName);
        instance.javascriptFileHelper.makeFile(this.file);

        this.jsonService = new JSONService();
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
                .registerTypeAdapter(CommandInterface.class, new CommandSerializer())
                .create()
                .fromJson(element, classToBind);
    }

    public <T> void saveObjectToJson(T object) throws IOException {
        this.jsonService.saveObjectsToFile(object, this.file);
    }
}
