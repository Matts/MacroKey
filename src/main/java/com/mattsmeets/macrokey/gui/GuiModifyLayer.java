package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.model.Layer;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;

public class GuiModifyLayer extends GuiScreen {

    private final GuiScreen parentScreen;
    private final LayerInterface result;

    private final String defaultScreenTitleText = I18n.format("gui.modify.layer.text.title.new");
    private final String editScreenTitleText = I18n.format("gui.modify.layer.text.title.edit");
    private final String saveLayerButtonText = I18n.format("gui.modify.layer.text.save");

    private final String cancelText = I18n.format("gui.cancel");

    private GuiTextField textFieldName;

    private boolean existing;

    public GuiModifyLayer(GuiScreen guiScreen, LayerInterface layer) {
        this.parentScreen = guiScreen;
        this.result = layer == null ? new Layer() : layer;
        this.existing = layer != null;
    }

    GuiModifyLayer(GuiScreen parentScreen) {
        this(parentScreen, null);
    }

    @Override
    public void initGui() {
        super.initGui();

        // Add layer button
        this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, this.saveLayerButtonText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (textFieldName.getText().length() <= 1) {
                    return;
                }

                result.setDisplayName(textFieldName.getText());

                if (existing) {
                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerChangedEvent(result));
                } else {
                    MinecraftForge.EVENT_BUS.post(new LayerEvent.LayerAddedEvent(result));
                }

                mc.displayGuiScreen(parentScreen);
            }
        });

        // Cancel button
        this.addButton(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, this.cancelText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(parentScreen);
            }
        });

        this.textFieldName = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 50, 200, 20);
        this.textFieldName.setFocused(true);
        this.textFieldName.setMaxStringLength(20);
        this.textFieldName.setText(existing ? result.getDisplayName() : StringUtils.EMPTY);
        this.children.add(this.textFieldName);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Render title
        this.drawCenteredString(this.fontRenderer, !existing ? this.defaultScreenTitleText : this.editScreenTitleText, this.width / 2, 8, 0xFFFFFF);

        // Render buttons & labels
        super.render(mouseX, mouseY, partialTicks);

        // Render text fields
        this.textFieldName.drawTextField(mouseX, mouseY, partialTicks);
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
}
