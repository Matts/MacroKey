package com.mattsmeets.macrokey.hook;

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

public class KeyInputEvent {

    private Set<Integer> pressedKeys;

    public KeyInputEvent() {
        this.pressedKeys = new HashSet<>();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) {
        int keyCode = Keyboard.getEventKey();
        Set<Macro> macros = MacroKey.instance.bindingsRepository.findMacroByKeycode(keyCode);

        if (macros.size() > 0) {
            if (Keyboard.getEventKeyState()) {
                if (!this.pressedKeys.contains(keyCode)) {
                    MinecraftForge.EVENT_BUS.post(new MacroKeyEvent.MacroKeyPressEvent(macros));
                }
                this.pressedKeys.add(keyCode);
            } else {
                if (this.pressedKeys.contains(keyCode)) {
                    MinecraftForge.EVENT_BUS.post(new MacroKeyEvent.MacroKeyReleaseEvent(macros));
                }
                this.pressedKeys.remove(keyCode);
            }
        }

        macros = null;
    }

}
