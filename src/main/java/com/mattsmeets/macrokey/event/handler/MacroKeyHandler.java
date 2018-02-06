package com.mattsmeets.macrokey.event.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mattsmeets.macrokey.event.LimitedInGameTickEvent;
import com.mattsmeets.macrokey.event.MacroKeyEvent;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;

@SideOnly(Side.CLIENT)
public class MacroKeyHandler {

    /**
     * private stash of macro's to run
     */
    private Set<MacroInterface> macrosToRun;

    public MacroKeyHandler() {
        this.macrosToRun = new HashSet<>();
    }

    @SubscribeEvent
    public void onKeyEvent(MacroKeyEvent event) {
        if (event.getMacroState().isKeyDown()) {
            this.macrosToRun.addAll(event.getMacros());
        } else {
            this.macrosToRun.removeAll(event.getMacros());
        }
    }

    @SubscribeEvent
    public void inGameTickEvent(LimitedInGameTickEvent event) {
        // loop through all pending macro's sending their command
        // and then removing them from the list if it is not set to repeat
        this.macrosToRun.forEach(macro -> event.getCurrentPlayer().sendChatMessage(macro.getCommand()));

        // remove the command from the pending
        // list if it is not to be re-executed
        this.macrosToRun.removeIf(macro -> !macro.willRepeat());
    }

}
