package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.event.InGameTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class GameTickHandler {

    /**
     * private stash of macro's to run
     */
    private Set<MacroInterface> macrosToRun;

    /**
     * private stash of executor's to run
     */
    private Set<ExecuteOnTickInterface> executorsToRun;

    public GameTickHandler(HashSet macrosToRun, HashSet executorsToRun) {
        this.macrosToRun = macrosToRun == null ? new HashSet<>() : macrosToRun;
        this.executorsToRun = executorsToRun == null ? new HashSet<>() : executorsToRun;
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
    public void onExecutorEvent(ExecuteOnTickEvent event) {
        executorsToRun.add(event.getExecutor());
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
                .forEach(macro -> macro.getCommand().execute(event.getCurrentPlayer()));

       this.executorsToRun.removeIf(executor -> executor.execute(event.isLimitedTick()));

        // remove the command from the pending
        // list if it is not to be re-executed
        this.macrosToRun.removeIf(macro -> !macro.willRepeat());
    }

}
