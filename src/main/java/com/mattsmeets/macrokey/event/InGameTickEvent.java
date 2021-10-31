package com.mattsmeets.macrokey.event;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.eventbus.api.Event;

public class InGameTickEvent extends Event {

    /**
     * Current player / sender
     */
    private LocalPlayer currentPlayer;
    /**
     * Is this a limited tick event
     */
    private boolean limitedTickEvent;

    public InGameTickEvent(LocalPlayer entityPlayerSP, boolean limited) {
        this.currentPlayer = entityPlayerSP;
        this.limitedTickEvent = limited;
    }

    public LocalPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isLimitedTick() {
        return limitedTickEvent;
    }

    public static class LimitedInGameTickEvent extends InGameTickEvent {
        public LimitedInGameTickEvent(LocalPlayer entityPlayerSP) {
            super(entityPlayerSP, true);
        }
    }
}
