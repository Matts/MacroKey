package com.mattsmeets.macrokey.factory;

import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.JSCommand;
import com.mattsmeets.macrokey.model.StringCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommandFactory {
    public enum CommandType {
        STRING("string"), JAVASCRIPT("javascript");

        private String id;

        CommandType(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public static CommandType valueOfId(String id) {
            Optional item = Arrays.stream(values()).filter(type -> type.id.equalsIgnoreCase(id)).findFirst();
            return item.isPresent() ? (CommandType) item.get() : null;
        }
    }

    public static Map<String, Class> supportedTypes = new HashMap<>();

    static {
        supportedTypes.put(CommandType.STRING.id, StringCommand.class);
        supportedTypes.put(CommandType.JAVASCRIPT.id, JSCommand.class);
    }

    public static CommandInterface create(String type, String command) {
        try {
            Constructor cons = supportedTypes.get(type).getDeclaredConstructor(String.class);
            cons.setAccessible(true);
            return (CommandInterface) cons.newInstance(command);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
