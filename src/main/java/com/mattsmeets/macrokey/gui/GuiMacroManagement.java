package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.gui.fragment.MacroListFragment;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class GuiMacroManagement extends GuiScreen {

    private MacroListFragment macroListFragment;

    private final GuiScreen parentScreen;

    public MacroInterface macroModify;

    private final String
            screenTitle = I18n.format("gui.manage.text.title"),
            layerMasterText = I18n.format("text.layer.master"),
            addMacroButtonText = I18n.format("gui.manage.text.macro.add"),
            layerEditorButtonText = I18n.format("gui.manage.text.layer.edit"),
            layerSwitcherButtonText = I18n.format("gui.manage.text.layer.switch");

    private final String
            doneText = I18n.format("gui.done");

    private GuiButton
            buttonDone,
            buttonAdd,
            layerEditor,
            layerSwitcher;

    private int currentSelectedLayer;
    private List<LayerInterface> layers;

    private volatile boolean updateList = false;

    public GuiMacroManagement(GuiScreen screen) {
        this.parentScreen = screen;
        this.currentSelectedLayer = -1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.updateList) {
            this.updateScreen();
        }

        this.macroListFragment.drawScreen(mouseX, mouseY, partialTicks);

        this.buttonDone.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);
        this.buttonAdd.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        this.layerEditor.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        this.layerSwitcher.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, 0.0f);

        this.drawCenteredString(this.fontRenderer, this.screenTitle, this.width / 2, 8, 16777215);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiModifyMacro(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiLayerManagement(this));
                break;
            case 3:
                if (this.currentSelectedLayer < this.layers.size() - 1) {
                    this.currentSelectedLayer++;
                } else {
                    this.currentSelectedLayer = -1;
                }

                this.updateList = true;
                break;
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, this.doneText));
        this.buttonList.add(this.buttonAdd = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, this.addMacroButtonText));

        this.buttonList.add(this.layerEditor = new GuiButton(2, this.width / 2 - 155 + 160, 40, 150, 20, this.layerEditorButtonText));
        this.buttonList.add(this.layerSwitcher = new GuiButton(3, this.width / 2 - 155, 40, 150, 20, this.layerSwitcherButtonText));

        this.updateList = true;
    }


    @Override
    public void updateScreen() {
        super.updateScreen();

        if (!this.updateList) {
            return;
        }

        try {
            this.layers = instance.modState.getLayers(true);

            LayerInterface currentLayer =
                    currentSelectedLayer == -1 ? null : this.layers.get(currentSelectedLayer);

            this.macroListFragment = new MacroListFragment(this, currentLayer);

            this.layerSwitcher.displayString =
                    I18n.format("text.layer.display",
                            currentLayer == null ? this.layerMasterText : currentLayer.getDisplayName()
                    );
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.updateList = false;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (this.macroModify == null) {
            super.keyTyped(typedChar, keyCode);

            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE) {
            this.macroModify.setKeyCode(0);
        } else if (keyCode != 0) {
            this.macroModify.setKeyCode(keyCode);
        } else if (typedChar > 0) {
            this.macroModify.setKeyCode(typedChar + 256);
        }

        MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(this.macroModify));

        this.macroModify = null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.macroModify != null) {
            this.macroModify = null;
        } else if (mouseButton != 0 || !this.macroListFragment.mouseClicked(mouseX, mouseY, mouseButton)) {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }


    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        this.macroListFragment.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
