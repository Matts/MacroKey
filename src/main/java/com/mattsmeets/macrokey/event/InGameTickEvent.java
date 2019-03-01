package com.mattsmeets.macrokey.event;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.eventbus.api.Event;

public class InGameTickEvent extends Event {

    /**
     * Current player / sender
     */
    private EntityPlayerSP currentPlayer;
    /**
     * Is this a limited tick event
     */
    private boolean limitedTickEvent;

    public InGameTickEvent(EntityPlayerSP entityPlayerSP, boolean limited) {
        this.currentPlayer = entityPlayerSP;
        this.limitedTickEvent = limited;
    }

    public InGameTickEvent(EntityPlayerSP entityPlayerSP) {
        this.currentPlayer = entityPlayerSP;
        this.limitedTickEvent = false;
    }

    public EntityPlayerSP getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isLimitedTick() {
        return limitedTickEvent;
    }

    public static class LimitedInGameTickEvent extends InGameTickEvent {
        public LimitedInGameTickEvent(EntityPlayerSP entityPlayerSP) {
            super(entityPlayerSP, true);
        }
    }

}
