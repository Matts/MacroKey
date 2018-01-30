package com.mattsmeets.macrokey;

import java.io.IOException;
import java.util.UUID;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.mattsmeets.macrokey.exception.PropertyInitalizationException;
import com.mattsmeets.macrokey.proxy.CommonProxy;
import com.mattsmeets.macrokey.repository.BindingsRepository;
import com.mattsmeets.macrokey.service.JsonConfig;
import com.mattsmeets.macrokey.service.LogHelper;
import com.mattsmeets.macrokey.service.PropertyLoader;

@Mod(modid = ModReference.MOD_ID, clientSideOnly = true, useMetadata = true)
public class MacroKey {

    @Mod.Instance
    public static MacroKey instance;

    @SidedProxy(clientSide = ModReference.CLIENT_PROXY)
    public static CommonProxy proxy;

    public PropertyLoader referencePropLoader;
    public LogHelper logger;

    public JsonConfig bindingsJSONConfig;

    public BindingsRepository bindingsRepository;

    /**
     * Any pre-preInitialization stuff that has to occur...
     *
     * @throws PropertyInitalizationException whenever the reference.properties file is not
     * found, an exception will be thrown
     */
    public MacroKey() throws PropertyInitalizationException {
        this.referencePropLoader = new PropertyLoader("reference.properties");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        this.logger = new LogHelper(event.getModLog());

        // setting the version from reference
        ModMetadata modMetadata = event.getModMetadata();
        modMetadata.version = ModReference.MOD_VERSION;
        modMetadata.name = ModReference.MOD_NAME;

        // MacroKey is a client side only mod, so we never want a server to run it
        if (event.getSide() == Side.SERVER) {
            this.logger.warn("Whoops! It seems you are trying to run MacroKey on a server... No worries, we will just disable.");
        }

        this.logger.info("Hello World! Welcome to MacroKey Keybinding. Please sit back while we initialize...");
        this.logger.debug("PreInitialization");

        // set-up the bindings.json service & files
        this.bindingsJSONConfig = new JsonConfig(event.getModConfigurationDirectory().getAbsolutePath(), "bindings.json");
        this.bindingsJSONConfig.initializeFile();

        // BindingsRepository has a dependency on the bindings.json file being created
        this.bindingsRepository = new BindingsRepository(this.bindingsJSONConfig);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        this.logger.info("Getting ready to take over the world!");
        this.logger.debug("PreInitialization");

        proxy.init();
    }

}
