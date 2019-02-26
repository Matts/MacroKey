package com.mattsmeets.macrokey.service;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

public class JavascriptHelper {

    public final File modConfigDir;

    public JavascriptHelper(String parentDir) {
        this.modConfigDir = new File(parentDir + "/macrokey/");
    }

    public void initializeFolder() {
        this.modConfigDir.mkdirs();
    }

    public File getFile(String filename) throws IOException {
        initializeFolder();

        return new File(modConfigDir + filename);
    }

    public boolean makeFile(File file) throws IOException {
        if(!file.exists()) {
            file.createNewFile();

            return true;
        }

        return false;
    }

    public File getMacroFile(UUID uuid) throws IOException {
        File file = this.getFile("/javascript-" + uuid + ".macrokey.js");
        if(makeFile(file)) {
            FileUtils.writeStringToFile(file, "const main = () => { /** TODO: Implement Macro **/ }", Charset.defaultCharset());
        }

        return file;
    }
}
