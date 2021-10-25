package com.mattsmeets.macrokey.config;

//@Config(modid = ModReference.MOD_ID)
//@Config.LangKey("macrokey.config.title")
public class ModConfig {

    //@Config.Comment("How many ticks need to pass for the repeatable command cooldown timer to expire (default: 20 ticks -> 1 second)")
    public static int repeatDelay = 20;

    //@Config.Comment("What file should be used for saving the bindings and various other dynamic information (default: bindings.json)")
    public static String bindingFile = "bindings.json";

    //@Config.Comment("Customize the ID that is used when calling the macro management GUI (default: 423458971)")
    public static int guiMacroManagementId = 423458971;

    //@Config.Comment("Customize the ID that is used when creating the button for switching layers (default: 823358142), (-1 = disabled)")
    public static int buttonLayerSwitcherId = 823358142;

    //@Config.Comment("Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 200, 20)")
    public static int[] buttonLayerSwitchSettings = {-100, 128, 200, 20};

//    @Mod.EventBusSubscriber(modid = ModReference.MOD_ID)
//    public static class EventHandler {
//        @SubscribeEvent
//        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
//            if (event.getModID().equals(ModReference.MOD_ID)) {
//                //ConfigManager.sync(ModReference.MOD_ID, Config.Type.INSTANCE);
//            }
//        }
//    }
}
