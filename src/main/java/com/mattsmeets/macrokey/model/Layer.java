package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Layer implements LayerInterface {

    private final UUID ulid;
    private String displayName;
    private Set<UUID> macros;

    public Layer(UUID ulid, String displayName, Set<UUID> macros) {
        this.ulid = ulid;
        this.displayName = displayName;
        this.macros = macros;
    }

    public Layer(UUID ulid, String displayName) {
        this(ulid, displayName, new HashSet<>());
    }

    public Layer(String displayName) {
        this(UUID.randomUUID(), displayName, new HashSet<>());
    }

    public Layer() {
        this("");
    }

    public String getDisplayName() {
        return displayName;
    }

    public Layer setDisplayName(String displayName) {
        this.displayName = displayName;

        return this;
    }

    public Set<UUID> getMacros() {
        return macros;
    }

    public Layer setMacros(Set<UUID> macros) {
        this.macros = macros;

        return this;
    }

    public Layer addMacro(UUID macro) {
        this.macros.add(macro);

        return this;
    }

    public Layer addMacro(MacroInterface macro) {
        return this.addMacro(macro.getUMID());
    }

    public Layer removeMacro(UUID macro) {
        this.macros.remove(macro);

        return this;
    }

    public Layer removeMacro(MacroInterface macro) {
        return this.removeMacro(macro.getUMID());
    }

    public UUID getULID() {
        return ulid;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof LayerInterface
                && this.ulid.equals(((LayerInterface) obj).getULID());
    }
}
