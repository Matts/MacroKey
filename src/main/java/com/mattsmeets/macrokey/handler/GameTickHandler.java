package com.mattsmeets.macrokey.handler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mattsmeets.macrokey.ModKeyBinding;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.event.InGameTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GameTickHandler {

    /**
     * Mod KeyBinding list
     */
    private final Map<ModKeyBinding, KeyBinding> keyBindings;

    /**
     * private stash of macro's to run
     */
    private final Set<MacroInterface> macrosToRun;

    /**
     * private stash of executor's to run
     */
    private final Set<ExecuteOnTickInterface> executorsToRun;

    public GameTickHandler(Set<MacroInterface> macrosToRun, Set<ExecuteOnTickInterface> executorsToRun, Map<ModKeyBinding, KeyBinding> keyBindings) {
        this.keyBindings = keyBindings;
        this.macrosToRun = macrosToRun == null ? new HashSet<>() : macrosToRun;
        this.executorsToRun = executorsToRun == null ? new HashSet<>() : executorsToRun;
    }

    @SubscribeEvent
    public void onKeyEvent(MacroActivationEvent event) {
        if (Minecraft.getInstance() != null && Minecraft.getInstance().screen != null) {
            return;
        }
        if (event.getMacroState().isKeyDown()) {
            this.macrosToRun.addAll(event.getMacros());
        } else {
            this.macrosToRun.removeIf(macro -> event.getMacros().contains(macro) && macro.willRepeat());
        }
    }

    @SubscribeEvent
    public void onExecutorEvent(ExecuteOnTickEvent event) {
        executorsToRun.add(event.getExecutor());
    }

    @SubscribeEvent
    public void onTick(InGameTickEvent event) {
        // find if the current key being pressed is the dedicated
        // MacroKey gui button. If so, open its GUI
        if (keyBindings.get(ModKeyBinding.OPEN_MANAGEMENT_GUI).isDown()) {
            MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));
        }

        // loop through all pending macro's sending their command
        // on a off-tick, it will only send normal macro's, on a
        // delayed tick it will also execute the repeatable macro's
        // finally removing them from the list if it is not set to repeat
        this.macrosToRun
                .stream()
                .filter(macro -> !macro.willRepeat() || event.isLimitedTick())
                .forEach(macro -> macro.getCommand().execute(event.getCurrentPlayer()));

        // loop through all executors and run them.
        this.executorsToRun
                .forEach(executor -> executor.execute(event.isLimitedTick()));

        // remove the command from the pending
        // list if it is not to be re-executed
        this.macrosToRun.removeIf(macro -> !macro.willRepeat());
        this.executorsToRun.clear();
    }

}
