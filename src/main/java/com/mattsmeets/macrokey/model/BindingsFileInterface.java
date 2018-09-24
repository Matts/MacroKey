package com.mattsmeets.macrokey.model;

import java.util.Set;
import java.util.UUID;

public interface BindingsFileInterface {

    /**
     * @return version of the file
     */
    public int getVersion();

    /**
     * @param version of the file
     */
    public void setVersion(int version);

    /**
     * @return Set of Macro's
     */
    public Set<MacroInterface> getMacros();

    /**
     * @param macros Set of macro's
     */
    public void setMacros(Set<MacroInterface> macros);

    /**
     * @param macro macro to add
     */
    public void addMacro(MacroInterface macro);

    /**
     * @return Set of Layer's
     */
    public Set<LayerInterface> getLayers();

    /**
     * @param layers Set of Layer's
     */
    public void setLayers(Set<LayerInterface> layers);

    /**
     * @param layer layer to add
     */
    public void addLayer(LayerInterface layer);

    /**
     * @return Active layer UUID
     */
    public UUID getActiveLayer();

    /**
     * @param activeLayer UUID of the new active layer
     */
    public void setActiveLayer(UUID activeLayer);
}
