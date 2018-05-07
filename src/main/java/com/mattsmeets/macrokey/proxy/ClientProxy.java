package com.mattsmeets.macrokey.proxy;

import static com.mattsmeets.macrokey.MacroKey.instance;

import com.mattsmeets.macrokey.event.handler.ChangeHandler;
import com.mattsmeets.macrokey.event.handler.GuiHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.mattsmeets.macrokey.event.handler.MacroKeyHandler;
import com.mattsmeets.macrokey.hook.ClientTickEvent;
import com.mattsmeets.macrokey.hook.KeyInputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.input.Keyboard;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        // hook into forge events
        this.registerHooks();

        // register MacroKey event handlers
        MinecraftForge.EVENT_BUS.register(new MacroKeyHandler());
        MinecraftForge.EVENT_BUS.register(new ChangeHandler.LayerChangeHandler());
        MinecraftForge.EVENT_BUS.register(new ChangeHandler.MacroChangeHandler());

        // register GUI registry
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
    }

    private void registerHooks() {
        instance.forgeKeybindings = new KeyBinding[1];
        instance.forgeKeybindings[0] = new KeyBinding("key.openmacrokey.desc", Keyboard.KEY_K, "key.macrokey.category");

        for (int i = 0; i < instance.forgeKeybindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(instance.forgeKeybindings[i]);
        }

        MinecraftForge.EVENT_BUS.register(new KeyInputEvent());
        MinecraftForge.EVENT_BUS.register(new ClientTickEvent());
    }
}
