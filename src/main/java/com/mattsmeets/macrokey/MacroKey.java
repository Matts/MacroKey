package com.mattsmeets.macrokey;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.mattsmeets.macrokey.command.CommandMacroKey;
import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.handler.ChangeHandler;
import com.mattsmeets.macrokey.handler.GameTickHandler;
import com.mattsmeets.macrokey.handler.hook.ClientTickHandler;
import com.mattsmeets.macrokey.handler.hook.GuiEventHandler;
import com.mattsmeets.macrokey.handler.hook.KeyInputHandler;
import com.mattsmeets.macrokey.repository.BindingsRepository;
import com.mattsmeets.macrokey.service.JsonConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

@Mod(ModReference.MOD_ID)
public class MacroKey {
    private static final Logger LOGGER = LogManager.getLogger(ModReference.MOD_ID);

    public static BindingsRepository bindingsRepository;
    public static ModState modState;

    public MacroKey() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.SPEC, "macrokey.toml");
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void onServerStarting(final FMLServerStartingEvent event) {
        new CommandMacroKey(event.getServer().getCommands().getDispatcher());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void clientSetup(final FMLClientSetupEvent event) throws IOException {
            LOGGER.info("Hello World! Welcome to MacroKey Keybinding. Please sit back while we initialize...");
            LOGGER.debug("PreInitialization");

            // set-up the bindings.json service & files
            final JsonConfig bindingsJSONConfig = new JsonConfig(Minecraft.getInstance().gameDirectory.getAbsolutePath(), ModConfig.bindingFile.get());
            bindingsJSONConfig.initializeFile();

            // BindingsRepository has a dependency on the bindings.json file being created
            bindingsRepository = new BindingsRepository(bindingsJSONConfig);

            // Initialize the mod's state
            modState = new ModState(bindingsRepository, bindingsRepository.findActiveLayer(true));

            LOGGER.info("Init macro keys");

            MinecraftForge.EVENT_BUS.register(new GameTickHandler(null, null, registerKeyBindings()));
            MinecraftForge.EVENT_BUS.register(new ChangeHandler.LayerChangeHandler(bindingsRepository));
            MinecraftForge.EVENT_BUS.register(new ChangeHandler.MacroChangeHandler(bindingsRepository));
            MinecraftForge.EVENT_BUS.register(new KeyInputHandler(bindingsRepository, modState));
            MinecraftForge.EVENT_BUS.register(new ClientTickHandler());
            MinecraftForge.EVENT_BUS.register(new GuiEventHandler(modState));
        }

        private static Map<ModKeyBinding, KeyBinding> registerKeyBindings() {
            final KeyBinding managementKey = new KeyBinding(I18n.get("key.macrokey.management.desc"), KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_K), "key.macrokey.category");
            final Map<ModKeyBinding, KeyBinding> keyBindingMap = Collections.singletonMap(ModKeyBinding.OPEN_MANAGEMENT_GUI, managementKey);

            keyBindingMap
                    .values()
                    .forEach(ClientRegistry::registerKeyBinding);

            return keyBindingMap;
        }

        private RegistryEvents() {
            // Hide the public constructor
        }
    }
}
