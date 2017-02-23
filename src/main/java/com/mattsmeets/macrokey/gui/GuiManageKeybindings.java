package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.gui.list.GuiKeyBindingsListing;
import com.mattsmeets.macrokey.object.BoundKey;
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
public class GuiManageKeybindings extends GuiScreen implements GuiYesNoCallback {

    private GuiKeyBindingsListing keyBindingList;

    private GuiScreen parentScreen;
    private GameSettings options;

    private GuiButton layerEditor;
    private GuiButton layerSwitcher;

    public BoundKey boundKey;

    protected String screenTitle = I18n.format("gui.keybindings.screenTitle", new Object[0]);

    private GuiButton buttonDone;
    private GuiButton buttonAdd;

    public int currentSelectedLayer;

    private static boolean updateList=false;

    public GuiManageKeybindings(GuiScreen screen, GameSettings settings){
        this.parentScreen = screen;
        this.options = settings;
        currentSelectedLayer = -1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);
        buttonDone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
        buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);

        layerEditor.drawButton(Minecraft.getMinecraft(), mouseX,mouseY);

        if(currentSelectedLayer==-1){
            layerSwitcher.displayString = "Layer: Master";
        }else {
            layerSwitcher.displayString = "Layer: " + MacroKey.instance.layers.get(currentSelectedLayer).getDisplayName();
        }

        layerSwitcher.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);

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
        if(button.id == 2){
            this.mc.displayGuiScreen(new GuiManageLayers(this, mc.gameSettings));
        }
        if(button.id == 3){
            if(currentSelectedLayer<MacroKey.instance.layers.size()-1) {
                currentSelectedLayer++;
            }else {
                currentSelectedLayer=-1;
            }
            updateList=true;
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(buttonDone = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.keybindings.addkeybinding", new Object[0])));

        this.buttonList.add(layerEditor = new GuiButton(2, this.width / 2 - 155 + 160, 40, 150, 20, "Layer Editor"));
        this.buttonList.add(layerSwitcher = new GuiButton(3, this.width / 2 - 155, 40, 150, 20, "Switch Layer"));


        if (currentSelectedLayer > MacroKey.instance.layers.size()-1 || currentSelectedLayer == -1) {
            currentSelectedLayer = -1;
            this.keyBindingList = new GuiKeyBindingsListing(this, this.mc, null);
        } else {
            this.keyBindingList = new GuiKeyBindingsListing(this, this.mc, MacroKey.instance.layers.get(currentSelectedLayer));
        }
    }


    @Override
    public void updateScreen()
    {
        super.updateScreen();
        if(updateList){
            if(currentSelectedLayer==-1) {
                this.keyBindingList = new GuiKeyBindingsListing(this, this.mc, null);
            }else{
                this.keyBindingList = new GuiKeyBindingsListing(this, this.mc, MacroKey.instance.layers.get(currentSelectedLayer));
            }
            updateList=false;
        }

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.boundKey != null)
        {
            if (keyCode == 1)
            {
                this.boundKey.setKeyCode(0);
            }
            else if (keyCode != 0)
            {
                this.boundKey.setKeyCode(keyCode);
            }
            else if (typedChar > 0)
            {
                this.boundKey.setKeyCode(typedChar + 256);
            }

            this.boundKey = null;
        }
        else
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.boundKey != null)
        {
            this.boundKey = null;
        }
        else
        if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton))
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if(buttonDone.mousePressed(mc, mouseX, mouseY)){
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }



    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.keyBindingList.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
