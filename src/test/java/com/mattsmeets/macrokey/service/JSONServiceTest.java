package com.mattsmeets.macrokey.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class JSONServiceTest {

    @Mock
    private JSONService jsonService;

    @Test
    public void loadJSONElementFromFileReturnsNullWhenFileDoesNotExist() throws IOException {
        File file = new File("bindings.json");

        assertNull(jsonService.loadJSONElementFromFile(file));
    }

}
