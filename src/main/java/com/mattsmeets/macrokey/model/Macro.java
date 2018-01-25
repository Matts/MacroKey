package com.mattsmeets.macrokey.model;

import java.util.UUID;

/**
 * Model for Macro's (Bindings)
 */
public class Macro implements MacroInterface {

    /**
     * Unique Macro Identifier
     */
    private UUID umid;

    /**
     * Keycode of the button that is bound
     */
    private int keyCode;

    /**
     * Command in string form
     */
    private String command;

    /**
     * If the macro is active (default: true)
     */
    private boolean active;

    public Macro(UUID umid, int keyCode, String command, boolean active) {
        this.umid = umid;
        this.keyCode = keyCode;
        this.command = command;
        this.active = active;
    }

    public Macro(int keyCode, String command, boolean active) {
        this(UUID.randomUUID(), keyCode, command, active);
    }

    public Macro(int keyCode, String command) {
        this(keyCode, command, true);
    }

    public UUID getUMID() {
        return umid;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public Macro setKeyCode(int keyCode) {
        this.keyCode = keyCode;

        return this;
    }

    public String getCommand() {
        return command;
    }

    public Macro setCommand(String command) {
        this.command = command;

        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Macro setActive(boolean active) {
        this.active = active;

        return this;
    }
}
