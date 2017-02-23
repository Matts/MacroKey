package com.mattsmeets.macrokey.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import com.mattsmeets.macrokey.object.*;

import java.io.IOException;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiKeybindings extends GuiScreen implements GuiYesNoCallback {

    private GuiKeyBindingsListing keyBindingList;

    private GuiScreen parentScreen;
    private GameSettings options;

    public BoundKey boundKey;

    protected String screenTitle = I18n.format("gui.keybindings.screenTitle", new Object[0]);

    public GuiKeybindings(GuiScreen screen, GameSettings settings){
        this.parentScreen = screen;
        this.options = settings;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        guiButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
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
            this.mc.displayGuiScreen(new GuiCreateKeybinding(this));
        }
    }

    private GuiButton guiButton;
    private GuiButton buttonAdd;

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(guiButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.keybindings.addkeybinding", new Object[0])));
        keyBindingList = new GuiKeyBindingsListing(this, mc);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (boundKey != null)
        {
            if (keyCode == 1)
            {
                boundKey.setKeyCode(0);
            }
            else if (keyCode != 0)
            {
                boundKey.setKeyCode(keyCode);
            }
            else if (typedChar > 0)
            {
                boundKey.setKeyCode(typedChar + 256);
            }

            boundKey = null;
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (boundKey != null)
        {
            boundKey = null;
        }
        else
        if (mouseButton != 0 || !keyBindingList.mouseClicked(mouseX, mouseY, mouseButton))
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if(guiButton.mousePressed(mc, mouseX, mouseY)){
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        keyBindingList.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
