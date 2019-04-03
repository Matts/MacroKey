package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.repository.BindingsRepository;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class KeyInputHandler {

    private final BindingsRepository bindingsRepository;
    private final ModState modState;
    // private stash of pressed keys
    private final Set<Integer> pressedKeys;

    public KeyInputHandler(final BindingsRepository bindingsRepository, final ModState modState) {
        this.bindingsRepository = bindingsRepository;
        this.modState = modState;
        this.pressedKeys = new HashSet<>();
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) throws IOException {
        int keyCode = 0;
        boolean keyIsDown = false;

        final Set<MacroInterface> macroList = bindingsRepository.findMacroByKeycode(keyCode, modState.getActiveLayer(), false);
        if (macroList.size() == 0) {
            return;
        }

        if (keyIsDown && !this.pressedKeys.contains(keyCode)) {
            /*
             * if the key has not been pressed during last events, send
             * an event, and add it to the current index of pressed keys
             */
            MinecraftForge.EVENT_BUS.post(new MacroActivationEvent.MacroActivationPressEvent(macroList));

            this.pressedKeys.add(keyCode);
        } else if (!keyIsDown && this.pressedKeys.contains(keyCode)) {
            /*
             * if the key has been pressed during last events, send
             * an event, and remove it from the current index of pressed keys
             */
            MinecraftForge.EVENT_BUS.post(new MacroActivationEvent.MacroActivationReleaseEvent(macroList));

            this.pressedKeys.remove(keyCode);
        }
    }
}
