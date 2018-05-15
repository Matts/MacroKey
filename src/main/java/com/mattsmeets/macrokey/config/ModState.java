package com.mattsmeets.macrokey.config;

import com.mattsmeets.macrokey.model.Layer;
import com.mattsmeets.macrokey.model.LayerInterface;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class ModState {

    private LayerInterface activeLayer;

    public ModState(LayerInterface activeLayer) {
        this.activeLayer = activeLayer;
    }

    public LayerInterface getActiveLayer() {
        return this.activeLayer;
    }

    public ModState setActiveLayer(LayerInterface layer) {
        this.activeLayer = layer;

        return this;
    }

    public List<LayerInterface> getLayers(boolean sync) throws IOException {
        return instance.bindingsRepository.findAllLayers(sync)
                .stream()
                .sorted(Comparator.comparing(LayerInterface::getULID))
                .collect(Collectors.toList());
    }
}
