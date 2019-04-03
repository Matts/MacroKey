package com.mattsmeets.macrokey.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

class JsonService {
    /**
     * JSON File Reader
     *
     * @param file A JSON file
     * @return The JSON contents of the give file
     * @throws IOException When the file is not found, or not readable
     */
    JsonElement loadJSONElementFromFile(File file) throws IOException {
        return new JsonParser().parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }

    <T> void saveObjectsToFile(T element, File file) throws IOException {
        final Writer writer = new FileWriter(file);

        final Gson gson = new GsonBuilder().create();
        gson.toJson(element, writer);
        writer.close();
    }
}
