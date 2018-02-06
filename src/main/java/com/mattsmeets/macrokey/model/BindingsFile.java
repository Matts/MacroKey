package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;

public class BindingsFile implements BindingsFileInterface {

    private int version;
    private Set<MacroInterface> macros;

    public BindingsFile(int version, Set<MacroInterface> macros) {
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

    public Set<MacroInterface> getMacros() {
        return macros;
    }

    public void setMacros(Set<MacroInterface> macros) {
        this.macros = macros;
    }

    public void addMacro(MacroInterface macro) {
        this.macros.add(macro);
    }
}
