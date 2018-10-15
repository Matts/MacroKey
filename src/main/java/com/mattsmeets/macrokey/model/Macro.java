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
     * Hold key to repeat command
     */
    private boolean repeat;

    /**
     * StringCommand in string form
     */
    private String command;

    /**
     * If the macro is active (default: true)
     */
    private boolean active;

    public Macro(UUID umid, int keyCode, String command, boolean active, boolean repeat) {
        this.umid = umid;
        this.keyCode = keyCode;
        this.command = command;
        this.active = active;
        this.repeat = repeat;
    }

    public Macro(int keyCode, String command, boolean active, boolean repeat) {
        this(UUID.randomUUID(), keyCode, command, active, repeat);
    }

    public Macro(int keyCode, String command, boolean active) {
        this(UUID.randomUUID(), keyCode, command, active, false);
    }

    public Macro(int keyCode, String command) {
        this(keyCode, command, true);
    }

    public Macro() {
        this.umid = UUID.randomUUID();
        this.active = true;
        this.repeat = false;
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

    public boolean willRepeat() {
        return repeat;
    }

    public Macro setRepeat(boolean repeat) {
        this.repeat = repeat;

        return this;
    }

}
