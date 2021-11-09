package com.mattsmeets.macrokey.model;

import com.mattsmeets.macrokey.model.command.CommandInterface;

import java.util.UUID;

public interface MacroInterface {

    /**
     * Get the identifier of the macro (used internally)
     * <p>
     * *Should* be immutable after setup.
     *
     * @return UUID unique macro identifier
     */
    UUID getUMID();

    /**
     * Get the keyCode the Macro should be bound to
     *
     * @return int keyCode
     */
    int getKeyCode();

    /**
     * Set the keyCode the Macro should be bound to
     *
     * @param keyCode the keyCode
     * @return the current Macro instance
     */
    Macro setKeyCode(int keyCode);

    /**
     * Get the actual command that will can run
     *
     * @return String command
     */
    CommandInterface getCommand();

    /**
     * Set the actual command that will can run
     *
     * @param command the command
     * @return the current Macro instance
     */
    Macro setCommand(CommandInterface command);

    /**
     * Is the Macro active?
     *
     * @return boolean isActive
     */
    boolean isActive();

    /**
     * Set the state of the Macro
     *
     * @param active isActive
     * @return the current Macro instance
     */
    Macro setActive(boolean active);

    /**
     * When holding button, should we repeat?
     *
     * @return repeat
     */
    boolean willRepeat();

    /**
     * Set the 'repeat' flag on a macro.
     *
     * @param repeat willRepeat
     * @return the current Macro instance
     */
    Macro setRepeat(boolean repeat);

    Macro setInterfaceType(int interfaceType);
    int getInterfaceType();

    Macro setModifier(int modifier);
    int getModifier();
}
