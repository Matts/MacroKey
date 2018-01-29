package com.mattsmeets.macrokey.event.handler;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mattsmeets.macrokey.event.MacroKeyEvent;

@SideOnly(Side.CLIENT)
public class MacroKeyHandler {

    @SubscribeEvent
    public void onKeyPress(MacroKeyEvent.MacroKeyPressEvent event) {
        event.getMacros()
                .forEach((macro) ->
                        event.getCurrentPlayer().sendChatMessage(macro.getCommand())
                );
    }

}
