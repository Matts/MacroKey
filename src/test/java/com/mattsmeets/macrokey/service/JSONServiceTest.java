package com.mattsmeets.macrokey.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JSONServiceTest {

    @Mock
    private JSONService jsonService;

    @Test(expected = FileNotFoundException.class)
    public void loadJSONElementFromFileReturnsFileNotFoundExceptionWhenFileNotFound() throws IOException {
        File file = new File("");

        new InputStreamReader(new FileInputStream(file), "UTF-8");

        jsonService.loadJSONElementFromFile(file);
    }

}
