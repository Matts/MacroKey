package com.mattsmeets.macrokey.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BindingsFile implements BindingsFileInterface {

    private int version;
    private Set<MacroInterface> macros;
    private Set<LayerInterface> layers;
    private UUID activeLayer;

    public BindingsFile(int version, Set<MacroInterface> macros, Set<LayerInterface> layers) {
        this.version = version;
        this.macros = macros;
        this.layers = layers;
    }

    public BindingsFile(int version, Set<MacroInterface> macros) {
        this(version, macros, new HashSet<>());
    }

    public BindingsFile(int version) {
        this(version, new HashSet<>(), new HashSet<>());
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

    public Set<LayerInterface> getLayers() {
        return layers;
    }

    public void setLayers(Set<LayerInterface> layers) {
        this.layers = layers;
    }

    public void addLayer(LayerInterface layer) {
        this.layers.add(layer);
    }

    public UUID getActiveLayer() {
        return activeLayer;
    }

    public void setActiveLayer(UUID activeLayer) {
        this.activeLayer = activeLayer;
    }
}
