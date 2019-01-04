package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;

public class PlayerAPI extends AbstractAPI {
    public static class Position {
        public double X() {
            return Minecraft.getMinecraft().player.posX;
        }

        public double Y() {
            return Minecraft.getMinecraft().player.posY;
        }

        public double Z() {
            return Minecraft.getMinecraft().player.posZ;
        }
    }

    public String DisplayName() {
        return Minecraft.getMinecraft().player.getDisplayName().getUnformattedText();
    }

    public String UUID() {
        return Minecraft.getMinecraft().player.getUniqueID().toString();
    }

    public Position Pos = new Position();

    public Action Action = new Action();

public static class Action extends AbstractAPI {
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
}
