package com.mattsmeets.macrokey.model;

import net.minecraft.client.entity.EntityPlayerSP;

public final class StringCommand implements CommandInterface {

    private final String command;

    public StringCommand(String command) {
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
