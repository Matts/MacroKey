package com.mattsmeets.macrokey.event;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mattsmeets.macrokey.model.Macro;

@SideOnly(Side.CLIENT)
public class MacroKeyEvent extends Event {

    /**
     * States the key pressed event can be in
     */
    public static enum MacroState {
        KEY_UP(false),
        KEY_DOWN(true);

        private boolean state;

        MacroState(boolean state) {
            this.state = state;
        }

        public boolean isKeyDown() {
            return this.state;
        }

        public boolean isKeyUp() {
            return !this.state;
        }
    }

    /**
     * The macro('s) that have been activated
     */
    private Set<Macro> macros;

    /**
     * Current player / sender
     */
    private EntityPlayerSP currentPlayer;

    /**
     * Current state of the button
     */
    private MacroState pressed;

    public MacroKeyEvent(Set<Macro> macros, MacroState pressed) {
        this.macros = macros;
        this.pressed = pressed;

        this.currentPlayer = Minecraft.getMinecraft().player;
    }

    public Set<Macro> getMacros() {
        return this.macros;
    }

    public EntityPlayerSP getCurrentPlayer() {
        return currentPlayer;
    }

    public static class MacroKeyPressEvent extends MacroKeyEvent {
        public MacroKeyPressEvent(Set<Macro> macros) {
            super(macros, MacroState.KEY_DOWN);
        }
    }

    public static class MacroKeyReleaseEvent extends MacroKeyEvent {
        public MacroKeyReleaseEvent(Set<Macro> macros) {
            super(macros, MacroState.KEY_UP);
        }
    }

    public MacroState getMacroState() {
        return pressed;
    }
}
