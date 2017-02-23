package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.gui.list.GuiLayerList;
import com.mattsmeets.macrokey.object.Layer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

import java.io.IOException;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiManageLayers extends GuiScreen implements GuiYesNoCallback {

    private GuiLayerList layerList;

    private GuiScreen parentScreen;
    private GameSettings options;

    public Layer layer;

    protected String screenTitle = "Manage Layer";

    private GuiButton buttonDone;
    private GuiButton buttonAdd;

    public GuiManageLayers(GuiScreen screen, GameSettings settings){
        this.parentScreen = screen;
        this.options = settings;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.layerList.drawScreen(mouseX, mouseY, partialTicks);
        buttonDone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);

        this.drawCenteredString(this.fontRendererObj, this.screenTitle, this.width / 2, 8, 16777215);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        if(button.id == 1){
            this.mc.displayGuiScreen(new GuiCreateLayer(this));
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(buttonDone = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, "Add Layer"));

        this.layerList = new GuiLayerList(this, this.mc);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        super.keyTyped(typedChar, keyCode);

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

        if (mouseButton != 0 || !this.layerList.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if(buttonDone.mousePressed(mc, mouseX, mouseY)){
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.layerList.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
