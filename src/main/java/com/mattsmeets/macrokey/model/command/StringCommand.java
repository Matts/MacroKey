package com.mattsmeets.macrokey.model.command;

import net.minecraft.client.entity.player.ClientPlayerEntity;

/**
 * Old vanilla MacroKey command execution
 */
public class StringCommand extends AbstractCommand implements CommandInterface {

    /**
     * Command to execute
     */
    private final String command;

    public StringCommand(String command) {
        super("string");

        this.command = command;
    }

    @Override
    public void execute(final ClientPlayerEntity player) {
        // send command or text to server. For the time being it is
        // not possible to execute client-only commands. Tested and its
        // cool that the mod can bind its own GUI to different keys
        // from within the GUI, but this caused some weird issues
        player.chat(command);
    }

    @Override
    public String toString() {
        return command;
    }
}
