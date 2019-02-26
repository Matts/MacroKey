package com.mattsmeets.macrokey.model.serializer;

import com.google.gson.*;
import com.mattsmeets.macrokey.model.CommandInterface;
import com.mattsmeets.macrokey.model.StringCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommandSerializerTest {

    @InjectMocks
    public CommandSerializer<CommandInterface> commandSerializer;

    @Mock
    public JsonSerializationContext serializationContext;

    @Mock
    public JsonDeserializationContext deserializationContext;

    @Test
    public void testSerializeWillConvertString() {
        StringCommand command = new StringCommand("", "Magic String");
        StringCommand commandSpy = spy(command);

        JsonObject obj = new JsonObject();
        obj.addProperty("test", "testValue");

        when(serializationContext.serialize(any())).thenReturn(obj);

        JsonElement result = commandSerializer.serialize(commandSpy, CommandInterface.class, serializationContext);

        verify(serializationContext).serialize("Magic String");

        assertEquals("{\"test\":\"testValue\"}", result.toString());
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWillThrowErrorWhenTypeNotFound() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "someRandomNotExistingValue");

        commandSerializer.deserialize(obj, CommandInterface.class, deserializationContext);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserializeWillThrowErrorWhenTypeNull() {
        JsonObject obj = new JsonObject();

        commandSerializer.deserialize(obj, CommandInterface.class, deserializationContext);
    }

    @Test
    public void testDeserializeWillDeserializeStringType() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "string");
        obj.addProperty("command", "SomeRandomCommand");

        when(deserializationContext.deserialize(any(), any())).thenReturn(new StringCommand("", "SomeRandomCommand"));

        CommandInterface command = commandSerializer.deserialize(obj, CommandInterface.class, deserializationContext);

        assertEquals("string", command.getCommandType());
        assertEquals("SomeRandomCommand", command.toString());

        verify(deserializationContext).deserialize(obj, StringCommand.class);
    }

}
