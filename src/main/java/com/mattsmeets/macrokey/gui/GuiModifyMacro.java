package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.factory.CommandFactory;
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
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

public class GuiModifyMacro extends GuiScreen {
    private final GuiScreen parentScreen;

    private final String
            defaultScreenTitleText = I18n.format("gui.modify.text.title.new"),
            editScreenTitleText = I18n.format("gui.modify.text.title.edit"),
            commandBoxTitleText = I18n.format("gui.modify.text.command"),
            optionsTitleText = I18n.format("gui.modify.text.options"),
            macroNameTitleText = I18n.format("gui.modify.text.name"),
            keyBoxTitleText = I18n.format("gui.modify.text.key"),
            saveButtonText = I18n.format("gui.modify.text.save"),
            openInEditorButtonText = I18n.format("gui.modify.text.options.openInEditor");

    private final String
            yesText = I18n.format("yes"),
            noText = I18n.format("no");

    private final String
            disabledText = I18n.format("disabled"),
            cancelText = I18n.format("gui.cancel");

    private final boolean existing;
    private final MacroInterface result;

    private GuiTextField name, command;

    private GuiButton btnKeyBinding;
    private GuiButton repeatCommand, commandActive, commandType;
    private GuiButton addButton, cancelButton;

    private boolean changingKey = false;

    private CommandFactory.CommandType currentType;

    public GuiModifyMacro(GuiScreen guiScreen, MacroInterface key) {
        // does the macro already exist, if not create a new one
        this.result = key == null ? new Macro() : key;
        this.currentType = key == null || key.getCommand() == null ? CommandFactory.CommandType.STRING : CommandFactory.CommandType.valueOfId(key.getCommand().getCommandType());
        this.parentScreen = guiScreen;
        this.existing = key != null;
    }

    public GuiModifyMacro(GuiScreen guiScreen) {
        this(guiScreen, null);
    }

