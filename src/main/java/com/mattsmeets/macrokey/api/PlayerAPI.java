package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;
import org.graalvm.polyglot.HostAccess;

public class PlayerAPI extends AbstractAPI {
    public static class Position {
        @HostAccess.Export
        public double X() {
            return Minecraft.getMinecraft().player.posX;
        }

        @HostAccess.Export
        public double Y() {
            return Minecraft.getMinecraft().player.posY;
        }

        @HostAccess.Export
        public double Z() {
            return Minecraft.getMinecraft().player.posZ;
        }
    }

    @HostAccess.Export
    public String DisplayName() {
        return Minecraft.getMinecraft().player.getDisplayName().getUnformattedText();
    }

    @HostAccess.Export
    public String UUID() {
        return Minecraft.getMinecraft().player.getUniqueID().toString();
    }

    @HostAccess.Export
    public Position Pos = new Position();

    @HostAccess.Export
    public Action Action = new Action();

    public static class Action extends AbstractAPI {
        @HostAccess.Export
        public void Jump() {
            // only jump if player is on ground
            if (Minecraft.getMinecraft().player.onGround) {
                Minecraft.getMinecraft().player.jump();
            }
        }

        @HostAccess.Export
        public void Sneak(long milliseconds) {
            pressButton(Minecraft.getMinecraft().gameSettings.keyBindSneak, milliseconds);
        }

        @HostAccess.Export
        public void Forward(long milliseconds) {
            pressButton(Minecraft.getMinecraft().gameSettings.keyBindForward, milliseconds);
        }

        @HostAccess.Export
        public void Back(long milliseconds) {
            pressButton(Minecraft.getMinecraft().gameSettings.keyBindBack, milliseconds);
        }

        @HostAccess.Export
        public void Left(long milliseconds) {
            pressButton(Minecraft.getMinecraft().gameSettings.keyBindLeft, milliseconds);
        }

        @HostAccess.Export
        public void Right(long milliseconds) {
            pressButton(Minecraft.getMinecraft().gameSettings.keyBindRight, milliseconds);
        }
    }
}
