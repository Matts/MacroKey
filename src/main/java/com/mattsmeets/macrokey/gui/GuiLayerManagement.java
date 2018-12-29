package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.gui.fragment.LayerListFragment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import java.io.IOException;

public class GuiLayerManagement extends GuiScreen {
    private final GuiScreen parentScreen;
    private final String
            screenTitle = I18n.format("gui.manage.layer.text.title"),
            addLayerButtonText = I18n.format("gui.manage.text.layer.add");
    private final String
            doneText = I18n.format("gui.done");
    private LayerListFragment layerListFragment;
    private GuiButton
            buttonDone,
            buttonAdd;

    public GuiLayerManagement(GuiScreen screen) {
        this.parentScreen = screen;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.layerListFragment.drawScreen(mouseX, mouseY, partialTicks);

        buttonDone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);

        this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiModifyLayer(this));
                break;
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(buttonDone = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, this.doneText));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, this.addLayerButtonText));

        try {
            this.layerListFragment = new LayerListFragment(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton != 0 || !this.layerListFragment.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.layerListFragment.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
