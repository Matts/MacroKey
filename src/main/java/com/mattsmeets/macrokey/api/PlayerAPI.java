package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;

public class PlayerAPI extends AbstractAPI {

    public String DisplayName() {
        return Minecraft.getMinecraft().player.getDisplayName().getUnformattedText();
    }

    public void Jump() {
        // only jump if player is on ground
        if (Minecraft.getMinecraft().player.onGround) {
            Minecraft.getMinecraft().player.jump();
        }
    }

    public void Sneak(long milliseconds) {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindSneak, milliseconds);
    }

    public void Forward(long milliseconds) {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindForward, milliseconds);
    }

    public void Back(long milliseconds) {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindBack, milliseconds);
    }

    public void Left(long milliseconds) {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindLeft, milliseconds);
    }

    public void Right(long milliseconds) {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindRight, milliseconds);
    }

}
