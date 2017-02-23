package com.mattsmeets.macrokey.util;


import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.object.BoundKey;
import com.mattsmeets.macrokey.object.Layer;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Matt on 4/1/2016.
 */
public class JsonConfig {

    private static File keybindingFile;
    private static File layerFile;

    public JsonConfig(File file){
        this.keybindingFile = new File(file.getParent()+"/macrokey/keybindings.json");
        this.layerFile = new File(file.getParent()+"/macrokey/layers.json");


        MacroKey.instance.boundKeys = new ArrayList<BoundKey>();
        MacroKey.instance.layers = new ArrayList<Layer>();


        File dir = new File(file.getParent()+"/macrokey/");
        if(!dir.exists()){
            dir.mkdir();
        }

        FileHelper.fileExist(keybindingFile);
        FileHelper.fileExist(layerFile);


        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        if(!category.isEmpty()){
            LogHelper.info("Old Configuration File Detected! Converting...");
            convertConfig();
            MacroKey.instance.configuration.removeCategory(category);
            MacroKey.instance.configuration.save();
        }
    }



    public void loadKeybindings() {
        MacroKey.instance.boundKeys.clear();

        JsonParser parser = new JsonParser();
        JsonElement root = null;
        try {
            root = parser.parse(new InputStreamReader(new FileInputStream(keybindingFile), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (root.isJsonArray()) {
            Gson gson = new GsonBuilder().create();
            BoundKey[] r = gson.fromJson(root, BoundKey[].class);
            MacroKey.instance.boundKeys.addAll(Arrays.asList(r));
        }
    }

    public void loadLayers(){
        MacroKey.instance.layers.clear();

        JsonParser parser = new JsonParser();
        JsonElement root = null;
        try {
            root = parser.parse(new InputStreamReader(new FileInputStream(layerFile), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (root.isJsonArray()) {
            Gson gson = new GsonBuilder().create();
            Layer[] r = gson.fromJson(root, Layer[].class);
            MacroKey.instance.layers.addAll(Arrays.asList(r));
        }
    }

    public static void saveKeybinding(){
        try {
            Writer writer = new FileWriter(keybindingFile);
            Gson gson = new GsonBuilder().create();
            gson.toJson(MacroKey.instance.boundKeys.toArray(new BoundKey[0]), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLayers(){
        try {
            Writer writer = new FileWriter(layerFile);
            Gson gson = new GsonBuilder().create();
            gson.toJson(MacroKey.instance.layers.toArray(new Layer[0]), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Provide comparability for older versions
     */
    public void convertConfig(){
        MacroKey.instance.configuration.load();
        Map<String, Property> map = Maps.newHashMap();

        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        for (Map.Entry<String, Property> entry : category.entrySet()) {
            BoundKey.addKeybinding(new BoundKey(Integer.parseInt(entry.getKey()), entry.getValue().getString(), false, true));
        }
    }

}

