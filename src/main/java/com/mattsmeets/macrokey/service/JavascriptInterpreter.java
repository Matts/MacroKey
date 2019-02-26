package com.mattsmeets.macrokey.service;

import com.mattsmeets.macrokey.api.*;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import javax.script.ScriptException;
import java.io.*;

public class JavascriptInterpreter {

    private Context engine;

    public JavascriptInterpreter(File file) throws IOException {
        engine = Context.create("js");

        Value value = engine.getBindings("js");
        value.putMember("Player", new PlayerAPI());
        value.putMember("Chat", new ChatAPI());
        value.putMember("Text", new TextAPI());
        value.putMember("State", new StateAPI());
        value.putMember("MacroKey", new MacroAPI());
        value.putMember("Keyboard", new KeyboardAPI());

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder("");
        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            try {
                reader.close();
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }
        }
        try {
            engine.eval("js", builder.toString());
        } catch (PolyglotException e) {
            e.printStackTrace();
        }

    }

    public Value eval(String code) throws FileNotFoundException, ScriptException {
        return engine.eval("js", code);
    }
}
