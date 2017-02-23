package com.mattsmeets.macrokey.proxy;


import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.handler.GuiEventHandler;
import com.mattsmeets.macrokey.handler.IngameEventHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Created by Matt on 3/30/2016.
 */
public class ClientProxy extends CommonProxy {

    public void registerClient(){
        MinecraftForge.EVENT_BUS.register(new GuiEventHandler());
        MinecraftForge.EVENT_BUS.register(new IngameEventHandler());


        MacroKey.forgeKeybindings = new KeyBinding[1];
        MacroKey.forgeKeybindings[0] = new KeyBinding("key.openmacrokey.desc", Keyboard.KEY_K, "key.macrokey.category");

        for (int i = 0; i < MacroKey.forgeKeybindings.length; ++i)
        {
            ClientRegistry.registerKeyBinding(MacroKey.forgeKeybindings[i]);
        }

    }

}
