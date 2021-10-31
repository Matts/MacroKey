package com.mattsmeets.macrokey.gui;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.mattsmeets.macrokey.gui.list.LayerListFragment;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;

public class GuiLayerManagement extends Screen {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Screen parentScreen;

    private final String screenTitle = I18n.get("gui.manage.layer.text.title");
    private final String addLayerButtonText = I18n.get("gui.manage.text.layer.add");
    private final String doneText = I18n.get("gui.done");

    private LayerListFragment layerListFragment;

    private Button btnDone, btnAdd;

    GuiLayerManagement(Screen screen) {
        super(new StringTextComponent("test"));
        this.parentScreen = screen;
    }

    @Override
    public void render(MatrixStack ps, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ps);

        // Render list
        this.layerListFragment.render(ps, mouseX, mouseY, partialTicks);

        // Render title
        drawCenteredString(ps, this.font, this.screenTitle, this.width / 2, 8, 0xFFFFFF);

        // Render buttons & labels
        super.render(ps, mouseX, mouseY, partialTicks);
    }

    @Override
    public void init() {
        final GuiLayerManagement that = this;

        // Cancel button
        btnDone = this.addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new StringTextComponent(this.doneText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        // Add layer button
        btnAdd = this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, new StringTextComponent(this.addLayerButtonText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(new GuiModifyLayer(that));
            }
        });

        try {
            this.layerListFragment = new LayerListFragment(this);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public List<? extends IGuiEventListener> children() {
        return ImmutableList.of(this.btnAdd, this.btnDone);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseButton != 0 || !this.layerListFragment.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }

        return false;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
