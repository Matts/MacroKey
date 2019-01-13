package com.mattsmeets.macrokey.command;

import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class CommandOpenGUI extends StrippedCommand {

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));

    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return new ArrayList<String>();
    }
}
