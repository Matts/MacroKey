package com.mattsmeets.macrokey.hook;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class GuiEvent {

    private String
            layerMasterText = I18n.format("text.layer.master");

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void event(GuiScreenEvent.InitGuiEvent event) {
        GuiScreen gui = event.getGui();

        if (event.getGui() instanceof GuiIngameMenu) {
            LayerInterface layer = instance.modState.getActiveLayer();

            if(ModConfig.buttonLayerSwitcherId != -1) {
                // render the layer switcher button
                event.getButtonList().add(
                        new GuiButton(
                                ModConfig.buttonLayerSwitcherId,
                                gui.width / 2 + ModConfig.buttonLayerSwitchLocation[0],
                                gui.height / 4 + ModConfig.buttonLayerSwitchLocation[1],
                                150 / 3 * 2 - 1,
                                20,
                                I18n.format("text.layer.display",
                                        layer == null ? this.layerMasterText : layer.getDisplayName()
                                )
                        ));
            }
        }
    }

    @SubscribeEvent
    public void postActionPerformed(GuiScreenEvent.ActionPerformedEvent.Post event) throws IOException {
        if (event.getGui() instanceof GuiIngameMenu
                && event.getButton().id == ModConfig.buttonLayerSwitcherId) {
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

}
