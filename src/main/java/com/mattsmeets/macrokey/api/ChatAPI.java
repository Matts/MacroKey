package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class ChatAPI extends AbstractAPI {

    public void Send(String message) {
        Minecraft.getMinecraft().player.sendChatMessage(legalCharacters(message));
    }

    public void Add(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

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
