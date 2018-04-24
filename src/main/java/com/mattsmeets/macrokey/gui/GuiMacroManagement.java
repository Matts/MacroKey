package com.mattsmeets.macrokey.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;

public class GuiMacroManagement extends GuiScreen {

    private final GuiScreen parent;
    private final GameSettings settings;

    public GuiMacroManagement(GuiScreen parent, GameSettings settings) {
        this.parent = parent;
        this.settings = settings;
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
