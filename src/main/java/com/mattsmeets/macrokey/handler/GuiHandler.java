package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.gui.GuiMacroManagement;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == ModConfig.guiMacroManagementId) {
            return new GuiMacroManagement(Minecraft.getMinecraft().currentScreen);
        }

        return null;
    }

}
