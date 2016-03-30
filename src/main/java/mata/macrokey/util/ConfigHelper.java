package mata.macrokey.util;

import com.google.common.collect.Maps;
import mata.macrokey.MacroKey;
import mata.macrokey.object.CommandBinding;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Property;

import java.util.Map;

/**
 * Created by Matt on 3/30/2016.
 */
public class ConfigHelper {

    public static Map<String,Property> getKeysFromConfig(){
        MacroKey.instance.configuration.load();
        Map<String, Property> map = Maps.newHashMap();

        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        for (Map.Entry<String, Property> entry : category.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    public static boolean doesBindingExist(int key){
        Map<String, Property> map = getKeysFromConfig();

        for (Map.Entry<String, Property> entry : map.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(key+"")){
                return true;
            }
        }
        return false;
    }

    public static void removeCommand(int key){
        ConfigCategory category = MacroKey.instance.configuration.getCategory("bindings");
        Map<String, Property> map = getKeysFromConfig();

        for (Map.Entry<String, Property> entry : map.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(key+"")){
                map.entrySet().remove(entry);
            }
        }

        MacroKey.instance.configuration.removeCategory(category);

        for (Map.Entry<String, Property> entry : map.entrySet()) {
            MacroKey.instance.configuration.get("bindings", entry.getKey()+"", entry.getValue().getString());
            MacroKey.instance.configuration.save();
        }

        MacroKey.instance.configuration.save();

        CommandBinding.loadKeybindings();
    }

}
