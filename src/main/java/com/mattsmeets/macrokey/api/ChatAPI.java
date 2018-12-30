package com.mattsmeets.macrokey.api;

import net.minecraft.client.Minecraft;

public class ChatAPI extends AbstractAPI {

    public void Send(String message) {
        Minecraft.getMinecraft().player.sendChatMessage(message);
    }

}
