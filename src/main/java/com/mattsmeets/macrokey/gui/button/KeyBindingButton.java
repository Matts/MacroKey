package com.mattsmeets.macrokey.gui.button;

import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.lwjgl.glfw.GLFW;

public class KeyBindingButton extends Button {
    public KeyBindingButton(int p_93721_, int p_93722_, int p_93723_, int p_93724_, Component p_93725_, OnPress p_93726_) {
        super(p_93721_, p_93722_, p_93723_, p_93724_, p_93725_, p_93726_);
    }

    public void updateDisplayString(final MacroInterface macro, final boolean isListening) {
        String keyValue = GLFW.glfwGetKeyName(macro.getKeyCode(), 0);
        if(keyValue == null) {
            keyValue = "";
        }
        this.setMessage(new TextComponent(isListening ? ChatFormatting.WHITE + "> " + ChatFormatting.YELLOW + keyValue + ChatFormatting.WHITE + " <"
                : isMacroKeyAlreadyBind(macro) ? ChatFormatting.GOLD + keyValue
                : keyValue));
    }

    private static boolean isMacroKeyAlreadyBind(final MacroInterface macro) {
        if (macro.getKeyCode() != 0) {
            for (KeyMapping keybinding : Minecraft.getInstance().options.keyMappings) {
                if (keybinding.getKey().getValue() == macro.getKeyCode()) {
                    return true;
                }
            }
        }

        return false;
    }
}
