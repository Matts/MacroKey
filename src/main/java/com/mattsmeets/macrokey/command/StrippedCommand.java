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
    public abstract List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return null;
    }

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
