package com.mattsmeets.macrokey.service;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mattsmeets.macrokey.model.command.CommandInterface;
import com.mattsmeets.macrokey.model.serializer.CommandSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class JsonConfig {
    private static final Logger LOGGER = LogManager.getLogger();

    private File file;
    private JsonService jsonService;

    public JsonConfig(String parentFolder, String fileName) {
        this.file = new File(parentFolder + "/macrokey/" + fileName);

        this.jsonService = new JsonService();
    }

    public void initializeFile() throws IOException {
        final File parentFolder = new File(this.file.getParent());

        if (!parentFolder.exists()) {
            final boolean result = parentFolder.mkdirs();
            if (!result) {
                LOGGER.error("Create folder failed for : " + file.getAbsolutePath());
            }
        }

        if (!this.file.exists()) {
            final boolean result = this.file.createNewFile();
            if (!result) {
                LOGGER.error("Create folder failed for : " + file.getAbsolutePath());
            }
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
