package mata.macrokey.util;


import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mata.macrokey.MacroKey;
import mata.macrokey.object.BoundKey;
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

    private File file;

    public JsonConfig(File file){
        this.file = new File(file.getParent()+"/macrokey/keybindings.json");
        MacroKey.instance.boundKeys = new ArrayList();

        File dir = new File(file.getParent()+"/macrokey/");
        if(!dir.exists()){
            dir.mkdir();
        }

        if(!this.file.exists()){
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
            root = parser.parse(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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

    public void saveKeybinding(){
        try {
            Writer writer = new FileWriter(file);
            Gson gson = new GsonBuilder().create();
            gson.toJson(MacroKey.instance.boundKeys.toArray(new BoundKey[0]), writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addKeybinding(BoundKey boundKey){
        MacroKey.instance.boundKeys.add(boundKey);
        saveKeybinding();
    }

    public void convertConfig(){
        MacroKey.instance.configuration.load();
        Map<String, Property> map = Maps.newHashMap();

        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        for (Map.Entry<String, Property> entry : category.entrySet()) {
            addKeybinding(new BoundKey(Integer.parseInt(entry.getKey()), entry.getValue().getString(), false));
        }

    }

}
