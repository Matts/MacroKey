package com.mattsmeets.macrokey.service;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.api.ChatAPI;
import com.mattsmeets.macrokey.api.PlayerAPI;
import com.mattsmeets.macrokey.api.TextAPI;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minecraft.util.text.TextFormatting;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Base64;

public class JavascriptInterpreter {

    private ScriptEngine engine;

    public JavascriptInterpreter(ClassFilter allowedClasses) {
        engine = new NashornScriptEngineFactory().getScriptEngine(allowedClasses);

        try {
            engine.eval(new String(Base64.getDecoder().decode("dmFyIFBsYXRmb3JtID0gSmF2YS50eXBlKCJqYXZhZnguYXBwbGljYXRpb24uUGxhdGZvcm0iKTsKdmFyIFRpbWVyICAgID0gSmF2YS50eXBlKCJqYXZhLnV0aWwuVGltZXIiKTsKZnVuY3Rpb24gc2V0VGltZXJSZXF1ZXN0KGhhbmRsZXIsIGRlbGF5LCBpbnRlcnZhbCwgYXJncykgewogICAgaGFuZGxlciA9IGhhbmRsZXIgfHwgZnVuY3Rpb24oKSB7fTsKICAgIGRlbGF5ID0gZGVsYXkgfHwgMDsKICAgIGludGVydmFsID0gaW50ZXJ2YWwgfHwgMDsKICAgIHZhciBhcHBseUhhbmRsZXIgPSBmdW5jdGlvbigpIGhhbmRsZXIuYXBwbHkodGhpcywgYXJncyk7CiAgICB2YXIgcnVuTGF0ZXIgPSBmdW5jdGlvbigpIFBsYXRmb3JtLnJ1bkxhdGVyKGFwcGx5SGFuZGxlcik7CiAgICB2YXIgdGltZXIgPSBuZXcgVGltZXIoInNldFRpbWVyUmVxdWVzdCIsIHRydWUpOwogICAgaWYgKGludGVydmFsID4gMCkgewogICAgICAgIHRpbWVyLnNjaGVkdWxlKHJ1bkxhdGVyLCBkZWxheSwgaW50ZXJ2YWwpOwogICAgfSBlbHNlIHsKICAgICAgICB0aW1lci5zY2hlZHVsZShydW5MYXRlciwgZGVsYXkpOwogICAgfQogICAgcmV0dXJuIHRpbWVyOwp9CmZ1bmN0aW9uIGNsZWFyVGltZXJSZXF1ZXN0KHRpbWVyKSB7CiAgICB0aW1lci5jYW5jZWwoKTsKfQpmdW5jdGlvbiBzZXRJbnRlcnZhbCgpIHsKICAgIHZhciBhcmdzID0gQXJyYXkucHJvdG90eXBlLnNsaWNlLmNhbGwoYXJndW1lbnRzKTsKICAgIHZhciBoYW5kbGVyID0gYXJncy5zaGlmdCgpOwogICAgdmFyIG1zID0gYXJncy5zaGlmdCgpOwogICAgcmV0dXJuIHNldFRpbWVyUmVxdWVzdChoYW5kbGVyLCBtcywgbXMsIGFyZ3MpOwp9CmZ1bmN0aW9uIGNsZWFySW50ZXJ2YWwodGltZXIpIHsKICAgIGNsZWFyVGltZXJSZXF1ZXN0KHRpbWVyKTsKfQpmdW5jdGlvbiBzZXRUaW1lb3V0KCkgewogICAgdmFyIGFyZ3MgPSBBcnJheS5wcm90b3R5cGUuc2xpY2UuY2FsbChhcmd1bWVudHMpOwogICAgdmFyIGhhbmRsZXIgPSBhcmdzLnNoaWZ0KCk7CiAgICB2YXIgbXMgPSBhcmdzLnNoaWZ0KCk7CgogICAgcmV0dXJuIHNldFRpbWVyUmVxdWVzdChoYW5kbGVyLCBtcywgMCwgYXJncyk7Cn0KZnVuY3Rpb24gY2xlYXJUaW1lb3V0KHRpbWVyKSB7CiAgICBjbGVhclRpbWVyUmVxdWVzdCh0aW1lcik7Cn0KZnVuY3Rpb24gc2V0SW1tZWRpYXRlKCkgewogICAgdmFyIGFyZ3MgPSBBcnJheS5wcm90b3R5cGUuc2xpY2UuY2FsbChhcmd1bWVudHMpOwogICAgdmFyIGhhbmRsZXIgPSBhcmdzLnNoaWZ0KCk7CgogICAgcmV0dXJuIHNldFRpbWVyUmVxdWVzdChoYW5kbGVyLCAwLCAwLCBhcmdzKTsKfQpmdW5jdGlvbiBjbGVhckltbWVkaWF0ZSh0aW1lcikgewogICAgY2xlYXJUaW1lclJlcXVlc3QodGltZXIpOwp9")));
        } catch (ScriptException e) {
            e.printStackTrace();
            MacroKey.instance.logger.err("Could not polyfill, send this to the author : " + e.getMessage());
        }
        engine.put("Player", new PlayerAPI());
        engine.put("Chat", new ChatAPI());
        engine.put("Text", new TextAPI());
        engine.put("TextColor", new TextAPI.Color());
        engine.put("TextFormat", new TextAPI.Format());
    }

    public Invocable eval(File file) throws FileNotFoundException, ScriptException {
        engine.eval(new FileReader(file));

        return (Invocable) engine;
    }
}
