package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.event.InGameTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientTickHandler {
    /**
     * Amount of ticks since last limited tick
     */
    private int delta;

    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        final LocalPlayer player = Minecraft.getInstance().player;

        // check if we are in-game
        if (player == null) {
            return;
        }

        // every tick post an event for the normal,
        // non repeating commands to trigger
        MinecraftForge.EVENT_BUS.post(new InGameTickEvent(player, false));

        // rate-limiting so users can define
        // how fast a repeating command should execute
        // retrieve the given delay within the config,
        // this will by default be 20 ticks
        if (delta < ModConfig.repeatDelay.get()) {
            delta++;
        } else {
            delta = 0;

            // once the delta time has reached the delay,
            // post a tick event for the repeating commands
            MinecraftForge.EVENT_BUS.post(new InGameTickEvent.LimitedInGameTickEvent(player));
        }
    }
}
