package com.mattsmeets.macrokey.model.lambda;

import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import net.minecraft.client.Minecraft;

public interface ExecuteOnTickInterface {
    void execute(boolean delayed);

    ExecuteOnTickInterface openMacroKeyGUI = (boolean delayed) -> Minecraft.getInstance().setScreen(new GuiMacroManagement(Minecraft.getInstance().screen));
}
