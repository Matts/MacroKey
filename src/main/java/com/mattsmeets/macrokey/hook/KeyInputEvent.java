package com.mattsmeets.macrokey.hook;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.MacroKeyEvent;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;

public class KeyInputEvent {

    // private stash of pressed keys
    private Set<Integer> pressedKeys;

    public KeyInputEvent() {
        this.pressedKeys = new HashSet<>();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) throws IOException {
        int keyCode = Keyboard.getEventKey();

        // find all macro's by the current key pressed, while not syncing
        Set<MacroInterface> macros = MacroKey.instance.bindingsRepository.findMacroByKeycode(keyCode, true, false);

        // if the list is not empty
        if (macros.size() > 0) {
            // is the button pressed, or being released
            if (Keyboard.getEventKeyState()) {
                /*
                if the key has not been pressed during last events, send
                an event, and add it to the current index of pressed keys
                 */
                if (!this.pressedKeys.contains(keyCode)) {
                    MinecraftForge.EVENT_BUS.post(new MacroKeyEvent.MacroKeyPressEvent(macros));
                    this.pressedKeys.add(keyCode);
                }
            } else {
                /*
                if the key has been pressed during last events, send
                an event, and remove it from the current index of pressed keys
                 */
                if (this.pressedKeys.contains(keyCode)) {
                    MinecraftForge.EVENT_BUS.post(new MacroKeyEvent.MacroKeyReleaseEvent(macros));
                    this.pressedKeys.remove(keyCode);
                }
            }
        }

        // un-set set for gc
        macros = null;
    }

}
