package com.mattsmeets.macrokey.config;

import com.mattsmeets.macrokey.ModReference;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModReference.MOD_ID)
@Config.LangKey("macrokey.config.title")
public class ModConfig {

    @Config.Comment("How many ticks need to pass for the repeatable command cooldown timer to expire (default: 20 ticks -> 1 second)")
    public static int repeatDelay = 20;

    @Config.Comment("What file should be used for saving the bindings and various other dynamic information (default: bindings.json)")
    public static String bindingFile = "bindings.json";

    @Config.Comment("Customize the ID that is used when calling the macro management GUI (default: 423458971)")
    public static int guiMacroManagementId = 423458971;

    @Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ModReference.MOD_ID)) {
                ConfigManager.sync(ModReference.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }

}
