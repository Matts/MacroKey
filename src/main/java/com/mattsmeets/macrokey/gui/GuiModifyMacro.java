package com.mattsmeets.macrokey.gui;

import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.gui.button.KeyBindingButton;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import com.mattsmeets.macrokey.model.command.StringCommand;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

public class GuiModifyMacro extends Screen {
    private final Screen parentScreen;

    private final String defaultScreenTitleText = I18n.get("gui.modify.text.title.new");
    private final String editScreenTitleText = I18n.get("gui.modify.text.title.edit");
    private final String repeatOnHoldText = I18n.get("gui.modify.text.repeat");
    private final String enableCommandText = I18n.get("gui.modify.text.enable");
    private final String commandBoxTitleText = I18n.get("gui.modify.text.command");
    private final String keyBoxTitleText = I18n.get("gui.modify.text.key");
    private final String saveButtonText = I18n.get("gui.modify.text.save");

    private final String enabledText = I18n.get("enabled");
    private final String disabledText = I18n.get("disabled");
    private final String cancelText = I18n.get("gui.cancel");

    private final boolean isUpdatingMacro;
    private final MacroInterface macro;

    private EditBox textFieldCommand;

    private KeyBindingButton btnKeyBinding;

    private boolean changingKey = false;

    public GuiModifyMacro(final Screen parentScreen, final MacroInterface macro) {
        super(new TextComponent("Test"));

        this.parentScreen = parentScreen;
        this.isUpdatingMacro = macro != null;
        this.macro = isUpdatingMacro ? macro : new Macro();
    }

    GuiModifyMacro(Screen Screen) {
        this(Screen, null);
    }

    @Override
    public void init() {
        super.init();

        // Add macro button
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TextComponent(saveButtonText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                if (textFieldCommand.getValue().length() <= 1) {
                    return;
                }

                macro.setCommand(new StringCommand(textFieldCommand.getValue()));

                if (isUpdatingMacro) {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroChangedEvent(macro));
                } else {
                    MinecraftForge.EVENT_BUS.post(new MacroEvent.MacroAddedEvent(macro));
                }

                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        // Cancel button
        this.addRenderableWidget(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, new TextComponent(cancelText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                Minecraft.getInstance().setScreen(parentScreen);
            }
        });

        String buttonName = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
        if(buttonName == null) {
            buttonName = "";
        }

        // Modify key binding button
        this.btnKeyBinding = this.addRenderableWidget(new KeyBindingButton(this.width / 2 - 75, 100, 150, 20, new TextComponent(buttonName), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                changingKey = true;
                this.updateDisplayString(macro, true);
            }
        });
        this.btnKeyBinding.updateDisplayString(this.macro, false);

        // Toggle macro repeat button
        this.addRenderableWidget(new Button(this.width / 2 - 75, 140, 75, 20, new TextComponent(macro.willRepeat() ? enabledText : disabledText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                macro.setRepeat(!macro.willRepeat());

                this.setMessage(new TextComponent(macro.willRepeat() ? enabledText : disabledText));
            }
        });

        // Toggle macro active button
        this.addRenderableWidget(new Button(this.width / 2 - 75, 163, 75, 20, new TextComponent(macro.isActive() ? enabledText : disabledText), Button::onPress) {
            @Override
            public void onClick(double mouseX, double mouseY) {
                macro.setActive(!macro.isActive());

                this.setMessage(new TextComponent(macro.isActive() ? enabledText : disabledText));
            }
        });

        // Command text field
        this.textFieldCommand = new EditBox(this.font, this.width / 2 - 100, 50, 200, 20, new TextComponent("test"));
        this.textFieldCommand.setFocus(true);
        this.textFieldCommand.setMaxLength(Integer.MAX_VALUE);
        this.textFieldCommand.setValue(this.isUpdatingMacro ? macro.getCommand().toString() : StringUtils.EMPTY);
        this.addRenderableWidget(this.textFieldCommand);
    }

    @Override
    public void render(PoseStack ps, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ps);

        // Render title
        drawCenteredString(ps, this.font, isUpdatingMacro ? this.editScreenTitleText : this.defaultScreenTitleText, this.width / 2, 8, 0xFFFFFF);

        // Render labels
        // TODO : replace by GuiLabel ?

        drawString(ps, this.font, repeatOnHoldText, this.width / 2 + 50 - Minecraft.getInstance().font.width(repeatOnHoldText) - 140, 145, 0xFFFFFF);
        drawString(ps, this.font, enableCommandText, this.width / 2 + 50 - Minecraft.getInstance().font.width(enableCommandText) - 140, 168, 0xFFFFFF);
        drawCenteredString(ps, this.font, commandBoxTitleText, this.width / 2, 37, 0xFFFFFF);
        drawCenteredString(ps, this.font, keyBoxTitleText, this.width / 2, 90, 0xFFFFFF);

        // Render buttons & labels
        super.render(ps, mouseX, mouseY, partialTicks);

        // Render text field
        this.textFieldCommand.renderButton(ps, mouseX, mouseY, partialTicks);
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

            this.btnKeyBinding.updateDisplayString(macro, false);

            return true;
        }

        if (this.textFieldCommand.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.textFieldCommand.setFocus(false);

                return true;
            }

            return this.textFieldCommand.keyPressed(keyCode, scanCode, modifier);
        }

        return super.keyPressed(keyCode, scanCode, modifier);
    }

    @Override
    public void tick() {
        this.textFieldCommand.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        this.textFieldCommand.setFocus(false);

        if (this.changingKey) {
            this.changingKey = false;
            this.btnKeyBinding.updateDisplayString(macro, false);
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}
