package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;

public class Layer implements LayerInterface {

    private String displayName;
    private Set<Macro> macros;

    public Layer(String displayName, Set<Macro> macros) {
        this.displayName = displayName;
        this.macros = macros;
    }

    public Layer(String displayName) {
        this(displayName, new HashSet<>());
    }

    public String getDisplayName() {
        return displayName;
    }

    public Layer setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public Set<Macro> getMacros() {
        return macros;
    }

    public Layer setMacros(Set<Macro> macros) {
        this.macros = macros;

        return this;
    }

    public Layer addMacro(Macro macro) {
        this.macros.add(macro);

        return this;
    }
}
