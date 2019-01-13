package com.mattsmeets.macrokey.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import java.util.List;

public abstract class StrippedCommand implements ICommand {

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getCommandAliases() {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
