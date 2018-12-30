package com.mattsmeets.macrokey.model.lambda;

import com.mattsmeets.macrokey.config.ModConfig;
import net.minecraft.client.Minecraft;

import static com.mattsmeets.macrokey.MacroKey.instance;

public interface ExecuteOnTickInterface {
    public boolean execute(boolean delayed);

    public ExecuteOnTickInterface openMacroKeyGUI =
            (boolean delayed) -> {
                    Minecraft.getMinecraft().player.openGui(
                            instance,
                            ModConfig.guiMacroManagementId,
                            Minecraft.getMinecraft().world,
                            (int) Minecraft.getMinecraft().player.posX,
                            (int) Minecraft.getMinecraft().player.posY,
                            (int) Minecraft.getMinecraft().player.posZ
                    );

                    return true;
            };
}
