package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import com.mattsmeets.macrokey.repository.BindingsRepository;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class KeyInputHandler {

    private final BindingsRepository bindingsRepository;
    private final ModState modState;
    private final KeyBinding[] keyBindingList;
    // private stash of pressed keys
    private final Set<Integer> pressedKeys;

    public KeyInputHandler(final BindingsRepository bindingsRepository, final ModState modState) {
        this.bindingsRepository = bindingsRepository;
        this.modState = modState;
        this.keyBindingList = registerKeyBindings();
        this.pressedKeys = new HashSet<>();
    }

    private static KeyBinding[] registerKeyBindings() {
        final KeyBinding managementKey = new KeyBinding("key.macrokey.management.desc", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrMakeInput(GLFW.GLFW_KEY_K), "key.macrokey.category");
        final KeyBinding[] keyBindingList = new KeyBinding[]{managementKey};

        for (final KeyBinding keyBinding : keyBindingList) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        return keyBindingList;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInputEvent(InputEvent.KeyInputEvent event) throws IOException {
        // find if the current key being pressed is the dedicated
        // MacroKey gui button. If so, open its GUI
        if (keyBindingList[0].isPressed()) {
            MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));
        }

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
