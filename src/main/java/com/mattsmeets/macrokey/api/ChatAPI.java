package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import org.graalvm.polyglot.HostAccess;

public class ChatAPI extends AbstractAPI {

    @HostAccess.Export
    public void Send(String message) {
        Minecraft.getMinecraft().player.sendChatMessage(legalCharacters(message));
    }

    @HostAccess.Export
    public void Add(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    @HostAccess.Export
    public String legalCharacters(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); ++i)
        {
            if (ChatAllowedCharacters.isAllowedCharacter(str.charAt(i)))
            {
                builder.append(str.charAt(i));
            }
        }

        return builder.toString();
    }

}
