package com.mattsmeets.macrokey.event.handler;

import com.mattsmeets.macrokey.event.InGameTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

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
    public void onKeyEvent(MacroActivationEvent event) {
        if (event.getMacroState().isKeyDown()) {
            this.macrosToRun.addAll(event.getMacros());
        } else {
            this.macrosToRun.removeAll(event.getMacros());
        }
    }

    @SubscribeEvent
    public void onTick(InGameTickEvent event) {
        // loop through all pending macro's sending their command
        // on a off-tick, it will only send normal macro's, on a
        // delayed tick it will also execute the repeatable macro's
        // finally removing them from the list if it is not set to repeat
        this.macrosToRun
                .stream()
                .filter(macro -> !macro.willRepeat() || event.isLimitedTick())
                .forEach(macro -> event.getCurrentPlayer().sendChatMessage(macro.getCommand()));

        // remove the command from the pending
        // list if it is not to be re-executed
        this.macrosToRun.removeIf(macro -> !macro.willRepeat());
    }
}
