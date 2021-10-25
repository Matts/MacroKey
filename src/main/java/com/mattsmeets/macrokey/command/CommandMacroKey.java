package com.mattsmeets.macrokey.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public class CommandMacroKey {
    public CommandMacroKey(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSourceStack>literal("macrokey")
                        .then(CommandLayer.register())
                        .then(CommandOpenGUI.register())
        );
    }
}
