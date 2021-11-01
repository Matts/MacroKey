package com.mattsmeets.macrokey.gui;

import org.apache.commons.lang3.StringUtils;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.model.Layer;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class GuiModifyLayer extends Screen {

    private final Screen parentScreen;
    private final LayerInterface result;

    private final String defaultScreenTitleText = I18n.get("gui.modify.layer.text.title.new");
    private final String editScreenTitleText = I18n.get("gui.modify.layer.text.title.edit");
    private final String saveLayerButtonText = I18n.get("gui.modify.layer.text.save");

    private final String cancelText = I18n.get("gui.cancel");

    private TextFieldWidget textFieldName;

    private boolean existing;

    public GuiModifyLayer(Screen guiScreen, LayerInterface layer) {
        super(new StringTextComponent("test"));
        this.parentScreen = guiScreen;
        this.result = layer == null ? new Layer() : layer;
        this.existing = layer != null;
    }

    GuiModifyLayer(Screen parentScreen) {
        this(parentScreen, null);
    }

    @Override
    public void init() {
        super.init();

        // Add layer button
        this.addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new StringTextComponent(this.saveLayerButtonText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (textFieldName.getValue().length() <= 1) {
                    return;
                }

                result.setDisplayName(textFieldName.getValue());

                if (existing) {
                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerChangedEvent(result));
                } else {
                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerAddedEvent(result));
                }

                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        // Cancel button
        this.addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, new StringTextComponent(this.cancelText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        this.textFieldName = new TextFieldWidget(this.font, this.width / 2 - 100, 50, 200, 20, new StringTextComponent(existing ? result.getDisplayName() : StringUtils.EMPTY));
        this.textFieldName.setFocus(true);
        this.textFieldName.setMaxLength(20);
        this.textFieldName.setValue(existing ? result.getDisplayName() : StringUtils.EMPTY);
        this.addButton(this.textFieldName);
    }

    @Override
    public void render(MatrixStack ps, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ps);

        // Render title
        drawCenteredString(ps, this.font, !existing ? this.defaultScreenTitleText : this.editScreenTitleText, this.width / 2, 8, 0xFFFFFF);

        // Render buttons & labels
        super.render(ps, mouseX, mouseY, partialTicks);

        // Render text fields
        this.textFieldName.render(ps, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        this.textFieldName.tick();
    }

    @Override
    public boolean charTyped(char keyValue, int modifier) {
        return this.textFieldName.charTyped(keyValue, modifier);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        return textFieldName.keyPressed(keyCode, scanCode, modifier);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.textFieldName.mouseClicked(mouseX, mouseY, mouseButton);

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
