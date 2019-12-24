package com.mattsmeets.macrokey.api;

import org.graalvm.polyglot.HostAccess;
import org.lwjgl.input.Keyboard;

/**
 * KeyboardAPI allows you to implement your own custom keyboard functionality.
 *
 * @since 2.1
 */
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
