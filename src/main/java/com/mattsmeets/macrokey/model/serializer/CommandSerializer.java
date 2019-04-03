package com.mattsmeets.macrokey.model.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mattsmeets.macrokey.model.command.StringCommand;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CommandSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {

    private final Map<String, Class> supportedTypes;

    public CommandSerializer() {
        this.supportedTypes = new HashMap<>();
        this.supportedTypes.put("string", StringCommand.class);
    }

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        final JsonElement type = json.getAsJsonObject().get("type");
        if (type == null || !this.supportedTypes.containsKey(type.getAsString())) {
            throw new JsonParseException("Could not parse command: " + json.toString());
        }

        return context.deserialize(json, this.supportedTypes.get(type.getAsString()));
    }

    @Override
    public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.toString());
    }
}
