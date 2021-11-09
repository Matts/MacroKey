package com.mattsmeets.macrokey.model;

import com.mattsmeets.macrokey.model.command.CommandInterface;
import com.mattsmeets.macrokey.model.command.StringCommand;

import java.util.UUID;

/**
 * Model for Macro's (Bindings)
 */
public class Macro implements MacroInterface {

    /**
     * Unique Macro Identifier
     */
    private UUID umid;

    // 0 = keyboard, 1 = mouse
    private int interfaceType = 0;

    // 0 = no modifier
    private int modifier = 0;

    /**
     * Key code of the button that is bound
     */
    private int keyCode;

    /**
     * Hold key to repeat command
     */
    private boolean repeat;

    /**
     * Command in string form
     */
    private CommandInterface command;

    /**
     * If the macro is active (default: true)
     */
    private boolean active;

    public Macro(UUID umid, int keyCode, CommandInterface command, boolean active, boolean repeat) {
        this.umid = umid;
        this.keyCode = keyCode;
        this.command = command;
        this.active = active;
        this.interfaceType = 0;
        this.modifier = 0;
        this.repeat = repeat;
    }

    public Macro(UUID umid, int keyCode, CommandInterface command, int interfaceType, int modifier, boolean active, boolean repeat) {
        this.umid = umid;
        this.keyCode = keyCode;
        this.command = command;
        this.active = active;
        this.interfaceType = interfaceType;
        this.modifier = modifier;
        this.repeat = repeat;
    }

    private Macro(int keyCode, CommandInterface command, boolean active) {
        this(UUID.randomUUID(), keyCode, command, active, false);
    }

    public Macro(int keyCode, String command, boolean active) {
        this(keyCode, new StringCommand(command), active);
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

    public CommandInterface getCommand() {
        return command;
    }

    public Macro setCommand(CommandInterface command) {
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

    @Override
    public Macro setInterfaceType(int interfaceType) {
        this.interfaceType = interfaceType;

        return this;
    }

    @Override
    public int getInterfaceType() {
        return 0;
    }

    @Override
    public Macro setModifier(int modifier) {
        this.modifier = modifier;

return this;
    }

    @Override
    public int getModifier() {
        return 0;
    }

}
