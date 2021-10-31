package com.mattsmeets.macrokey.command;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

class CommandLayer {
    private static final String LAYER_MASTER_TEXT = I18n.get("text.layer.master");

    static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("layer")
                .then(Commands.literal("toggle")
                        .executes(ctx ->
                        {
                            MacroKey.modState.nextLayer();
                            return printLayerInformation(ctx);
                        })).executes(
                        CommandLayer::printLayerInformation
                );
    }

    private static int printLayerInformation(CommandContext<CommandSourceStack> context) {
        final LayerInterface activeLayer = MacroKey.modState.getActiveLayer();
        final String layerDisplayName;
        final int countMacroEnabled;

        if (activeLayer == null) {
            layerDisplayName = LAYER_MASTER_TEXT;
            countMacroEnabled = MacroKey.bindingsRepository.findAllMacros(false).size();
        } else {
            layerDisplayName = activeLayer.getDisplayName();
            countMacroEnabled = activeLayer.getMacros().size();
        }

        context.getSource().sendSuccess(
                new TextComponent(I18n.get(
                        "command.layer.information",
                        layerDisplayName,
                        countMacroEnabled)
                ), true
        );

        return 0;
    }

    private CommandLayer() {
        // Hide the public constructor
    }
}
