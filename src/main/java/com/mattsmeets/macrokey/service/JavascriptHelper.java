package com.mattsmeets.macrokey.service;

import jdk.nashorn.api.scripting.ClassFilter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class JavascriptHelper {

    public final File modConfigDir;

    private final String[] allowedClasses = {"javafx.application.Platform","java.util.Timer"};

    public JavascriptHelper(String parentDir) {
        this.modConfigDir = new File(parentDir + "/macrokey/");
    }

    public void initializeFolder() {
        this.modConfigDir.mkdirs();
    }

    public File initializeFile(String filename) throws IOException {
        initializeFolder();

        File file = new File(modConfigDir + filename);
        if (!file.exists()) {
            file.createNewFile();

            FileUtils.writeStringToFile(file, "function main() { /** TODO: Implement Macro **/ }");
        }

        return file;
    }

    public File getMacroFile(UUID uuid) throws IOException {
        return this.initializeFile("/javascript-" + uuid + ".macrokey.js");
    }

    public ClassFilter getAllowedClasses() {
        return (ClassFilter) s -> Arrays.stream(allowedClasses).filter(i -> i.equalsIgnoreCase(s)).count() >= 1;
    }
}
