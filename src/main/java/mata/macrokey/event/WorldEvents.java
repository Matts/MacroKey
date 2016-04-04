package mata.macrokey.event;

import mata.macrokey.MacroKey;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Matt on 3/30/2016.
 */
public class WorldEvents {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onJoinEvent(EntityJoinWorldEvent event){
        if(event.getEntity() == Minecraft.getMinecraft().thePlayer) {

            if(!event.getEntity().isDead) {
                String message = I18n.translateToLocal("chat.join");

                message = message.replace("%tag%", I18n.translateToLocal("chat.tag"));
                message = message.replace("%loaded%",MacroKey.instance.boundKeys.size()+"");

                if(MacroKey.instance.configuration.getBoolean("isSpawnMessageEnabled",MacroKey.instance.configuration.CATEGORY_GENERAL, true, "set this to false if you are getting annoyed by the spam")) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(message));
                }
                MacroKey.instance.configuration.save();
            }
        }
    }
}
