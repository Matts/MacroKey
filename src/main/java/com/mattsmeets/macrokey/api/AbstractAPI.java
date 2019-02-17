package com.mattsmeets.macrokey.api;

import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public abstract class AbstractAPI {
    public void pressButton(KeyBinding key, long milliseconds) {
        this.pressButton(key.getKeyCode(), milliseconds);
    }

    public void pressButton(int keyCode, long milliseconds) {
        KeyBinding.setKeyBindState(keyCode, true);

        long releaseTime = System.currentTimeMillis() + milliseconds;

        MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(
                (delayed) -> {
                    if (System.currentTimeMillis() >= releaseTime) {
                        KeyBinding.setKeyBindState(keyCode, false);
                        return true;
                    }

                    return false;
                })
        );

    }

    public static void notYetImplemented() {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString("PSST: This function has not yet been implemented."));
    }
}
