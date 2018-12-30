package com.mattsmeets.macrokey.model.serializer;

import com.google.gson.*;
import com.mattsmeets.macrokey.factory.CommandFactory;
import com.mattsmeets.macrokey.model.AbstractCommand;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.JSCommand;
import com.mattsmeets.macrokey.model.StringCommand;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CommandSerializer<T extends CommandInterface> implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement type = json.getAsJsonObject().get("type");

        if(type == null || !CommandFactory.supportedTypes.containsKey(type.getAsString())) {
            throw new JsonParseException("Could not parse command: " + json.toString());
        }

        T command = context.deserialize(json, CommandFactory.supportedTypes.get(type.getAsString()));
        command.setup();

        return command;
    }

    @Override
    public JsonElement serialize(CommandInterface src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString());
    }
}
