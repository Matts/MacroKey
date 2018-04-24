package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Layer implements LayerInterface {

    private UUID ulid;
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

    public Layer addMacro(Macro macro) {
        return this.addMacro(macro.getUMID());
    }

    public UUID getULID() {
        return ulid;
    }

    public Layer setULID(UUID ulid) {
        this.ulid = ulid;

        return this;
    }
}
