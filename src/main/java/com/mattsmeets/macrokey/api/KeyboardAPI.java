package com.mattsmeets.macrokey.api;

import org.lwjgl.input.Keyboard;

public class KeyboardAPI extends AbstractAPI {

    public int getKeyCode(String name) {
        return Keyboard.getKeyIndex(name);
    }

    public boolean isKeyDown(int keyCode) {
        return Keyboard.isKeyDown(keyCode);
    }

    public String getKeyName(int keycode) {
        return Keyboard.getKeyName(keycode);
    }



}
