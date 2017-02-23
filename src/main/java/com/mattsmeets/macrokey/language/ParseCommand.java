package com.mattsmeets.macrokey.language;

import com.mattsmeets.macrokey.handler.IngameEventHandler;
import com.mattsmeets.macrokey.object.ToBeExecutedCommand;
import net.minecraft.client.Minecraft;


public class ParseCommand {

    public static void parse(String code){
        code = code.replace(";",";\n");

        Tokenizer tokenizer = new Tokenizer(code);

        int nextsleep=-1;
        int totaltime=0;

        boolean error=false;
        while (tokenizer.hasNextToken() &&!error) {
            Token t = tokenizer.nextToken();
            if (t.getToken().equalsIgnoreCase("sleep")) {
                Token t1 = tokenizer.nextToken();
                if(t1.getType()==TokenType.INTEGER_LITERAL) {
                    try {
                        nextsleep = Integer.parseInt(t1.getToken()) * 20;
                        totaltime += nextsleep;
                    }catch (NumberFormatException e){
                        Minecraft.getMinecraft().player.sendChatMessage("Could not parse int " + t1.getToken());
                        error=true;
                    }
                }else{
                    Minecraft.getMinecraft().player.sendChatMessage("Expecting integer after sleep " + t1.getToken());
                    error=true;
                }
            }
            if (t.getToken().equalsIgnoreCase("exec")) {
                if (nextsleep != -1) {
                    Token t1 = tokenizer.nextToken();
                    if(t1.getType()==TokenType.STRING_LITERAL) {
                        IngameEventHandler.keyList.add(new ToBeExecutedCommand(IngameEventHandler.ticks + totaltime, t1.getToken()));
                        nextsleep = -1;
                    }else{
                        Minecraft.getMinecraft().player.sendChatMessage("Error occurred in parsing of the command at " + t1.getToken());
                        error=true;
                    }
                } else {
                    Token t1 = tokenizer.nextToken();
                    if(t1.getType()==TokenType.STRING_LITERAL) {
                        IngameEventHandler.keyList.add(new ToBeExecutedCommand(IngameEventHandler.ticks + totaltime, t1.getToken()));
                    }else {
                        Minecraft.getMinecraft().player.sendChatMessage("Error occurred in parsing of the command at " + t1.getToken());
                        error=true;
                    }
                }
            }
        }

    }
}