    @Override
    public void initGui() {
        super.initGui();

        // input fields and button left
        this.name = new GuiTextField(15, this.fontRenderer, this.width / 2 - 150, height / 2 - 75, 200, 20);
        this.command = new GuiTextField(9, this.fontRenderer, this.width / 2 - 150, height / 2 - 25, 200, 20);
        this.buttonList.add(this.btnKeyBinding = new GuiButton(3, this.width / 2 - 125, height / 2 + 25, 150, 20, GameSettings.getKeyDisplayString(0)));

        this.name.setMaxStringLength(Integer.MAX_VALUE);
        this.command.setFocused(true);
        this.command.setMaxStringLength(Integer.MAX_VALUE);

        if (this.existing) {
            this.command.setText(result.getCommand().toString());
            this.name.setText(result.getName());
            if (this.currentType == CommandFactory.CommandType.JAVASCRIPT) {
                this.command.setFocused(false);
                this.name.setFocused(true);
            }
        }

        // option buttons
        this.buttonList.add(this.commandType = new GuiButton(5, this.width / 2 + 75, height / 2 - 50, 100, 20, disabledText));
        this.buttonList.add(this.commandActive = new GuiButton(5, this.width / 2 + 75, height / 2 - 25, 100, 20, disabledText));
        this.buttonList.add(this.repeatCommand = new GuiButton(4, this.width / 2 + 75, height / 2, 100, 20, disabledText));

        // bottom buttons
        this.buttonList.add(this.addButton = new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, saveButtonText));
        this.buttonList.add(this.cancelButton = new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, cancelText));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0:
                // the command must be at least one char
                if (this.command.getText().length() <= 1) {
                    break;
                }

                this.result.setCommand(CommandFactory.create(
                        this.currentType.getId(),
                        command.getText())
                );

                this.result.setName(name.getText());

                if (this.existing) {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(this.result));
                } else {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroAddedEvent(this.result));
                }
            case 1:
                this.mc.displayGuiScreen(parentScreen);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();

        // draw title and subtitles
        this.drawCenteredString(this.fontRenderer, existing ? this.editScreenTitleText : this.defaultScreenTitleText, this.width / 2, 8, 16777215);
        this.drawCenteredString(this.fontRenderer, optionsTitleText, this.width / 2 + 125, height / 2 - 75, -6250336);
        this.drawCenteredString(this.fontRenderer, macroNameTitleText, this.width / 2 - 50, height / 2 - 90, -6250336);
        this.drawCenteredString(this.fontRenderer, commandBoxTitleText, this.width / 2 - 50, height / 2 - 40, -6250336);
        this.drawCenteredString(this.fontRenderer, keyBoxTitleText, this.width / 2 - 50, height / 2 + 10, -6250336);

        // render add and cancel buttons
        this.addButton.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);
        this.cancelButton.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);

        // draw keycode as keyboard key
        this.btnKeyBinding.displayString = GameSettings.getKeyDisplayString(this.result.getKeyCode());

        // option buttons
        this.commandType.displayString = I18n.format("gui.modify.text.options.type", I18n.format("text.command.type." + currentType.getId()));
        this.commandActive.displayString = I18n.format("gui.modify.text.options.active", this.result.isActive() ? yesText : noText);
        this.repeatCommand.displayString = I18n.format("gui.modify.text.options.repeat", this.result.willRepeat() ? yesText : noText);

        if (this.currentType == CommandFactory.CommandType.JAVASCRIPT) {
            this.repeatCommand.displayString = openInEditorButtonText;
            this.command.setEnabled(false);
            this.result.setRepeat(false);
        } else {
            this.command.setEnabled(true);
        }

        this.repeatCommand.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);
        this.commandActive.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);
        this.commandType.drawButton(parentScreen.mc, mouseX, mouseY, 0.0f);

        // draw left controls
        this.name.drawTextBox();
        this.command.drawTextBox();

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
        if (this.changingKey) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                this.result.setKeyCode(0);
            } else if (keyCode != 0) {
                this.result.setKeyCode(keyCode);
            } else if (typedChar > 0) {
                this.result.setKeyCode(typedChar + 256);
            }

            this.changingKey = false;

            return;
        }

        if (this.command.isFocused() || this.name.isFocused()) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                this.command.setFocused(false);
                this.name.setFocused(false);
            }

            this.command.textboxKeyTyped(typedChar, keyCode);
            this.name.textboxKeyTyped(typedChar, keyCode);
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.command.mouseClicked(mouseX, mouseY, mouseButton);
        this.name.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.changingKey) {
            this.changingKey = false;
        }

        if (this.btnKeyBinding.mousePressed(mc, mouseX, mouseY)) {
            this.changingKey = true;
        }

        if (this.repeatCommand.mousePressed(mc, mouseX, mouseY)) {
            if (this.currentType == CommandFactory.CommandType.JAVASCRIPT) {
                File file = MacroKey.instance.javascriptFileHelper.getMacroFile(this.result.getUMID());
                this.command.setText(file.getCanonicalPath());

                try {
                    Runtime.getRuntime().exec(ModConfig.editor + " " + file.getCanonicalPath());
                } catch (IOException e) {
                    Desktop.getDesktop().edit(file);
                }
            } else {
                this.result.setRepeat(!this.result.willRepeat());
            }
        }

        if (this.commandActive.mousePressed(mc, mouseX, mouseY)) {
            this.result.setActive(!this.result.isActive());
        }

        // Change command type
        if (this.commandType.mousePressed(mc, mouseX, mouseY)) {
            List<CommandFactory.CommandType> list = Arrays.asList(CommandFactory.CommandType.values());
            int index = list.indexOf(this.currentType);
            index++;

            if (index >= list.size()) {
                index = 0;
            }

            this.currentType = list.get(index);
            this.command.setText("");
        }
    }

    public void updateScreen() {
        this.command.updateCursorCounter();
        this.name.updateCursorCounter();
    }
}
