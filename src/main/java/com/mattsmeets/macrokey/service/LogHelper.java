package com.mattsmeets.macrokey.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private Logger logger;

    /**
     * Initializes the LogHelper with given parameters
     *
     * @param logger FMLPreInitializationEvent.getModLog()
     */
    public LogHelper(Logger logger) {
        this.logger = logger;
    }

    public void log(Level logLevel, Object object) {
        this.logger.log(logLevel, object);
    }

    public void info(Object object) {
        log(Level.INFO, object);
    }

    public void warn(Object object) {
        log(Level.WARN, object);
    }

    public void debug(Object object) {
        log(Level.DEBUG, object);
    }

}
