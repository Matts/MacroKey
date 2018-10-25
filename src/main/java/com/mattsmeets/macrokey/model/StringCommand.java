package com.mattsmeets.macrokey.model;

import net.minecraft.client.entity.EntityPlayerSP;

/**
 * Old vanilla macrokey command execution
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
    public void execute(EntityPlayerSP player) {
        player.sendChatMessage(command);
    }

    @Override
    public String toString() {
        return command;
    }
}
