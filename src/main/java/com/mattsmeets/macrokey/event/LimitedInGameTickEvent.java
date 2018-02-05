package com.mattsmeets.macrokey.event;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LimitedInGameTickEvent extends Event {

    /**
     * Current player / sender
     */
    private EntityPlayerSP currentPlayer;

    public LimitedInGameTickEvent(EntityPlayerSP entityPlayerSP) {
        this.currentPlayer = entityPlayerSP;
    }

    public EntityPlayerSP getCurrentPlayer() {
        return currentPlayer;
    }
}
