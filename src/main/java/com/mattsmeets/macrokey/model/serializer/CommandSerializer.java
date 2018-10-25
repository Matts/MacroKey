package com.mattsmeets.macrokey.model.serializer;

import com.google.gson.*;
import com.mattsmeets.macrokey.model.AbstractCommand;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.StringCommand;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CommandSerializer<CommandInterface> implements JsonSerializer<CommandInterface>, JsonDeserializer<CommandInterface> {

    private Map<String, Class> supportedTypes;

    public CommandSerializer() {
        this.supportedTypes = new HashMap<>();
        this.supportedTypes.put("string", StringCommand.class);
    }

    @Override
    public CommandInterface deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement type = json.getAsJsonObject().get("type");

        if(type == null || !this.supportedTypes.containsKey(type.getAsString())) {
            throw new JsonParseException("Could not parse command: " + json.toString());
        }

        return context.deserialize(json, this.supportedTypes.get(type.getAsString()));
    }

    @Override
    public JsonElement serialize(CommandInterface src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString());
    }
}
