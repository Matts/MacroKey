package mata.macrokey.proxy;

import mata.macrokey.handler.WorldEvents;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Matt on 3/30/2016.
 */
public class CommonProxy {

    public void registerClient(){
    }

    public void globalRegister(){
        MinecraftForge.EVENT_BUS.register(new WorldEvents());
    }

}
