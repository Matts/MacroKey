package com.mattsmeets.macrokey.factory;

import com.mattsmeets.macrokey.model.AbstractCommand;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.JSCommand;
import com.mattsmeets.macrokey.model.StringCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandFactory {

    public static Map<String, Class> supportedTypes = new HashMap<>();

    static {
        supportedTypes.put("string", StringCommand.class);
        supportedTypes.put("javascript", JSCommand.class);
    }

    public static CommandInterface create(String type, String arg) {
        try {
            Constructor cons = supportedTypes.get(type).getDeclaredConstructor(String.class);
            cons.setAccessible(true);
            return (CommandInterface) cons.newInstance(arg);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
