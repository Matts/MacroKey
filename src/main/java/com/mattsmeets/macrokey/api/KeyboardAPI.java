package com.mattsmeets.macrokey.api;

import org.graalvm.polyglot.HostAccess;
import org.lwjgl.input.Keyboard;

public class KeyboardAPI extends AbstractAPI {

    @HostAccess.Export
    public int getKeyCode(String name) {
        return Keyboard.getKeyIndex(name);
    }

    @HostAccess.Export
    public boolean isKeyDown(int keyCode) {
        return Keyboard.isKeyDown(keyCode);
    }

    @HostAccess.Export
    public String getKeyName(int keycode) {
        return Keyboard.getKeyName(keycode);
    }



}
