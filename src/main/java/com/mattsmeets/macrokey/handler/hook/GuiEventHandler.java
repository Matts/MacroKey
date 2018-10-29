package com.mattsmeets.macrokey.handler.hook;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.List;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class GuiEventHandler {

    private String
            layerMasterText = I18n.format("text.layer.master");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void event(GuiScreenEvent.InitGuiEvent event) {
        GuiScreen gui = event.getGui();
        // check if the current GUI is the in-game menu
        // and if the layer switch button is not disabled
        if (!(event.getGui() instanceof GuiIngameMenu) || ModConfig.buttonLayerSwitcherId == -1) {
            return;
        }

        LayerInterface layer = instance.modState.getActiveLayer();
        // render the layer switcher button
        event.getButtonList().add(
                new GuiButton(
                        ModConfig.buttonLayerSwitcherId,
                        gui.width / 2 + ModConfig.buttonLayerSwitchSettings[0],
                        gui.height / 4 + ModConfig.buttonLayerSwitchSettings[1],
                        ModConfig.buttonLayerSwitchSettings[2],
                        ModConfig.buttonLayerSwitchSettings[3],
                        I18n.format("text.layer.display",
                                layer == null ? this.layerMasterText : layer.getDisplayName()
                        )
                ));
    }

    @SubscribeEvent
    public void postActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) throws IOException {
        if (!(event.getGui() instanceof GuiIngameMenu)
                || event.getButton().id != ModConfig.buttonLayerSwitcherId) {
            return;
        }

        List<LayerInterface> layers = MacroKey.instance.modState.getLayers(true);
        LayerInterface layer = null;

        // get the index within all the
        // layers for the one currently active
        int indexOfCurrent = layers.indexOf(MacroKey.instance.modState.getActiveLayer());

        // if there are more layers than the next
        // layer being selected, then select the next one
        if (layers.size() > indexOfCurrent + 1) {
            layer = layers.get(indexOfCurrent + 1);
        }

        MacroKey.instance.modState.setActiveLayer(layer);
        event.getButton().displayString =
                I18n.format("text.layer.display",
                        layer == null ? this.layerMasterText : layer.getDisplayName()
                );
    }

}