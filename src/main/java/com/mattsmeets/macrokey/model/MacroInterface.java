package com.mattsmeets.macrokey.model;

import java.util.UUID;

public interface MacroInterface {

    /**
     * Get the identifier of the macro (used internally)
     * <p>
     * *Should* be immutable after setup.
     *
     * @return UUID unique macro identifier
     */
    public UUID getUMID();

    /**
     * Get the keyCode the Macro should be bound to
     *
     * @return int keyCode
     */
    public int getKeyCode();

    /**
     * Set the keyCode the Macro should be bound to
     *
     * @param keyCode the keyCode
     * @return the current Macro instance
     */
    public Macro setKeyCode(int keyCode);

    /**
     * Get the actual command that will can run
     *
     * @return String command
     */
    public CommandInterface getCommand();

    /**
     * Set the actual command that will can run
     *
     * @param command the command
     * @return the current Macro instance
     */
    public Macro setCommand(CommandInterface command);

    /**
     * Is the Macro active?
     *
     * @return boolean isActive
     */
    public boolean isActive();

    /**
     * Set the state of the Macro
     *
     * @param active isActive
     * @return the current Macro instance
     */
    public Macro setActive(boolean active);

    /**
     * When holding button, should we repeat?
     *
     * @return repeat
     */
    public boolean willRepeat();

    /**
     * Set the 'repeat' flag on a macro.
     *
     * @param repeat willRepeat
     * @return the current Macro instance
     */
    public Macro setRepeat(boolean repeat);

    /**
     * Get the 'name' / 'description' of a macro
     *
     * @return name
     */
    public String getName();

    /**
     * Set the 'name' / 'description' on a macro.
     *
     * @param name description
     * @return the current Macro instance
     */
    public Macro setName(String name);

}
