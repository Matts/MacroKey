package com.mattsmeets.macrokey.command;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommandLayer extends StrippedCommand {
    public final String
            layerMasterText = I18n.format("text.layer.master");

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: /macrokey layer [toggle]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            return;
        }

        if (args.length == 1) {
            this.printLayerInformation(sender);

            return;
        }

        if (args[1].equals("toggle")) {
            this.nextLayer(sender, new String[] {args[0]});

            return;
        }

        sender.addChatMessage(new ChatComponentText(this.getCommandUsage(sender)));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> list = new ArrayList<String>();
        list.add("toggle");

        return list;
    }

    private void printLayerInformation(ICommandSender sender) {
        LayerInterface activeLayer = MacroKey.instance.modState.getActiveLayer();

        String layerDisplayName = layerMasterText;
        int countMacroEnabled = 0;

        if (activeLayer != null) {
            layerDisplayName = activeLayer.getDisplayName();
            countMacroEnabled = activeLayer.getMacros().size();
        } else {
            try {
                countMacroEnabled = MacroKey.instance.bindingsRepository.findAllMacros(false).size();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sender.addChatMessage(
                new ChatComponentText(
                        I18n.format(
                            "command.layer.information",
                            layerDisplayName,
                            countMacroEnabled
                        )
                )
        );
    }

    private void nextLayer(ICommandSender sender, String[] args) throws CommandException {
        try {
            MacroKey.instance.modState.nextLayer();

            this.processCommand(sender, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
