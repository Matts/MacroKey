package com.mattsmeets.macrokey.event.handler;

import com.mattsmeets.macrokey.event.LayerChangedEvent;
import com.mattsmeets.macrokey.event.MacroChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class ChangeHandler {

    @SideOnly(Side.CLIENT)
    public static class MacroChangeHandler {

        @SubscribeEvent
        public void macroChangedEvent(MacroChangedEvent event) throws IOException {
            instance.bindingsRepository.updateMacro(event.getMacroChanged(), true);
        }
    }

    @SideOnly(Side.CLIENT)
    public static class LayerChangeHandler {

        @SubscribeEvent
        public void layerChangedEvent(LayerChangedEvent event) throws IOException {
            instance.bindingsRepository.updateLayer(event.getLayerChanged(), true);
        }
    }
}
