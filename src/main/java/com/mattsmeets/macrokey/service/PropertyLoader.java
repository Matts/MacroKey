package com.mattsmeets.macrokey.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mattsmeets.macrokey.MacroKey;
import com.mattsmeets.macrokey.exception.PropertyInitalizationException;

public class PropertyLoader {

    private Properties properties;

    /**
     * Will initialize the PropertyLoader with the given file
     *
     * @param filename name of the file, relative to the mod's classpath
     */
    public PropertyLoader(String filename) throws PropertyInitalizationException {
        this(MacroKey.class.getResourceAsStream(filename));
    }

    /**
     * Will initialize the PropertyLoader with the given file
     *
     * @param inputStream an instance of inputStream,
     *                    pointing to the property file
     */
    public PropertyLoader(InputStream inputStream) throws PropertyInitalizationException {
        try {
            this.properties = new Properties();
            this.properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            this.properties = null;

            throw new PropertyInitalizationException("Could not load the file given");
        }
    }

    /**
     * Will initialize the PropertyLoader with the given Properties
     *
     * @param properties properties
     */
    public PropertyLoader(Properties properties) {
        this.properties = properties;
    }

    /**
     * Fetches the values from the given key
     *
     * @param key property key
     * @return property value
     */
    public String getProperty(String key) {
         return this.properties.getProperty(key);
    }

}
