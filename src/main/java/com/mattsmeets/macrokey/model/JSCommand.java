package com.mattsmeets.macrokey.model;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.config.ModConfig;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.script.Invocable;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;

public class JSCommand extends AbstractCommand implements CommandInterface {

    /**
     * File to parse
     */
    private final String command;

    private Invocable inv;

    public JSCommand(String command) {
        super("javascript");

        this.command = command;
    }

    @Override
    public void execute(EntityPlayerSP player) {
        if (inv == null) {
            setup();
        }

        try {
            inv.invokeFunction(ModConfig.javascriptMain);
        } catch (ScriptException | NoSuchMethodException | NullPointerException e) {
            e.printStackTrace();

            ICommandSender sender = player.getCommandSenderEntity();
            if(sender != null) {
                sender.sendMessage(new TextComponentString(TextFormatting.RED + "[MacroKey] Something went wrong while parsing the command"));
                sender.sendMessage(
                        new TextComponentString(
                                e.getMessage() + ""
                        )
                );
            }
        }
    }

    @Override
    public void setup() {
        try {
            inv = MacroKey.instance.javascriptInterpreter.eval(new File(command));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return command;
    }
}
