package com.mattsmeets.macrokey.api;

import net.minecraft.util.text.TextFormatting;

public class TextAPI extends AbstractAPI {

    public String SECTION_SIGN = "\u00a7";

    public static class Color {
        public String BLACK = getFormat("BLACK");
        public String DARK_BLUE = getFormat("DARK_BLUE");
        public String DARK_GREEN = getFormat("DARK_GREEN");
        public String DARK_AQUA = getFormat("DARK_AQUA");
        public String DARK_RED = getFormat("DARK_RED");
        public String DARK_PURPLE = getFormat("DARK_PURPLE");
        public String GOLD = getFormat("GOLD");
        public String GRAY = getFormat("GRAY");
        public String DARK_GRAY = getFormat("DARK_GRAY");
        public String BLUE = getFormat("BLUE");
        public String GREEN = getFormat("GREEN");
        public String AQUA = getFormat("AQUA");
        public String RED = getFormat("RED");
        public String LIGHT_PURPLE = getFormat("LIGHT_PURPLE");
        public String YELLOW = getFormat("YELLOW");
        public String WHITE = getFormat("WHITE");
    }

    public static class Format {
        public String OBFUSCATED = getFormat("OBFUSCATED");
        public String BOLD = getFormat("BOLD");
        public String STRIKETHROUGH = getFormat("STRIKETHROUGH");
        public String UNDERLINE = getFormat("UNDERLINE");
        public String ITALIC = getFormat("ITALIC");
        public String RESET = getFormat("RESET");

    }

    public String getFormatByName(String name) {
        return getFormat(name);
    }
    
    public static String getFormat(String name) {
        TextFormatting format = TextFormatting.getValueByName(name);

        return format == null ? null : format.toString();
    }

}
