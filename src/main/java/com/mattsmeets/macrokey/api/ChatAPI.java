package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import org.graalvm.polyglot.HostAccess;

/**
 * ChatAPI allows you to send characters over text. The TextAPI can be looked at for formatting text
 *
 * @see TextAPI
 * @since 2.1
 */
public class ChatAPI extends AbstractAPI {

    /**
     * Send sends a message to the server. This can be a chat message, or a command
     *
     * @param message message to communicate to the server
     */
    @HostAccess.Export
    public void Send(String message) {
        Minecraft.getMinecraft().player.sendChatMessage(legalCharacters(message));
    }

    /**
     * Add will send a message to your client.
     *
     * @param message message to communicate to your client
     */
    @HostAccess.Export
    public void Add(String message) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(message));
    }

    /**
     * If you are unsure if your text is "Chat-safe", this can be used to validate.
     * This is implemented by default for the (server) Send functionality
     *
     * @param str dirty string
     * @return cleaned string
     */
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
