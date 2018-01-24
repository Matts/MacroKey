package com.mattsmeets.macrokey.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.mattsmeets.macrokey.exception.PropertyInitalizationException;

@RunWith(MockitoJUnitRunner.class)
public class PropertyLoaderTest {

    @Test
    public void testInitializationWithString() throws PropertyInitalizationException {
        PropertyLoader loader = new PropertyLoader("reference.properties");
    }

    @Test(expected = PropertyInitalizationException.class)
    public void testInitializationWithInvalidString() throws PropertyInitalizationException {
        PropertyLoader loader = new PropertyLoader("does.not.exist");
    }

    @Test
    public void testGetPropertyFromReferenceFile() throws PropertyInitalizationException {
        PropertyLoader loader = new PropertyLoader("reference.properties");

        assertEquals("${version}", loader.getProperty("version"));
        assertEquals("${mcversion}", loader.getProperty("mcversion"));
    }

    @Test
    public void testPropertyCalls() {
        Properties properties = mock(Properties.class);

        when(properties.getProperty(anyString())).thenReturn("cool!");

        PropertyLoader loader = new PropertyLoader(properties);

        assertEquals("cool!", loader.getProperty("macrokey"));

        verify(properties).getProperty("macrokey");

        verifyNoMoreInteractions(properties);
    }

}
