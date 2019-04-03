package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.command.StringCommand;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

public class GuiModifyMacro extends GuiScreen {
    private final GuiScreen parentScreen;

    private final String defaultScreenTitleText = I18n.format("gui.modify.text.title.new");
    private final String editScreenTitleText = I18n.format("gui.modify.text.title.edit");
    private final String repeatOnHoldText = I18n.format("gui.modify.text.repeat");
    private final String enableCommandText = I18n.format("gui.modify.text.enable");
    private final String commandBoxTitleText = I18n.format("gui.modify.text.command");
    private final String keyBoxTitleText = I18n.format("gui.modify.text.key");
    private final String saveButtonText = I18n.format("gui.modify.text.save");

    private final String enabledText = I18n.format("enabled");
    private final String disabledText = I18n.format("disabled");
    private final String cancelText = I18n.format("gui.cancel");

    private final boolean isUpdatingMacro;
    private final MacroInterface macro;

    private GuiTextField textFieldCommand;

    private KeyBindingButton btnKeyBinding;

    private boolean changingKey = false;

    class KeyBindingButton extends GuiButton {
        KeyBindingButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            changingKey = true;
            updateDisplayString(macro, true, false);
        }

        void updateDisplayString(final MacroInterface macro, final boolean isListening, boolean isKeyAlreadyBind) {
            final String keyValue = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
            this.displayString = isListening ? TextFormatting.WHITE + "> " + TextFormatting.YELLOW + keyValue + TextFormatting.WHITE + " <"
                    : isKeyAlreadyBind ? TextFormatting.GOLD + keyValue
                    : keyValue;
        }
    }

    public GuiModifyMacro(final GuiScreen parentScreen, final MacroInterface macro) {
        this.parentScreen = parentScreen;
        this.isUpdatingMacro = macro != null;
        this.macro = isUpdatingMacro ? macro : new Macro();
    }

    GuiModifyMacro(GuiScreen guiScreen) {
        this(guiScreen, null);
    }

    @Override
    public void initGui() {
        super.initGui();

        // Add macro button
        this.addButton(new GuiButton(0, this.width / 2 - 155, this.height - 29, 150, 20, saveButtonText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (textFieldCommand.getText().length() <= 1) {
                    return;
                }

                macro.setCommand(new StringCommand(textFieldCommand.getText()));

                if (isUpdatingMacro) {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(macro));
                } else {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroAddedEvent(macro));
                }

                mc.displayGuiScreen(parentScreen);
            }
        });

        // Cancel button
        this.addButton(new GuiButton(1, this.width / 2 - 155 + 160, this.height - 29, 150, 20, cancelText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                mc.displayGuiScreen(parentScreen);
            }
        });

        // Modify key binding button
        this.btnKeyBinding = this.addButton(new KeyBindingButton(3, this.width / 2 - 75, 100, 150, 20, GLFW.glfwGetKeyName(macro.getKeyCode(), 0)));

        // Toggle macro repeat button
        this.addButton(new GuiButton(4, this.width / 2 - 75, 140, 75, 20, macro.willRepeat() ? enabledText : disabledText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                macro.setRepeat(!macro.willRepeat());

                this.displayString = macro.willRepeat() ? enabledText : disabledText;
            }
        });

        // Toggle macro active button
        this.addButton(new GuiButton(5, this.width / 2 - 75, 163, 75, 20, macro.isActive() ? enabledText : disabledText) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                macro.setActive(!macro.isActive());

                this.displayString = macro.isActive() ? enabledText : disabledText;
            }
        });

        // Command text field
        this.textFieldCommand = new GuiTextField(9, this.fontRenderer, this.width / 2 - 100, 50, 200, 20);
        this.textFieldCommand.setFocused(true);
        this.textFieldCommand.setMaxStringLength(Integer.MAX_VALUE);
        this.textFieldCommand.setText(this.isUpdatingMacro ? macro.getCommand().toString() : StringUtils.EMPTY);
        this.children.add(this.textFieldCommand);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Render title
        this.drawCenteredString(this.fontRenderer, isUpdatingMacro ? this.editScreenTitleText : this.defaultScreenTitleText, this.width / 2, 8, 0xFFFFFF);

        // Render labels
        // TODO : replace by GuiLabel
        this.drawString(this.fontRenderer, repeatOnHoldText, this.width / 2 + 50 - mc.fontRenderer.getStringWidth(repeatOnHoldText) - 140, 145, 0x5F5F60);
        this.drawString(this.fontRenderer, enableCommandText, this.width / 2 + 50 - mc.fontRenderer.getStringWidth(enableCommandText) - 140, 168, 0x5F5F60);
        this.drawCenteredString(this.fontRenderer, commandBoxTitleText, this.width / 2, 37, 0x5F5F60);
        this.drawCenteredString(this.fontRenderer, keyBoxTitleText, this.width / 2, 90, 0x5F5F60);

        // Render buttons & labels
        super.render(mouseX, mouseY, partialTicks);

        // Render text field
        this.textFieldCommand.drawTextField(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean charTyped(char keyValue, int modifier) {
        return this.textFieldCommand.charTyped(keyValue, modifier);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifier) {
        if (this.changingKey) {
            changingKey = false;

            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.macro.setKeyCode(0);
            } else if (keyCode != GLFW.GLFW_KEY_UNKNOWN) {
                this.macro.setKeyCode(keyCode);
            }

            this.btnKeyBinding.updateDisplayString(macro, false, isKeyAlreadyBind());

            return true;
        }

        if (this.textFieldCommand.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.textFieldCommand.setFocused(false);

                return true;
            }

            return this.textFieldCommand.keyPressed(keyCode, scanCode, modifier);
        }

        return super.keyPressed(keyCode, scanCode, modifier);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.textFieldCommand.setFocused(false);

        if (this.changingKey) {
            this.changingKey = false;
            this.btnKeyBinding.updateDisplayString(macro, false, isKeyAlreadyBind());
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean isKeyAlreadyBind() {
        if (this.macro.getKeyCode() != 0) {
            for (KeyBinding keybinding : mc.gameSettings.keyBindings) {
                if (keybinding.getKey().getKeyCode() == this.macro.getKeyCode()) {
                    return true;
                }
            }
        }

        return false;
    }
}
