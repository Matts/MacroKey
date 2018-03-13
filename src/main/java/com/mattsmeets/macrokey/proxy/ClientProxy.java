package com.mattsmeets.macrokey.proxy;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.handler.GuiHandler;
import net.minecraftforge.common.MinecraftForge;

import com.mattsmeets.macrokey.event.handler.MacroKeyHandler;
import com.mattsmeets.macrokey.hook.ClientTickEvent;
import com.mattsmeets.macrokey.hook.KeyInputEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        // hook into forge events
        this.registerHooks();

        // register MacroKey event handlers
        MinecraftForge.EVENT_BUS.register(new MacroKeyHandler());
    }

    private void registerHooks() {
        NetworkRegistry.INSTANCE.registerGuiHandler(MacroKey.instance, new GuiHandler());
        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new ClientTickEvent());
    }
}
