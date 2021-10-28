package com.mattsmeets.macrokey.config;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@Config(modid = ModReference.MOD_ID)
//@Config.LangKey("macrokey.config.title")
public class ModConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("Config for MacroKey Keybinding");

        repeatDelay = BUILDER.comment("How many ticks need to pass for the repeatable command cooldown timer to expire (default: 20 ticks -> 1 second)").define("Repeat delay", 20);
        bindingFile = BUILDER.comment("What file should be used for saving the bindings and various other dynamic information (default: bindings.json)").define("Binding file", "bindings.json");
        buttonLayerSwitcherId = BUILDER.comment("Customize the ID that is used when creating the button for switching layers (default: 823358142), (-1 = disabled)").define("Switcher button id", 823358142);
        buttonLayerSwitchSetting1 = BUILDER.comment("Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 204, 20)").define("Switcher button setting x", -102);
        buttonLayerSwitchSetting2 = BUILDER.comment("Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 204, 20)").define("Switcher button setting y", 128);
        buttonLayerSwitchSetting3 = BUILDER.comment("Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 204, 20)").define("Switcher button setting width", 204);
        buttonLayerSwitchSetting4 = BUILDER.comment("Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 204, 20)").define("Switcher button setting height", 20);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static ForgeConfigSpec.ConfigValue<Integer> repeatDelay;

    public static ForgeConfigSpec.ConfigValue<String> bindingFile;

    public static int guiMacroManagementId = 423458971;

    public static ForgeConfigSpec.ConfigValue<Integer> buttonLayerSwitcherId;

    public static ForgeConfigSpec.ConfigValue<Integer> buttonLayerSwitchSetting1;
    public static ForgeConfigSpec.ConfigValue<Integer> buttonLayerSwitchSetting2;
    public static ForgeConfigSpec.ConfigValue<Integer> buttonLayerSwitchSetting3;
    public static ForgeConfigSpec.ConfigValue<Integer> buttonLayerSwitchSetting4;

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
