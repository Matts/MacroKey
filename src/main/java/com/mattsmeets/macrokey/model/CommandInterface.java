package com.mattsmeets.macrokey.model;

import net.minecraft.client.entity.EntityPlayerSP;

public interface CommandInterface {

    String toString();

    void execute(EntityPlayerSP player);

}
