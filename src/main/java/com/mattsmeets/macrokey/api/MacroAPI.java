package com.mattsmeets.macrokey.api;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.model.LayerInterface;

import java.io.IOException;
import java.util.Set;

public class MacroAPI {

    public static class Macro {
        public Set List() {
            try {
                return MacroKey.instance.bindingsRepository.findAllMacros(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void Add() {

        }

        public void Delete() {

        }

        public void Trigger() {

        }

        public void Edit() {

        }
    }

    public static class Layer {
        public Set List() {
            try {
                return MacroKey.instance.bindingsRepository.findAllLayers(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public LayerInterface Current() {
            return MacroKey.instance.modState.getActiveLayer();
        }

        public void Add() {

        }

        public void Delete() {

        }

        public void Toggle() {

        }

        public void Select() {

        }
    }

}
