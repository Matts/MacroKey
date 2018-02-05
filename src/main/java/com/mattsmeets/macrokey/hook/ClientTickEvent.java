package com.mattsmeets.macrokey.hook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mattsmeets.macrokey.event.LimitedInGameTickEvent;

@SideOnly(Side.CLIENT)
public class ClientTickEvent {

    private int delta;
    private int delay = 20; /* TODO: #14, make the tick delay a config option */

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        // rate-limiting so users can define
        // how fast a repeating command should execute
        if(delta < delay) {
            delta++;
            return;
        }

        // check if we are in-game
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if(player != null) {
            MinecraftForge.EVENT_BUS.post(new LimitedInGameTickEvent(player));
        }

        // set delta back to zero
        delta = 0;
    }

}
