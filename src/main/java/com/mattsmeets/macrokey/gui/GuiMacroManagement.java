package com.mattsmeets.macrokey.gui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.gui.list.MacroListFragment;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

// TODO : Clean this class
public class GuiMacroManagement extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int BUTTON_LEFT = 0;

    private final Screen parentScreen;
    private MacroListFragment macroListFragment;
    private Button layerSwitcher;

    public MacroInterface macroModify;

    private int currentSelectedLayer;
    private List<LayerInterface> layers;

    public GuiMacroManagement(final Screen screen) {
        super(new TranslationTextComponent("controls.title"));
        this.parentScreen = screen;
        this.currentSelectedLayer = -1;
    }

    @Override
    public void init() {
        super.init();

        final GuiMacroManagement that = this;
        // Cancel button
        this.addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TranslationTextComponent("gui.done"), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        // Add macro button
        this.addButton(new Button( this.width / 2 - 155 + 160, this.height - 29, 150, 20, new TranslationTextComponent("gui.manage.text.macro.add"), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(new GuiModifyMacro(that));
            }
        });

        // Open layer manager button
        this.addButton(new Button( this.width / 2 - 155 + 160, 40, 150, 20, new TranslationTextComponent("gui.manage.text.layer.edit"), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(new GuiLayerManagement(that));
            }
        });

        this.layerSwitcher = this.addButton(new Button( this.width / 2 - 155, 40, 150, 20, new TranslationTextComponent("gui.manage.text.layer.switch"), Button::onPress) {
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
    public void render(MatrixStack ps, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ps);
        // Render macro list
        this.macroListFragment.render(ps, mouseX, mouseY, partialTicks);

        // Render Title
        drawCenteredString(ps, this.font, I18n.get("gui.manage.text.title"), this.width / 2, 8, 0xFFFFFF);

        // Render Buttons & Labels
        super.render(ps,mouseX, mouseY, partialTicks);
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
    public boolean isPauseScreen() {
        return true;
    }

    private void updateMacroList() {
        try {
            this.layers = MacroKey.modState.getLayers(true);
        } catch (IOException e) {
            LOGGER.error(e);
            return;
        }

        final LayerInterface currentLayer = currentSelectedLayer == -1 ? null : this.layers.get(currentSelectedLayer);
        final String currentLayerName = currentLayer == null ? I18n.get("text.layer.master") : currentLayer.getDisplayName();

        this.macroListFragment = new MacroListFragment(this, currentLayer);
        this.layerSwitcher.setMessage(new TranslationTextComponent("text.layer.display", currentLayerName));
    }
}
