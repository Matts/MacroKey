package com.mattsmeets.macrokey.command;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.event.ExecuteOnTickEvent;
import com.mattsmeets.macrokey.model.lambda.ExecuteOnTickInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;
import java.util.List;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class CommandOpenGUI extends StrippedCommand {

    @Override
    @SideOnly(Side.CLIENT)
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        System.out.println("opening GUI");

        Minecraft.getMinecraft().player.closeScreenAndDropStack();

        ExecuteOnTickInterface execute = (boolean delayed) -> {
            Minecraft.getMinecraft().player.openGui(
                    instance,
                    ModConfig.guiMacroManagementId,
                    Minecraft.getMinecraft().world,
                    (int) Minecraft.getMinecraft().player.posX,
                    (int) Minecraft.getMinecraft().player.posY,
                    (int) Minecraft.getMinecraft().player.posZ
            );
        };

        MinecraftForge.EVENT_BUS.post(new ExecuteOnTickEvent(execute));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Arrays.asList(new String[]{"open"});
    }
}
