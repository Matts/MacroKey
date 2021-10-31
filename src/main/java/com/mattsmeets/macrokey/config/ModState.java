package com.mattsmeets.macrokey.config;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.repository.BindingsRepository;

public class ModState {
    private static final Logger LOGGER = LogManager.getLogger();

    private final BindingsRepository bindingsRepository;

    private LayerInterface activeLayer;

    public ModState(BindingsRepository bindingsRepository, LayerInterface activeLayer) {
        this.bindingsRepository = bindingsRepository;
        this.activeLayer = activeLayer;
    }

    public LayerInterface getActiveLayer() {
        return this.activeLayer;
    }

    public void setActiveLayer(LayerInterface layer) throws IOException {
        this.activeLayer = layer;

        bindingsRepository.setActiveLayer(layer == null ? null : layer.getULID(), true);
    }

    public List<LayerInterface> getLayers(boolean sync) throws IOException {
        return bindingsRepository.findAllLayers(sync)
                .stream()
                .sorted(Comparator.comparing(LayerInterface::getULID))
                .collect(Collectors.toList());
    }

    public LayerInterface nextLayer() {
        LayerInterface layer = null;
        try {
            final List<LayerInterface> layers = this.getLayers(true);

            // get the index within all the
            // layers for the one currently active
            int indexOfCurrent = layers.indexOf(this.getActiveLayer());

            // if there are more layers than the next
            // layer being selected, then select the next one
            layer = (layers.size() > indexOfCurrent + 1) ? layers.get(indexOfCurrent + 1) : null;

            this.setActiveLayer(layer);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        return layer;
    }
}
