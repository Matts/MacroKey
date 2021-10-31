package com.mattsmeets.macrokey.model;

import java.util.Set;
import java.util.UUID;

public interface LayerInterface {

    /**
     * Get the displayName that should be rendered
     *
     * @return display name, user given name
     */
    String getDisplayName();

    /**
     * Set the displayName that the macro should have
     *
     * @param displayName displayName to set
     * @return the current Layer instance
     */
    Layer setDisplayName(String displayName);

    /**
     * Get the macros bound to the layer
     *
     * @return Set of macros
     */
    Set<UUID> getMacros();

    /**
     * Set the macros the Layer should have
     *
     * @param macros the macros to have
     * @return the current Layer instance
     */
    Layer setMacros(Set<UUID> macros);

    /**
     * Add a macro the Layer should have
     *
     * @param macro a macro to have
     * @return the current Layer instance
     */
    Layer addMacro(UUID macro);

    /**
     * Add a macro the Layer should have
     *
     * @param macro a macro to have
     * @return the current Layer instance
     */
    Layer addMacro(MacroInterface macro);

    /**
     * Remove a macro the Layer shouldn't have
     *
     * @param macro a macro to remove
     * @return the current Layer instance
     */
    Layer removeMacro(UUID macro);

    /**
     * Remove a macro the Layer shouldn't have
     *
     * @param macro a macro to remove
     * @return the current Layer instance
     */
    Layer removeMacro(MacroInterface macro);


    /**
     * Get the unique layer id
     *
     * @return unique layer id
     */
    UUID getULID();
}
