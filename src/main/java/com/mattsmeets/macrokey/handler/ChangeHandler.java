package com.mattsmeets.macrokey.handler;

import java.io.IOException;

import com.mattsmeets.macrokey.event.LayerEvent;
import com.mattsmeets.macrokey.event.MacroEvent;
import com.mattsmeets.macrokey.repository.BindingsRepository;

import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ChangeHandler {
    final BindingsRepository bindingsRepository;

    ChangeHandler(final BindingsRepository bindingsRepository) {
        this.bindingsRepository = bindingsRepository;
    }

    public static class MacroChangeHandler extends ChangeHandler {
        public MacroChangeHandler(BindingsRepository bindingsRepository) {
            super(bindingsRepository);
        }

        @SubscribeEvent
        public void macroChangedEvent(MacroEvent.MacroChangedEvent event) throws IOException {
            bindingsRepository.updateMacro(event.getMacroChanged(), true);
        }

        @SubscribeEvent
        public void macroAddedEvent(MacroEvent.MacroAddedEvent event) throws IOException {
            bindingsRepository.addMacro(event.getMacro(), true);
        }
    }

    public static class LayerChangeHandler extends ChangeHandler {
        public LayerChangeHandler(BindingsRepository bindingsRepository) {
            super(bindingsRepository);
        }

        @SubscribeEvent
        public void layerAddedEvent(LayerEvent.LayerAddedEvent event) throws IOException {
            bindingsRepository.addLayer(event.getLayer(), true);
        }

        @SubscribeEvent
        public void layerChangedEvent(LayerEvent.LayerChangedEvent event) throws IOException {
            bindingsRepository.updateLayer(event.getLayerChanged(), true);
        }
    }
}
