package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerAPI extends AbstractAPI {

    private static GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
    private static EntityPlayer player = Minecraft.getMinecraft().player;

    public void Jump() {
        // only jump if player is on ground
        if (player.onGround) {
            player.jump();
        }
    }

    public void Sneak(long milliseconds) {
        pressButton(gameSettings.keyBindSneak, milliseconds);
    }

    public void Forward(long milliseconds) {
        pressButton(gameSettings.keyBindForward, milliseconds);
    }

    public void Back(long milliseconds) {
        pressButton(gameSettings.keyBindBack, milliseconds);
    }

    public void Left(long milliseconds) {
        pressButton(gameSettings.keyBindLeft, milliseconds);
    }

    public void Right(long milliseconds) {
        pressButton(gameSettings.keyBindRight, milliseconds);
    }

}
