package com.mattsmeets.macrokey.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.mattsmeets.macrokey.hook.KeyInputEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();

        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
    }
}
