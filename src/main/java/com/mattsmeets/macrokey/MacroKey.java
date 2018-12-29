package com.mattsmeets.macrokey;

import com.mattsmeets.macrokey.config.ModConfig;
import com.mattsmeets.macrokey.config.ModState;
import com.mattsmeets.macrokey.proxy.CommonProxy;
import com.mattsmeets.macrokey.repository.BindingsRepository;
import com.mattsmeets.macrokey.service.JsonConfig;
import com.mattsmeets.macrokey.service.LogHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;

@Mod(
        modid = ModReference.MOD_ID,
        name = BuildConfig.NAME,
        version = BuildConfig.VERSION,
        clientSideOnly = true,
        useMetadata = true,
        acceptedMinecraftVersions = BuildConfig.acceptedVersions,
        updateJSON = BuildConfig.updateJSON,
        certificateFingerprint = BuildConfig.fingerprint
)
public class MacroKey {

    @Mod.Instance
    public static MacroKey instance;

    @SidedProxy(clientSide = ModReference.CLIENT_PROXY)
    public static CommonProxy proxy;

    public LogHelper logger;

    public JsonConfig bindingsJSONConfig;

    public BindingsRepository bindingsRepository;

    public ModState modState;

    public KeyBinding[] forgeKeybindings;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) throws IOException {
        this.logger = new LogHelper(event.getModLog());
        // MacroKey is a client side only mod, so we never want a server to run it
        if (event.getSide() == Side.SERVER) {
            this.logger.warn("Whoops! It seems you are trying to run MacroKey on a server... No worries, we will just disable.");
        }

        this.logger.info("Hello World! Welcome to MacroKey Keybinding. Please sit back while we initialize...");
        this.logger.debug("PreInitialization");

        ModConfig.configure(event.getSuggestedConfigurationFile());

        // set-up the bindings.json service & files
        this.bindingsJSONConfig = new JsonConfig(event.getModConfigurationDirectory().getAbsolutePath(), ModConfig.bindingFile);
        this.bindingsJSONConfig.initializeFile();

        // BindingsRepository has a dependency on the bindings.json file being created
        this.bindingsRepository = new BindingsRepository(this.bindingsJSONConfig);
        // Initialize the mod's state
        this.modState = new ModState(this.bindingsRepository.findActiveLayer(true));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) throws IOException {
        this.logger.info("Getting ready to take over the world!");
        this.logger.debug("PreInitialization");

        proxy.init();
    }

    @Mod.EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        this.logger = new LogHelper(ModReference.MOD_ID);

        this.logger.warn("Invalid fingerprint detected! The version of the mod is most likely modified or an unofficial release.");
        this.logger.warn("Please download the latest version from http://curse.com/project/243479");
    }

}
