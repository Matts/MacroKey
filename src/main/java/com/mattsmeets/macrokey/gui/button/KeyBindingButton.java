package com.mattsmeets.macrokey.gui.button;

import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

public class KeyBindingButton extends GuiButton {
    protected KeyBindingButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public void updateDisplayString(final MacroInterface macro, final boolean isListening) {
        final String keyValue = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
        this.displayString = isListening ? TextFormatting.WHITE + "> " + TextFormatting.YELLOW + keyValue + TextFormatting.WHITE + " <"
                : isMacroKeyAlreadyBind(macro) ? TextFormatting.GOLD + keyValue
                : keyValue;
    }

    private static boolean isMacroKeyAlreadyBind(final MacroInterface macro) {
        if (macro.getKeyCode() != 0) {
            for (KeyBinding keybinding : Minecraft.getInstance().gameSettings.keyBindings) {
                if (keybinding.getKey().getKeyCode() == macro.getKeyCode()) {
                    return true;
                }
            }
        }

        return false;
    }
}
