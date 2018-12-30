package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;

public class PlayerAPI extends AbstractAPI {

    public void Jump() {
        // only jump if player is on ground
        if (Minecraft.getMinecraft().player.onGround) {
            Minecraft.getMinecraft().player.jump();
        }
    }

    public void Forward() {
        pressButton(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(),1L);
    }

}
