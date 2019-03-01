package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraftforge.eventbus.api.Event;

public class LayerEvent {

    public static class LayerChangedEvent extends Event {
        private final LayerInterface layer;

        public LayerChangedEvent(LayerInterface layer) {
            this.layer = layer;
        }

        public LayerInterface getLayerChanged() {
            return layer;
        }
    }

    public static class LayerAddedEvent extends Event {
        private final LayerInterface layer;

        public LayerAddedEvent(LayerInterface layer) {
            this.layer = layer;
        }

        public LayerInterface getLayer() {
            return layer;
        }
    }

}
