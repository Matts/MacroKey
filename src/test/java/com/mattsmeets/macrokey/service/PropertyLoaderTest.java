package com.mattsmeets.macrokey.service;

import com.mattsmeets.macrokey.exception.PropertyInitalizationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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

    /*@Test
    public void testGetPropertyFromReferenceFile() throws PropertyInitalizationException {
        PropertyLoader loader = new PropertyLoader("reference.properties");

        // need to do this since the reference.properties file is
        // set when building on CI server (or from CLI)
        String buildNum = System.getProperty("BUILD_NUMBER");
        if(buildNum == null) {
            assertEquals("${mod_version}", loader.getProperty("mod_version"));
            assertEquals("${mc_version}", loader.getProperty("mc_version"));
            assertEquals("${mod_name}", loader.getProperty("mod_name"));
        } else {
            assertEquals("MacroKey Keybinding", loader.getProperty("mod_name"));
            assertEquals("1.12", loader.getProperty("mc_version"));
        }
    }*/

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
