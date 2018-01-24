package com.mattsmeets.macrokey.exception;

import java.io.IOException;

/**
 * An error that may occur during setup of the properties
 */
public class PropertyInitalizationException extends IOException {

    public PropertyInitalizationException(String message) {
        super(message);
    }

}
