package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.gui.fragment.MacroListFragment;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.List;

// TODO :
public class GuiMacroManagement extends GuiScreen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int BUTTON_LEFT = 0;

    private final GuiScreen parentScreen;
    private MacroListFragment macroListFragment;
    private GuiButton layerSwitcher;

    public MacroInterface macroModify;

    private int currentSelectedLayer;
    private List<LayerInterface> layers;

    public GuiMacroManagement(final GuiScreen screen) {
        this.parentScreen = screen;
        this.currentSelectedLayer = -1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        super.initGui();

        final GuiMacroManagement that = this;

        // Cancel button
        this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().displayGuiScreen(parentScreen);
            }
        });

        // Add macro button
        this.addButton(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.manage.text.macro.add")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().displayGuiScreen(new GuiModifyMacro(that));
            }
        });

        // Open layer manager button
        this.addButton(new GuiButton(2, this.width / 2 - 155 + 160, 40, 150, 20, I18n.format("gui.manage.text.layer.edit")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().displayGuiScreen(new GuiLayerManagement(that));
            }
        });

        this.layerSwitcher = this.addButton(new GuiButton(3, this.width / 2 - 155, 40, 150, 20, I18n.format("gui.manage.text.layer.switch")) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (currentSelectedLayer < layers.size() - 1) {
                    currentSelectedLayer++;
                } else {
                    currentSelectedLayer = -1;
                }

                updateMacroList();
            }
        });

        updateMacroList();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Render macro list
        this.macroListFragment.drawScreen(mouseX, mouseY, partialTicks);

        // Render Title
        this.drawCenteredString(this.fontRenderer, I18n.format("gui.manage.text.title"), this.width / 2, 8, 0xFFFFFF);

        // Render Buttons & Labels
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (this.macroModify == null) {
            return super.keyPressed(keyCode, scanCode, modifier);
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.macroModify.setKeyCode(0);
        } else if (keyCode != GLFW.GLFW_KEY_UNKNOWN) {
            this.macroModify.setKeyCode(keyCode);
        }

        MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(this.macroModify));

        this.macroModify = null;

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.macroModify != null) {
            this.macroModify = null;
        } else if (mouseButton != BUTTON_LEFT || !this.macroListFragment.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private void updateMacroList() {
        try {
            this.layers = MacroKey.modState.getLayers(true);
        } catch (IOException e) {
            LOGGER.error(e);
            return;
        }

        final LayerInterface currentLayer = currentSelectedLayer == -1 ? null : this.layers.get(currentSelectedLayer);
        final String currentLayerName = currentLayer == null ? I18n.format("text.layer.master") : currentLayer.getDisplayName();

        this.macroListFragment = new MacroListFragment(this, currentLayer);
        this.layerSwitcher.displayString = I18n.format("text.layer.display", currentLayerName);
    }
}
