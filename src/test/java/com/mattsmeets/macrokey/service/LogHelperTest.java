package com.mattsmeets.macrokey.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class LogHelperTest {

    @Mock
    private Logger logger;

    @InjectMocks
    private LogHelper logHelper;

    @Test
    public void testIfDebugCallsLoggerDebug() {
        Object message = "message";

        logHelper.debug(message);

        verify(logger).log(Level.DEBUG, message);

        verifyNoMoreInteractions(logger);
    }

    @Test
    public void testIfInfoCallsLoggerInfo() {
        Object message = "message";

        logHelper.info(message);

        verify(logger).log(Level.INFO, message);

        verifyNoMoreInteractions(logger);
    }

    @Test
    public void testIfWarnCallsLoggerWarn() {
        Object message = "message";

        logHelper.warn(message);

        verify(logger).log(Level.WARN, message);

        verifyNoMoreInteractions(logger);
    }

    @Test
    public void testIfLogCallsGivenLevel() {
        Object message = "message";

        logHelper.log(Level.ALL, message);

        verify(logger).log(Level.ALL, message);

        verifyNoMoreInteractions(logger);
    }

}
