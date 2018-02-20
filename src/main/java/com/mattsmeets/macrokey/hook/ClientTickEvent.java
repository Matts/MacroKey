package com.mattsmeets.macrokey.hook;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.event.InGameTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientTickEvent {

    private int delta;
    // retrieve the given delay within the config,
    // this will by default be 20 ticks
    private int delay = ModConfig.repeatDelay;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        // check if we are in-game
        if (player == null) {
            return;
        }

        MinecraftForge.EVENT_BUS.post(new InGameTickEvent(player, false));

        // rate-limiting so users can define
        // how fast a repeating command should execute
        if (delta < delay) {
            delta++;
            return;
        }

        MinecraftForge.EVENT_BUS.post(new InGameTickEvent.LimitedInGameTickEvent(player));


        // set delta back to zero
        delta = 0;
    }

}
