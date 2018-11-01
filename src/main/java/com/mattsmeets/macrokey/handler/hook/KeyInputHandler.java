package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class KeyInputHandler {

    // private stash of pressed keys
    private Set<Integer> pressedKeys;

    public KeyInputHandler() {
        this.pressedKeys = new HashSet<>();
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) throws IOException {
        int keyCode = Keyboard.getEventKey();

        // find if the current key being pressed is the dedicated
        // MacroKey gui button. If so, open its GUI
        if (instance.forgeKeybindings[0].isPressed()) {
            MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));
        }

        // find all macro's by the current key pressed, while not syncing
        Set<MacroInterface> macros =
                instance.bindingsRepository.findMacroByKeycode(keyCode, instance.modState.getActiveLayer(), false);

        // if the list is not empty
        if (macros.size() == 0) {
            macros = null;
            return;
        }

        // is the button pressed, or being released
        if (Keyboard.getEventKeyState()) {
                /*
                if the key has not been pressed during last events, send
                an event, and add it to the current index of pressed keys
                 */
            if (!this.pressedKeys.contains(keyCode)) {
                MinecraftForge.EVENT_BUS.post(new MacroActivationEvent.MacroActivationPressEvent(macros));
                this.pressedKeys.add(keyCode);
            }
        } else {
                /*
                if the key has been pressed during last events, send
                an event, and remove it from the current index of pressed keys
                 */
            if (this.pressedKeys.contains(keyCode)) {
                MinecraftForge.EVENT_BUS.post(new MacroActivationEvent.MacroActivationReleaseEvent(macros));
                this.pressedKeys.remove(keyCode);
            }
        }
    }

}
