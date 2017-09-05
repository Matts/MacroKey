package com.mattsmeets.macrokey;

import com.google.common.base.Throwables;

import java.io.InputStream;
import java.util.Properties;

public class Reference {

    private static String getProp(String id) {
        if (prop == null) {
            try {
                prop = new Properties();
                InputStream stream = Reference.class
                        .getResourceAsStream("reference.properties");
                prop.load(stream);
                stream.close();
            } catch (Exception e) {
                prop = null;
                Throwables.propagate(e);
            }
        }
        return prop.getProperty(id);
    }

    private static Properties prop;

    public static final String MOD_ID = getProp("MOD_ID");
    public static final String MOD_NAME = getProp("MOD_NAME");
    public static final String MOD_VERSION = getProp("VERSION");

    public static final String COMMON_PROXY ="com.mattsmeets.macrokey.proxy.CommonProxy";
    public static final String CLIENT_PROXY ="com.mattsmeets.macrokey.proxy.ClientProxy";

    public static final String GUI_FACTORY = "com.mattsmeets.macrokey.gui.config.GuiFactoryConfig";

    public static final Integer MACROKEY_OPTIONS_BUTTON = 5003;
    public static final Integer MACROKEY_INGAME_LAYER_TOGGLE = 5004;


}
