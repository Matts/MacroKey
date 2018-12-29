package com.mattsmeets.macrokey.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class StrippedCommand implements ICommand {
    @Override
    public abstract void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

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
    public abstract List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return false;
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
