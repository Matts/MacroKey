package com.mattsmeets.macrokey.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandMacroKey extends CommandBase implements ICommand {

    private HashMap<String, ICommand> subCommands;

    public CommandMacroKey() {
        this.subCommands = new HashMap<>();

        subCommands.put("open", new CommandOpenGUI());
        subCommands.put("layer", new CommandLayer());
    }

    @Override
    public String getName() {
        return "macrokey";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "Usage: /macrokey [open / layer]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            this.subCommands.get("open").execute(server, sender, args);

            return;
        }

        if (this.subCommands.containsKey(args[0].toLowerCase())) {
            this.subCommands.get(args[0].toLowerCase()).execute(server, sender, args);

            return;
        }

        sender.sendMessage(new TextComponentString(this.getUsage(sender)));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length >= 1 && this.subCommands.containsKey(args[0].toLowerCase())) {
            return this.subCommands.get(args[0].toLowerCase()).getTabCompletions(server, sender, args, targetPos);
        }

        List<String> list = new ArrayList<String>();
        list.add("open");
        list.add("layer");

        return list;
    }
}
