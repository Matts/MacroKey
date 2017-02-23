package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.Reference;
import com.mattsmeets.macrokey.gui.GuiManageKeybindings;
import com.mattsmeets.macrokey.object.Layer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Matt on 3/31/2016.
 */
public class GuiEventHandler {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiEvent(GuiScreenEvent.InitGuiEvent guiEvent) {
        GuiScreen gui = guiEvent.getGui();
        if (gui instanceof GuiOptions) {
            guiEvent.getButtonList().add(new GuiButton(Reference.MACROKEY_OPTIONS_BUTTON, gui.width / 2 - 155 + 160, gui.height / 6 + 24 - 6, 150, 20, I18n.format("key.macrokeybinding")));
        }
        if(gui instanceof GuiIngameMenu){
            GuiButton button = new GuiButton(Reference.MACROKEY_INGAME_LAYER_TOGGLE, gui.width / 2 +2, gui.height / 4 + 72 + -16, 150, 20,  Layer.getActiveLayer()==null? "Layer: Master" : "Layer: "+Layer.getActiveLayer().getDisplayName());

            for (GuiButton button1 : guiEvent.getButtonList()) {
                if (button1.id == 7) {
                    button1.width = button1.width/2 -2;
                    button.width = button1.width;
                }
            }
            guiEvent.getButtonList().add(button);
        }
    }

    private int currentSelectedLayer=-1;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void actionPreformed(GuiScreenEvent.ActionPerformedEvent.Post event) {
        GuiScreen gui = event.getGui();

        if (gui instanceof GuiOptions) {
            if(event.getButton().id == Reference.MACROKEY_OPTIONS_BUTTON){
                Minecraft.getMinecraft().displayGuiScreen(new GuiManageKeybindings(gui, Minecraft.getMinecraft().gameSettings));

            }
        }
        if(gui instanceof GuiIngameMenu){
            if(event.getButton().id == Reference.MACROKEY_INGAME_LAYER_TOGGLE) {

                currentSelectedLayer++;

                if(currentSelectedLayer< MacroKey.instance.layers.size()) {
                    event.getButton().displayString = "Layer: " + MacroKey.instance.layers.get(currentSelectedLayer).getDisplayName();
                    Layer.setActiveLayer(MacroKey.instance.layers.get(currentSelectedLayer));

                }else if(currentSelectedLayer+1>=MacroKey.instance.layers.size()){
                    currentSelectedLayer=-1;
                    event.getButton().displayString = "Layer: Master";
                    Layer.setActiveLayer(null);

                }
            }
        }
    }
}
