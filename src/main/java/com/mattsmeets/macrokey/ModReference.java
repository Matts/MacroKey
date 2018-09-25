package com.mattsmeets.macrokey;

import static com.mattsmeets.macrokey.MacroKey.instance;

public class ModReference {
    public static final String MOD_ID = "macrokey";
    public static final String MOD_NAME = instance.referencePropLoader.getProperty("mod_name");
    public static final String MOD_VERSION = instance.referencePropLoader.getProperty("mod_version");
    public static final String UPDATE_URL = instance.referencePropLoader.getProperty("update_url");

    public static final String COMMON_PROXY = "com.mattsmeets.macrokey.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "com.mattsmeets.macrokey.proxy.ClientProxy";
}
