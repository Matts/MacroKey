package com.mattsmeets.macrokey.service;

import com.mattsmeets.macrokey.api.*;
import jdk.nashorn.api.scripting.ClassFilter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JavascriptInterpreter {

    private Context engine;

    public JavascriptInterpreter(ClassFilter allowedClasses) {
        engine = Context.create("js");
        Value bindings = engine.getBindings("js");

        bindings.putMember("Player", new PlayerAPI());
        bindings.putMember("Chat", new ChatAPI());
        bindings.putMember("Text", new TextAPI());
        bindings.putMember("State", new StateAPI());
        bindings.putMember("MacroKey", new MacroAPI());
        bindings.putMember("Keyboard", new KeyboardAPI());
    }

    public Context eval(File file) throws FileNotFoundException, ScriptException {
        engine.eval("js", new FileReader(file).toString());

        return engine;
    }
}
