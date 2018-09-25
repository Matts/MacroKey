package com.mattsmeets.macrokey.handler;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.event.MacroEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class ChangeHandler {

    @SideOnly(Side.CLIENT)
    public static class MacroChangeHandler {

        @SubscribeEvent
        public void macroChangedEvent(MacroEvent.MacroChangedEvent event) throws IOException {
            instance.bindingsRepository.updateMacro(event.getMacroChanged(), true);
        }

        @SubscribeEvent
        public void macroAddedEvent(MacroEvent.MacroAddedEvent event) throws IOException {
            instance.bindingsRepository.addMacro(event.getMacro(), true);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class LayerChangeHandler {

        @SubscribeEvent
        public void layerAddedEvent(LayerEvent.LayerAddedEvent event) throws IOException {
            instance.bindingsRepository.addLayer(event.getLayer(), true);
        }

        @SubscribeEvent
        public void layerChangedEvent(LayerEvent.LayerChangedEvent event) throws IOException {
            instance.bindingsRepository.updateLayer(event.getLayerChanged(), true);
        }
    }

}
