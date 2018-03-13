package com.mattsmeets.macrokey.event.handler;

import com.mattsmeets.macrokey.gui.TestScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {
    public static final int KEYBINDINGS_GUI = 5002;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == KEYBINDINGS_GUI)
            return new TestScreen(Minecraft.getMinecraft().currentScreen, Minecraft.getMinecraft().gameSettings);
        return null;
    }
}
