package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraftforge.fml.common.eventhandler.Event;

public class MacroChangedEvent extends Event {

    private MacroInterface macroChanged;

    public MacroChangedEvent(MacroInterface macroChanged) {
        this.macroChanged = macroChanged;
    }

    public MacroInterface getMacroChanged() {
        return macroChanged;
    }
}
