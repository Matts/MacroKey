package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.config.ModConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class GuiHandler {

//    @Override
    public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        return null;
    }

//    @Override
    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        if (id == ModConfig.guiMacroManagementId) {
//            return new GuiMacroManagement(Minecraft.getInstance().currentScreen);
        }

        return null;
    }

}
