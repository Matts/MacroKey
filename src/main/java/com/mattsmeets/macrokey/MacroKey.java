package com.mattsmeets.macrokey;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import com.mattsmeets.macrokey.exception.PropertyInitalizationException;
import com.mattsmeets.macrokey.service.LogHelper;
import com.mattsmeets.macrokey.service.PropertyLoader;

@Mod(modid = ModReference.MOD_ID, clientSideOnly = true, useMetadata = true)
public class MacroKey {

    @Mod.Instance
    public static MacroKey instance;

    public PropertyLoader referencePropLoader;
    public LogHelper logger;

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
    public void preInit(FMLPreInitializationEvent event) {
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
    }

}
