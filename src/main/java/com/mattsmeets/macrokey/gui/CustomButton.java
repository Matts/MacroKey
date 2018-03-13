package com.mattsmeets.macrokey.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;

public class CustomButton extends GuiButton {
    public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public CustomButton(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
    }
}
