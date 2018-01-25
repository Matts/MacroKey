package com.mattsmeets.macrokey;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.mattsmeets.macrokey.exception.PropertyInitalizationException;

@RunWith(MockitoJUnitRunner.class)
public class MacroKeyTest {

    @Test
    public void testThatPropLoaderIsInitialized() throws PropertyInitalizationException {
        MacroKey macroKey = new MacroKey();

        assertNotNull(macroKey.referencePropLoader);
    }
}
