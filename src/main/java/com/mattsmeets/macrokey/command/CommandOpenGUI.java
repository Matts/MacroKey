package com.mattsmeets.macrokey.command;

import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.MinecraftForge;

class CommandOpenGUI {
    static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("open")
                .executes(
                        ctx -> {
                            MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(ExecuteOnTickInterface.openMacroKeyGUI));
                            return 0;
                        }
                );
    }

    private CommandOpenGUI() {
        // Hide the public constructor
    }
}
