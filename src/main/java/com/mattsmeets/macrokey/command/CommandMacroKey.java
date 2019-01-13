package com.mattsmeets.macrokey.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
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
    public String getCommandName() {
        return "macrokey";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /macrokey [open / layer]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            this.subCommands.get("open").processCommand(sender, args);

            return;
        }

        if (this.subCommands.containsKey(args[0].toLowerCase())) {
            this.subCommands.get(args[0].toLowerCase()).processCommand(sender, args);

            return;
        }

        sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        if (args.length >= 1 && this.subCommands.containsKey(args[0].toLowerCase())) {
            return this.subCommands.get(args[0].toLowerCase()).addTabCompletionOptions(sender, args, pos);
        }

        List<String> list = new ArrayList<String>();
        list.add("open");
        list.add("layer");

        return list;
    }
}
