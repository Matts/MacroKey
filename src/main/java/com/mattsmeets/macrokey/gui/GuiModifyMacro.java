package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiModifyMacro extends GuiScreen {
    private final GuiScreen parentScreen;

    private final String
            defaultScreenTitleText = I18n.format("gui.modify.text.title.new"),
            editScreenTitleText = I18n.format("gui.modify.text.title.edit"),
            repeatOnHoldText = I18n.format("gui.modify.text.repeat"),
            enableCommandText = I18n.format("gui.modify.text.enable"),
            commandBoxTitleText = I18n.format("gui.modify.text.command"),
            keyBoxTitleText = I18n.format("gui.modify.text.key"),
            saveButtonText = I18n.format("gui.modify.text.save");

    private final String
            enabledText = I18n.format("enabled"),
            disabledText = I18n.format("disabled"),
            cancelText = I18n.format("gui.cancel");

    private final boolean existing;

    private GuiTextField command;

    private GuiButton btnKeyBinding;
    private GuiButton repeatCommand, commandActive;
    private GuiButton addButton, cancelButton;

    private boolean changingKey = false;
    private MacroInterface result;

    private boolean repeatEnabled = false;
    private boolean isCommandActive = true;

    public GuiModifyMacro(GuiScreen guiScreen, MacroInterface key) {
        this.result = key == null ? new Macro() : key;
        this.parentScreen = guiScreen;
        this.existing = key != null;

        if (existing) {
            this.repeatEnabled = key.willRepeat();
            this.isCommandActive = key.isActive();
        }
    }

    public GuiModifyMacro(GuiScreen guiScreen) {
        this(guiScreen, null);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(addButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, saveButtonText));
        this.buttonList.add(cancelButton = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, cancelText));

        this.buttonList.add(this.btnKeyBinding = new GuiButton(3, this.width / 2 - 75, 100, 150, 20, GameSettings.getKeyDisplayString(0)));
        this.buttonList.add(this.repeatCommand = new GuiButton(4, this.width / 2 - 75, 140, 75, 20, disabledText));
        this.buttonList.add(this.commandActive = new GuiButton(5, this.width / 2 - 75, 163, 75, 20, disabledText));

        this.command = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 50, 200, 20);
        this.command.setFocused(true);
        this.command.setMaxStringLength(Integer.MAX_VALUE);

        if (existing) {
            command.setText(result.getCommand());

            this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(result.getKeyCode());
            this.repeatCommand.displayString = repeatEnabled ? enabledText : disabledText;
            this.commandActive.displayString = isCommandActive ? enabledText : disabledText;
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                if (command.getText().length() <= 1) {
                    break;
                }

                result.setCommand(command.getText());

                if (existing) {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(this.result));
                } else {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroAddedEvent(this.result));
                }

                this.mc.displayGuiScreen(parentScreen);

                break;
            case 1:
                this.mc.displayGuiScreen(parentScreen);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();

        // draw title
        this.drawCenteredString(this.fontRenderer, existing ? this.editScreenTitleText : this.defaultScreenTitleText, this.width / 2, 8, 16777215);

        // render add and cancel buttons
        addButton.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);
        cancelButton.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);

        // draw keycode as keyboard key
        this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(this.result.getKeyCode());

        this.repeatCommand.displayString = repeatEnabled ? enabledText : disabledText;
        this.commandActive.displayString = isCommandActive ? enabledText : disabledText;

        repeatCommand.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);
        commandActive.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);

        this.command.drawTextBox();

        this.drawString(this.fontRenderer, repeatOnHoldText, this.width / 2 + 50 - mc.fontRenderer.getStringWidth(repeatOnHoldText) - 140, 145, -6250336);
        this.drawString(this.fontRenderer, enableCommandText, this.width / 2 + 50 - mc.fontRenderer.getStringWidth(enableCommandText) - 140, 168, -6250336);
        this.drawString(this.fontRenderer, commandBoxTitleText, this.width / 2 + 67 - mc.fontRenderer.getStringWidth(commandBoxTitleText), 37, -6250336);
        this.drawString(this.fontRenderer, keyBoxTitleText, this.width / 2 + 67 - mc.fontRenderer.getStringWidth(keyBoxTitleText), 90, -6250336);

        this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(this.result.getKeyCode());

        boolean macroKeyCodeModifyFlag = false;

        if (this.result.getKeyCode() != 0) {
            for (KeyBinding keybinding : mc.gameSettings.keyBindings) {
                if (keybinding.getKeyCode() == this.result.getKeyCode()) {
                    macroKeyCodeModifyFlag = true;
                    break;
                }
            }
        }

        if (this.changingKey) {
            this.btnKeyBinding.displayString = TextFormatting.WHITE + "> " + TextFormatting.YELLOW + this.btnKeyBinding.displayString + TextFormatting.WHITE + " <";
        } else if (macroKeyCodeModifyFlag) {
            this.btnKeyBinding.displayString = TextFormatting.GOLD + this.btnKeyBinding.displayString;
        }

        this.btnKeyBinding.drawButton(mc, mouseX, mouseY, 0.0f);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (changingKey) {
            if (keyCode == 1) {
                this.result.setKeyCode(0);
            } else if (keyCode != 0) {
                this.result.setKeyCode(keyCode);
            } else if (typedChar > 0) {
                this.result.setKeyCode(typedChar + 256);
            }

            this.changingKey = false;
        } else if (this.command.isFocused()) {
            this.command.textboxKeyTyped(typedChar, keyCode);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.command.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.changingKey) {
            this.changingKey = false;
        }

        if (this.btnKeyBinding.mousePressed(mc, mouseX, mouseY)) {
            changingKey = true;
        }
        if (this.repeatCommand.mousePressed(mc, mouseX, mouseY)) {
            repeatEnabled = !repeatEnabled;
            result.setRepeat(repeatEnabled);
        }
        if (this.commandActive.mousePressed(mc, mouseX, mouseY)) {
            isCommandActive = !isCommandActive;
            result.setActive(isCommandActive);
        }
    }

    public void updateScreen() {
        this.command.updateCursorCounter();
    }
}
