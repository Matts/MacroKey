package com.mattsmeets.macrokey.event;

import com.mattsmeets.macrokey.model.LayerInterface;
import net.minecraftforge.fml.common.eventhandler.Event;

public class LayerChangedEvent extends Event {

    private LayerInterface layerChanged;

    public LayerChangedEvent(LayerInterface layer) {
        this.layerChanged = layer;
    }

    public LayerInterface getLayerChanged() {
        return layerChanged;
    }
}
