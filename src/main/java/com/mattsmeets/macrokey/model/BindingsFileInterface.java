package com.mattsmeets.macrokey.model;

import java.util.Set;
import java.util.UUID;

public interface BindingsFileInterface {

    /**
     * @return version of the file
     */
    int getVersion();

    /**
     * @param version of the file
     */
    void setVersion(int version);

    /**
     * @return Set of Macro's
     */
    Set<MacroInterface> getMacros();

    /**
     * @param macros Set of macro's
     */
    void setMacros(Set<MacroInterface> macros);

    /**
     * @param macro macro to add
     */
    void addMacro(MacroInterface macro);

    /**
     * @return Set of Layer's
     */
    Set<LayerInterface> getLayers();

    /**
     * @param layers Set of Layer's
     */
    void setLayers(Set<LayerInterface> layers);

    /**
     * @param layer layer to add
     */
    void addLayer(LayerInterface layer);

    /**
     * @return Active layer UUID
     */
    UUID getActiveLayer();

    /**
     * @param activeLayer UUID of the new active layer
     */
    void setActiveLayer(UUID activeLayer);
}
