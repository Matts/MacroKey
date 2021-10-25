package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.config.ModConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GuiHandler {

//    @Override
    public Object getServerGuiElement(int id, Player player, Level world, int x, int y, int z) {
        return null;
    }

//    @Override
    public Object getClientGuiElement(int id, Player player, Level world, int x, int y, int z) {
        if (id == ModConfig.guiMacroManagementId) {
//            return new GuiMacroManagement(Minecraft.getInstance().currentScreen);
        }

        return null;
    }

}
