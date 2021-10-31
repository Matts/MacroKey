package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;

public class MacroActivationEvent extends Event {

    /**
     * The macro('s) that have been activated
     */
    private Set<MacroInterface> macros;

    /**
     * Current state of the button
     */
    private MacroState pressed;

    MacroActivationEvent(Set<MacroInterface> macros, MacroState pressed) {
        this.macros = macros;
        this.pressed = pressed;
    }

    public Set<MacroInterface> getMacros() {
        return this.macros;
    }

    public MacroState getMacroState() {
        return pressed;
    }

    /**
     * States the key pressed event can be in
     */
    public enum MacroState {
        KEY_UP(false),
        KEY_DOWN(true);

        private boolean state;

        MacroState(boolean state) {
            this.state = state;
        }

        public boolean isKeyDown() {
            return this.state;
        }
    }

    public static class MacroActivationPressEvent extends MacroActivationEvent {
        public MacroActivationPressEvent(Set<MacroInterface> macros) {
            super(macros, MacroState.KEY_DOWN);
        }
    }

    public static class MacroActivationReleaseEvent extends MacroActivationEvent {
        public MacroActivationReleaseEvent(Set<MacroInterface> macros) {
            super(macros, MacroState.KEY_UP);
        }
    }

}
