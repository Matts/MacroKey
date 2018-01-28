package com.mattsmeets.macrokey.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import scala.actors.threadpool.Arrays;

import com.google.gson.JsonObject;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.service.JsonConfig;

public class BindingsRepository {

    private JsonConfig config;

    private ArrayList<Macro> macros;
    private int fileVersion;

    public BindingsRepository(JsonConfig jsonConfig) throws IOException {
        this.config = jsonConfig;
    }

    public ArrayList<Macro> findAllMacros(boolean sync) throws IOException {
        if (sync)
            sync();

        return this.macros;
    }

    public List<Macro> findMacroByKeycode(int keyCode) {
        return this
                .macros
                .stream()
                .filter(
                        (macro) ->
                                macro.getKeyCode() == keyCode
                                        && macro.isActive()
                )
                .collect(Collectors.toList());
    }

    public int findFileVersion() {
        return fileVersion;
    }

    private void sync() throws IOException {
        this.macros = null;

        JsonObject jsonObject = this.config.getJSONObject();

        Macro[] macroArray = this.config.bindJsonElementToObject(Macro[].class, jsonObject.get("macros"));
        this.macros = new ArrayList<Macro>(Arrays.asList(macroArray));
        this.fileVersion = jsonObject.get("version").getAsInt();
    }
}
