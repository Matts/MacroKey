package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.MacroChangedEvent;
import com.mattsmeets.macrokey.gui.fragment.MacroListFragment;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class GuiMacroManagement extends GuiScreen {
    private MacroListFragment keyBindingList;

    private GuiScreen parentScreen;
    private GameSettings options;

    private GuiButton layerEditor;
    private GuiButton layerSwitcher;

    public MacroInterface macroModify;

    protected String screenTitle = I18n.format("gui.keybindings.screenTitle");

    private GuiButton buttonDone;
    private GuiButton buttonAdd;

    public int currentSelectedLayer;
    private List<LayerInterface> layers;

    private static boolean updateList = false;

    public GuiMacroManagement(GuiScreen screen, GameSettings settings) throws IOException {
        this.parentScreen = screen;
        this.options = settings;
        currentSelectedLayer = -1;

        this.layers = new ArrayList<>(instance.bindingsRepository.findAllLayers(false));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.keyBindingList.drawScreen(mouseX, mouseY, partialTicks);

        buttonDone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);
        buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        layerEditor.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        if (currentSelectedLayer == -1) {
            layerSwitcher.displayString = "Layer: Master";
        } else {
            layerSwitcher.displayString = "Layer: " + this.layers.get(this.currentSelectedLayer).getDisplayName();
        }

        layerSwitcher.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
        /*if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiCreateKeybinding(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiManageLayers(this, mc.gameSettings));
        }*/
        if (button.id == 3) {
            if (currentSelectedLayer < this.layers.size() - 1) {
                currentSelectedLayer++;
            } else {
                currentSelectedLayer = -1;
            }
            updateList = true;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(buttonDone = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("gui.keybindings.addkeybinding", new Object[0])));

        this.buttonList.add(layerEditor = new GuiButton(2, this.width / 2 - 155 + 160, 40, 150, 20, "Layer Editor"));
        this.buttonList.add(layerSwitcher = new GuiButton(3, this.width / 2 - 155, 40, 150, 20, "Switch Layer"));

        if (currentSelectedLayer > this.layers.size() - 1 || currentSelectedLayer == -1) {
            currentSelectedLayer = -1;
            this.keyBindingList = new MacroListFragment(this, null);
        } else {
            this.keyBindingList = new MacroListFragment(this, this.layers.get(currentSelectedLayer));
        }
    }


    @Override
    public void updateScreen() {
        super.updateScreen();

        if (updateList) {
            if (currentSelectedLayer == -1) {
                this.keyBindingList = new MacroListFragment(this, null);
            } else {
                this.keyBindingList = new MacroListFragment(this, this.layers.get(currentSelectedLayer));
            }
        }
        
        updateList = false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.macroModify != null) {
            if (keyCode == 1) {
                this.macroModify.setKeyCode(0);
            } else if (keyCode != 0) {
                this.macroModify.setKeyCode(keyCode);
            } else if (typedChar > 0) {
                this.macroModify.setKeyCode(typedChar + 256);
            }

            MinecraftForge.EVENT_BUS.post(new MacroChangedEvent(this.macroModify));

            this.macroModify = null;
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.macroModify != null) {
            this.macroModify = null;
        } else if (mouseButton != 0 || !this.keyBindingList.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (buttonDone.mousePressed(mc, mouseX, mouseY)) {
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
