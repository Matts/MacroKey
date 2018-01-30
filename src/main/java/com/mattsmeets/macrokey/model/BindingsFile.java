package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;

public class BindingsFile {

    private int version;
    private Set<Macro> macros;

    public BindingsFile(int version, Set<Macro> macros) {
        this.version = version;
        this.macros = macros;
    }

    public BindingsFile(int version) {
        this(version, new HashSet<>());
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<Macro> getMacros() {
        return macros;
    }

    public void setMacros(Set<Macro> macros) {
        this.macros = macros;
    }

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }
}
