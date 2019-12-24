package com.mattsmeets.macrokey.api;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.model.LayerInterface;
import com.mattsmeets.macrokey.model.MacroInterface;
import org.graalvm.polyglot.HostAccess;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * MacroAPI implements the own MacroKey API, this will allow you to modify commands in commands. As this is very powerful,
 * no money-back guarantee will be provided if the MacroAPI takes over the world when used incorrectly.
 *
 * @since 2.1
 */
public class MacroAPI {

    public static class Macro extends AbstractAPI {
        @HostAccess.Export
        public Set<MacroInterface> List() {
            try {
                return MacroKey.instance.bindingsRepository.findAllMacros(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @HostAccess.Export
        public void Add(int keyCode, String command, boolean active) throws IOException {
            MacroKey.instance.bindingsRepository.addMacro(new com.mattsmeets.macrokey.model.Macro(keyCode, command, active), true);
        }

        @HostAccess.Export
        public void Delete(UUID uuid) throws IOException {
            MacroKey.instance.bindingsRepository.deleteMacro(uuid, true, true);
        }

        /**
         * not yet implemented
         */
        public void Edit() {
            notYetImplemented();
        }
    }

    public static class Layer extends AbstractAPI {
        @HostAccess.Export
        public Set<LayerInterface> List() {
            try {
                return MacroKey.instance.bindingsRepository.findAllLayers(true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @HostAccess.Export
        public LayerInterface Current() {
            return MacroKey.instance.modState.getActiveLayer();
        }

        @HostAccess.Export
        public void Add(String displayName) throws IOException {
            MacroKey.instance.bindingsRepository.addLayer(new com.mattsmeets.macrokey.model.Layer(displayName), true);
        }

        /**
         * not yet implemented
         */
        public void Delete() {
            notYetImplemented();
        }

        /**
         * not yet implemented
         */
        public void Toggle() {
            notYetImplemented();
        }

        /**
         * not yet implemented
         */
        public void Select() {
            notYetImplemented();
        }
    }

}
