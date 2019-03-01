package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraftforge.eventbus.api.Event;

public class MacroEvent {

    public static class MacroChangedEvent extends Event {
        private final MacroInterface macroChanged;

        public MacroChangedEvent(MacroInterface macroChanged) {
            this.macroChanged = macroChanged;
        }

        public MacroInterface getMacroChanged() {
            return macroChanged;
        }
    }

    public static class MacroAddedEvent extends Event {
        private final MacroInterface macro;

        public MacroAddedEvent(MacroInterface macro) {
            this.macro = macro;
        }

        public MacroInterface getMacro() {
            return macro;
        }
    }

}
