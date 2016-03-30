package mata.macrokey.proxy;

import mata.macrokey.command.CommandBind;
import mata.macrokey.handler.ButtonClickEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by Matt on 3/30/2016.
 */
public class ClientProxy extends CommonProxy {

    public void registerClient(){
        ClientCommandHandler.instance.registerCommand(new CommandBind());
        MinecraftForge.EVENT_BUS.register(new ButtonClickEvent());
    }

}
