package com.mattsmeets.macrokey.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ModConfig {

    public static int repeatDelay = 20;

    public static String bindingFile = "bindings.json";

    public static int guiMacroManagementId = 423458971;

    public static int buttonLayerSwitcherId = 823358142;

    public static int[] buttonLayerSwitchSettings = {-100, 128, 200, 20};

    private static Configuration configuration;

    public static void configure(File file) {
        if(configuration == null)
            configuration = new Configuration(file);

        configuration.load();

        repeatDelay = configuration.get(Configuration.CATEGORY_GENERAL, "repeatDelay", repeatDelay, "How many ticks need to pass for the repeatable command cooldown timer to expire (default: 20 ticks -> 1 second)").getInt();
        bindingFile = configuration.get(Configuration.CATEGORY_GENERAL, "bindingFile", bindingFile, "What file should be used for saving the bindings and various other dynamic information (default: bindings.json)").getString();
        guiMacroManagementId = configuration.get(Configuration.CATEGORY_GENERAL, "guiMacroManagementId", guiMacroManagementId, "Customize the ID that is used when calling the macro management GUI (default: 423458971)").getInt();
        buttonLayerSwitcherId = configuration.get(Configuration.CATEGORY_GENERAL, "buttonLayerSwitcherId", buttonLayerSwitcherId, "Customize the ID that is used when creating the button for switching layers (default: 823358142), (-1 = disabled)").getInt();
        buttonLayerSwitchSettings = configuration.get(Configuration.CATEGORY_GENERAL, "buttonLayerSwitchSettings", buttonLayerSwitchSettings, "Customize the position that the button for switching layer should use, expected is {x, y, width, height} (default: -100, 128, 200, 20)").getIntList();

        if (configuration.hasChanged())
            configuration.save();
    }
}
