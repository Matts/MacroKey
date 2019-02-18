package com.mattsmeets.macrokey.model;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.service.JavascriptInterpreter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;

import javax.script.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;

public class JSCommand extends AbstractCommand implements CommandInterface {

    /**
     * File to parse
     */
    private final String command;

    private JavascriptInterpreter inv;

    public JSCommand(String command) {
        super("javascript");

        this.command = command;
    }

    @Override
    public void execute(EntityPlayerSP player) {
        if (inv == null) {
            setup();
        }
//
//
        try {
            inv.eval("main()");
        } catch (ScriptException | PolyglotException | FileNotFoundException e) {
            e.printStackTrace();

            sendErrorToPlayer(player, e);
        } catch (NullPointerException ignored){}
    }

    @Override
    public void setup() {
//        try {
            inv = new JavascriptInterpreter(new File(command));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            MacroKey.instance.logger.err("Could not find/read file on system " + e.getMessage());
//        } catch (ScriptException e) {
//            e.printStackTrace();
//            MacroKey.instance.logger.err("An error occurred while interpreting the script " + e.getMessage());
//
//            EntityPlayer player = Minecraft.getMinecraft().player;
//            if (player != null) {
//                sendErrorToPlayer(player, e);
//            }
//        }
    }

    private void sendErrorToPlayer(EntityPlayer player, Exception e) {
        player.sendMessage(new TextComponentString(TextFormatting.RED + "[MacroKey] Something went wrong while executing the script"));
        player.sendMessage(
                new TextComponentString(
                        e.getMessage().replaceAll("\\r", "") + ""
                )
        );
    }

    @Override
    public String toString() {
        return command;
    }
}
