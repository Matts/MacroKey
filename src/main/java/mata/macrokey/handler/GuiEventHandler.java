package mata.macrokey.handler;

import mata.macrokey.MacroKey;
import mata.macrokey.Reference;
import mata.macrokey.gui.GuiKeybindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiEvent(GuiScreenEvent.InitGuiEvent guiEvent) {
        GuiScreen gui = guiEvent.getGui();
        if (gui instanceof GuiControls) {
            guiEvent.getButtonList().add(new GuiButton(Reference.MACROKEY_OPTIONS_BUTTON, gui.width / 2 - 155 + 3 % 2 * 160, 18 + 24 * (3 >> 1), 150, 20, I18n.format("key.macrokeybinding", new Object[0])));
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void actionPreformed(GuiScreenEvent.ActionPerformedEvent event) {
        GuiScreen gui = event.getGui();

        if (gui instanceof GuiControls) {
            if(event.getButton().id == Reference.MACROKEY_OPTIONS_BUTTON){
                Minecraft.getMinecraft().displayGuiScreen(new GuiKeybindings(gui, Minecraft.getMinecraft().gameSettings));
            }
        }
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
    public void onEvent(InputEvent.KeyInputEvent event)
    {
        KeyBinding[] keyBindings = MacroKey.forgeKeybindings;

        if (keyBindings[0].isPressed())
        {
            Minecraft.getMinecraft().thePlayer.openGui(MacroKey.instance, 5002, Minecraft.getMinecraft().theWorld, (int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posY, (int) Minecraft.getMinecraft().thePlayer.posZ);
        }

        for(int i = 0; i< MacroKey.instance.boundKeys.size(); i++) {
            if(Keyboard.isKeyDown(MacroKey.instance.boundKeys.get(i).getKeyCode()) & !MacroKey.instance.boundKeys.get(i).isPressed()){
                MacroKey.instance.boundKeys.get(i).setPressed(true);
                EntityPlayerSP entity = Minecraft.getMinecraft().thePlayer;
                entity.sendChatMessage(MacroKey.instance.boundKeys.get(i).getCommand());
            }
            if(!Keyboard.isKeyDown(MacroKey.instance.boundKeys.get(i).getKeyCode())){
                MacroKey.instance.boundKeys.get(i).setPressed(false);
            }
        }
    }
}
