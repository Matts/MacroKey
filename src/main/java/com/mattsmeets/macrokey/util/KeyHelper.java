package com.mattsmeets.macrokey.util;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.input.Keyboard;

import java.util.Map;

/**
 * Created by Matt on 3/30/2016.
 */
public class KeyHelper {

    public static String getKeyFromInt(int key){
        Map<String, Integer> keyMap = (Map<String, Integer>) ReflectionHelper.getPrivateValue(Keyboard.class, null, "keyMap");
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            if (key == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Integer getIntFromKey(String key){
        Map<String, Integer> keyMap = (Map<String, Integer>) ReflectionHelper.getPrivateValue(Keyboard.class, null, "keyMap");
        for (Map.Entry<String, Integer> entry : keyMap.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
