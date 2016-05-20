package mata.macrokey.language;

import mata.macrokey.handler.GuiEventHandler;
import mata.macrokey.object.ToBeExecutedCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;


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
                                Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Could not parse int " + t1.getToken()));
                                error=true;
                            }
                        }else{
                            Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Expecting integer after sleep " + t1.getToken()));
                            error=true;
                        }
                    }
                    if (t.getToken().equalsIgnoreCase("exec")) {
                        if (nextsleep != -1) {
                            Token t1 = tokenizer.nextToken();
                            if(t1.getType()==TokenType.STRING_LITERAL) {
                                GuiEventHandler.keyList.add(new ToBeExecutedCommand(GuiEventHandler.ticks + totaltime, t1.getToken()));
                                nextsleep = -1;
                            }else{
                                Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Error occurred in parsing of the command at " + t1.getToken()));
                                error=true;
                            }
                        } else {
                            Token t1 = tokenizer.nextToken();
                            if(t1.getType()==TokenType.STRING_LITERAL) {
                                GuiEventHandler.keyList.add(new ToBeExecutedCommand(GuiEventHandler.ticks + totaltime, t1.getToken()));
                            }else {
                                Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Error occurred in parsing of the command at " + t1.getToken()));
                                error=true;
                            }
                        }
                    }
            }

    }
}