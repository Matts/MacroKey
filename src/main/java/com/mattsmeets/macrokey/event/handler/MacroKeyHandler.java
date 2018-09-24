package com.mattsmeets.macrokey.event.handler;

import com.mattsmeets.macrokey.event.InGameTickEvent;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.BossInfo;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
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
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        engine.put("event", event);
        try {
            engine.eval("function sendMessage(message) { event.getCurrentPlayer().sendChatMessage(message) }");
        } catch (ScriptException e) {
            e.printStackTrace();
        }

        this.macrosToRun
                .stream()
                .filter(macro -> !macro.willRepeat() || event.isLimitedTick())
                .forEach(macro -> {
                    try {
                        engine.put("macro", macro);
                        // todo: DON'T DO THIS
                        engine.eval("function sleep() { macro.sleep(5); }");
                        engine.eval(macro.getCommand());
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                });

        // remove the command from the pending
        // list if it is not to be re-executed
        this.macrosToRun.removeIf(macro -> !macro.willRepeat());
    }
}
