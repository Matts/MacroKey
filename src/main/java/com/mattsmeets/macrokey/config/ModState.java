package com.mattsmeets.macrokey.config;

import com.mattsmeets.macrokey.MacroKey;
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

    public ModState setActiveLayer(LayerInterface layer) throws IOException {
        this.activeLayer = layer;

        instance.bindingsRepository.setActiveLayer(layer == null ? null : layer.getULID(), true);

        return this;
    }

    public List<LayerInterface> getLayers(boolean sync) throws IOException {
        return instance.bindingsRepository.findAllLayers(sync)
                .stream()
                .sorted(Comparator.comparing(LayerInterface::getULID))
                .collect(Collectors.toList());
    }

    public LayerInterface nextLayer() throws IOException {
        List<LayerInterface> layers = this.getLayers(true);
        LayerInterface layer = null;

        // get the index within all the
        // layers for the one currently active
        int indexOfCurrent = layers.indexOf(this.getActiveLayer());

        // if there are more layers than the next
        // layer being selected, then select the next one
        if (layers.size() > indexOfCurrent + 1) {
            layer = layers.get(indexOfCurrent + 1);
        }

        this.setActiveLayer(layer);

        return layer;
    }
}
