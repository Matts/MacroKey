package com.mattsmeets.macrokey.model.lambda;

import com.mattsmeets.macrokey.config.ModConfig;
import net.minecraft.client.Minecraft;

import static com.mattsmeets.macrokey.MacroKey.instance;

public interface ExecuteOnTickInterface {
    public void execute(boolean delayed);

    public ExecuteOnTickInterface openMacroKeyGUI =
            (boolean delayed) ->
                    Minecraft.getMinecraft().thePlayer.openGui(
                            instance,
                            ModConfig.guiMacroManagementId,
                            Minecraft.getMinecraft().theWorld,
                            (int) Minecraft.getMinecraft().thePlayer.posX,
                            (int) Minecraft.getMinecraft().thePlayer.posY,
                            (int) Minecraft.getMinecraft().thePlayer.posZ
                    );
}
