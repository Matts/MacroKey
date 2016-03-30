package mata.macrokey.handler;

import mata.macrokey.MacroKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by Matt on 3/30/2016.
 */
public class ButtonClickEvent {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        for(int i = 0; i< MacroKey.instance.binding.size(); i++) {
            if(Keyboard.isKeyDown(MacroKey.instance.binding.get(i).getKey()) & !MacroKey.instance.binding.get(i).isPressed()){
                MacroKey.instance. binding.get(i).setPressed(true);
                EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
                entity.sendChatMessage(MacroKey.instance.binding.get(i).getExec());
            }
            if(!Keyboard.isKeyDown(MacroKey.instance.binding.get(i).getKey())){
                MacroKey.instance.binding.get(i).setPressed(false);
            }
        }

    }

}
