package com.mattsmeets.macrokey.gui.button;

import org.lwjgl.glfw.GLFW;

import com.mattsmeets.macrokey.model.MacroInterface;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class KeyBindingButton extends Button {
    public KeyBindingButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, ITextComponent p_93725_, IPressable p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
    }

    public void updateDisplayString(final MacroInterface macro, final boolean isListening) {
        String keyValue = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
        if(keyValue == null) {
            keyValue = I18n.get("key.keyboard.unknown");
        }
        this.setMessage(new StringTextComponent(isListening ? TextFormatting.WHITE + "> " + TextFormatting.YELLOW + keyValue + TextFormatting.WHITE + " <"
                : isMacroKeyAlreadyBind(macro) ? TextFormatting.GOLD + keyValue
                : keyValue));
    }

    private static boolean isMacroKeyAlreadyBind(final MacroInterface macro) {
        if (macro.getKeyCode() != 0) {
            for (KeyBinding keybinding : Minecraft.getInstance().options.keyMappings) {
                if (keybinding.getKey().getValue() == macro.getKeyCode()) {
                    return true;
                }
            }
        }

        return false;
    }
}
