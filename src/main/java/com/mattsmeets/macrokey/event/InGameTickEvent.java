package com.mattsmeets.macrokey.event;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.eventbus.api.Event;

public class InGameTickEvent extends Event {

    /**
     * Current player / sender
     */
    private ClientPlayerEntity currentPlayer;
    /**
     * Is this a limited tick event
     */
    private boolean limitedTickEvent;

    public InGameTickEvent(ClientPlayerEntity entityPlayerSP, boolean limited) {
        this.currentPlayer = entityPlayerSP;
        this.limitedTickEvent = limited;
    }

    public ClientPlayerEntity getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isLimitedTick() {
        return limitedTickEvent;
    }

    public static class LimitedInGameTickEvent extends InGameTickEvent {
        public LimitedInGameTickEvent(ClientPlayerEntity entityPlayerSP) {
            super(entityPlayerSP, true);
        }
    }
}
