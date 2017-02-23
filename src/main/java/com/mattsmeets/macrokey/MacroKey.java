package com.mattsmeets.macrokey;

import com.mattsmeets.macrokey.object.BoundKey;
import com.mattsmeets.macrokey.object.Layer;
import com.mattsmeets.macrokey.proxy.CommonProxy;
import com.mattsmeets.macrokey.util.JsonConfig;
import com.mattsmeets.macrokey.util.LogHelper;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 3/30/2016.
 */
@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, useMetadata = true, clientSideOnly=true, guiFactory = Reference.GUI_FACTORY, updateJSON = "https://bitbucket.org/MattsMc/versionchecking/raw/master/macrokey.json")
public class MacroKey {
    @Mod.Instance
    public static MacroKey instance;

    @SidedProxy(clientSide=Reference.CLIENT_PROXY, serverSide=Reference.COMMON_PROXY)
    public static CommonProxy proxy;

    public Configuration configuration;

    public static KeyBinding[] forgeKeybindings;

    public JsonConfig jsonConfig;

    public ArrayList<BoundKey> boundKeys;
    public List<Layer> layers;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){

        LogHelper.debug("PreInit");

        LogHelper.info("Starting the initialization of MacroKey...");

        if(event.getSide().isServer()){
            LogHelper.info("After some checks I have determined that you are running me on a server, I want to note that I do NOT work on server's :>");
        }

        event.getModMetadata().version = Reference.MOD_VERSION;

        configuration = new Configuration(event.getSuggestedConfigurationFile());
        configuration.load();

        jsonConfig = new JsonConfig(event.getSuggestedConfigurationFile());
        {
            jsonConfig.loadLayers();
            jsonConfig.loadKeybindings();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LogHelper.debug("Init");

        proxy.registerClient();
        proxy.globalRegister();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LogHelper.debug("PostInit");
    }

}
