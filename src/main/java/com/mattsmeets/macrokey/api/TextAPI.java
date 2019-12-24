package com.mattsmeets.macrokey.api;

import net.minecraft.util.text.TextFormatting;
import org.graalvm.polyglot.HostAccess;

public class TextAPI extends AbstractAPI {

    @HostAccess.Export
    public String SECTION_SIGN = "\u00a7";

    @HostAccess.Export
    public String getFormatByName(String name) {
        return getFormat(name);
    }

    @HostAccess.Export
    public Color Color = new Color();

    @HostAccess.Export
    public Format Format = new Format();

    public static class Color {
        @HostAccess.Export
        public String BLACK = getFormat("BLACK");
        @HostAccess.Export
        public String DARK_BLUE = getFormat("DARK_BLUE");
        @HostAccess.Export
        public String DARK_GREEN = getFormat("DARK_GREEN");
        @HostAccess.Export
        public String DARK_AQUA = getFormat("DARK_AQUA");
        @HostAccess.Export
        public String DARK_RED = getFormat("DARK_RED");
        @HostAccess.Export
        public String DARK_PURPLE = getFormat("DARK_PURPLE");
        @HostAccess.Export
        public String GOLD = getFormat("GOLD");
        @HostAccess.Export
        public String GRAY = getFormat("GRAY");
        @HostAccess.Export
        public String DARK_GRAY = getFormat("DARK_GRAY");
        @HostAccess.Export
        public String BLUE = getFormat("BLUE");
        @HostAccess.Export
        public String GREEN = getFormat("GREEN");
        @HostAccess.Export
        public String AQUA = getFormat("AQUA");
        @HostAccess.Export
        public String RED = getFormat("RED");
        @HostAccess.Export
        public String LIGHT_PURPLE = getFormat("LIGHT_PURPLE");
        @HostAccess.Export
        public String YELLOW = getFormat("YELLOW");
        @HostAccess.Export
        public String WHITE = getFormat("WHITE");
    }

    public static class Format {
        @HostAccess.Export
        public String OBFUSCATED = getFormat("OBFUSCATED");
        @HostAccess.Export
        public String BOLD = getFormat("BOLD");
        @HostAccess.Export
        public String STRIKETHROUGH = getFormat("STRIKETHROUGH");
        @HostAccess.Export
        public String UNDERLINE = getFormat("UNDERLINE");
        @HostAccess.Export
        public String ITALIC = getFormat("ITALIC");
        @HostAccess.Export
        public String RESET = getFormat("RESET");

    }

    public static String getFormat(String name) {
        TextFormatting format = TextFormatting.getValueByName(name);

        return format == null ? null : format.toString();
    }

}
