package com.mattsmeets.macrokey.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.command.CommandSource;

public class CommandMacroKey {
    public CommandMacroKey(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                LiteralArgumentBuilder.<CommandSource>literal("macrokey")
                        .then(CommandLayer.register())
                        .then(CommandOpenGUI.register())
        );
    }
}
