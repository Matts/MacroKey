package mata.macrokey.proxy;

import mata.macrokey.MacroKey;
import mata.macrokey.event.WorldEvents;
import mata.macrokey.handler.GuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Matt on 3/30/2016.
 */
public class CommonProxy {

    public void registerClient(){
    }

    public void globalRegister(){
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
        NetworkRegistry.INSTANCE.registerGuiHandler(MacroKey.instance, new GuiHandler());
    }

}
