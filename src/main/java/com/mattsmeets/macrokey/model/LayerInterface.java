package com.mattsmeets.macrokey.model;

import java.util.Set;
import java.util.UUID;

public interface LayerInterface {

    /**
     * Get the displayName that should be rendered
     *
     * @return display name, user given name
     */
    public String getDisplayName();

    /**
     * Set the displayName that the macro should have
     *
     * @param displayName displayName to set
     * @return the current Layer instance
     */
    public Layer setDisplayName(String displayName);

    /**
     * Get the macros bound to the layer
     *
     * @return Set of macros
     */
    public Set<UUID> getMacros();

    /**
     * Set the macros the Layer should have
     *
     * @param macros the macros to have
     * @return the current Layer instance
     */
    public Layer setMacros(Set<UUID> macros);

    /**
     * Add a macro the Layer should have
     *
     * @param macro a macro to have
     * @return the current Layer instance
     */
    public Layer addMacro(UUID macro);

    /**
     * Add a macro the Layer should have
     *
     * @param macro a macro to have
     * @return the current Layer instance
     */
    public Layer addMacro(Macro macro);

    /**
     * Get the unique layer id
     *
     * @return unique layer id
     */
    public UUID getULID();

    /**
     * Set the unique layer id
     *
     * @param ulid unique layer id
     * @return the current Layer instance
     */
    public Layer setULID(UUID ulid);

}
