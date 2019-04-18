package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.gui.fragment.LayerListFragment;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GuiLayerManagement extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final GuiScreen parentScreen;

    private final String screenTitle = I18n.format("gui.manage.layer.text.title");
    private final String addLayerButtonText = I18n.format("gui.manage.text.layer.add");
    private final String doneText = I18n.format("gui.done");

    private LayerListFragment layerListFragment;

    GuiLayerManagement(GuiScreen screen) {
        this.parentScreen = screen;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Render list
        this.layerListFragment.drawScreen(mouseX, mouseY, partialTicks);

        // Render title
        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 0xFFFFFF);

        // Render buttons & labels
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        final GuiLayerManagement that = this;

        // Cancel button
        this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, this.doneText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(parentScreen);
            }
        });

        // Add layer button
        this.addButton(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, this.addLayerButtonText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(new GuiModifyLayer(that));
            }
        });

        try {
            this.layerListFragment = new LayerListFragment(this);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton != 0 || !this.layerListFragment.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
