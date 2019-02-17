package com.mattsmeets.macrokey.api;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.event.MacroActivationEvent;
import com.mattsmeets.macrokey.model.Layer;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.Macro;
import com.mattsmeets.macrokey.model.MacroInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MacroAPI {

    public static class Macro extends AbstractAPI {
        public Set List() {
            try {
                return MacroKey.instance.bindingsRepository.findAllMacros(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public void Add(int keyCode, String command, boolean active) throws IOException {
            MacroKey.instance.bindingsRepository.addMacro(new com.mattsmeets.macrokey.model.Macro(keyCode, command, active), true);
        }

        public void Delete(UUID uuid) throws IOException {
            MacroKey.instance.bindingsRepository.deleteMacro(uuid, true, true);
        }

        public void Edit() {
            notYetImplemented();
        }
    }

    public static class Layer extends AbstractAPI {
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

        public void Add(String displayName) throws IOException {
            MacroKey.instance.bindingsRepository.addLayer(new com.mattsmeets.macrokey.model.Layer(displayName), true);
        }

        public void Delete() {
            notYetImplemented();
        }

        public void Toggle() {
            notYetImplemented();
        }

        public void Select() {
            notYetImplemented();
        }
    }

}
