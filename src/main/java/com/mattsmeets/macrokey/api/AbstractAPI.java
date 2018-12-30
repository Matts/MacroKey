package com.mattsmeets.macrokey.api;

import net.minecraft.client.settings.KeyBinding;

public abstract class AbstractAPI {
    public void pressButton(int keyCode, long time) {
        KeyBinding.setKeyBindState(keyCode, true);
        KeyBinding.setKeyBindState(keyCode, false);
    }
}
