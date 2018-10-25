package com.mattsmeets.macrokey.model;

import net.minecraft.client.entity.EntityPlayerSP;

public interface CommandInterface {

    /**
     * toString to save into file
     *
     * @return stringified command
     */
    String toString();

    /**
     * Run Command
     * @param player the player
     */
    void execute(EntityPlayerSP player);

    /**
     * Type identifier used for (de)serialization
     * @return String
     */
    String getCommandType();

}
