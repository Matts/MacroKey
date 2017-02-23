package com.mattsmeets.macrokey.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by Matt on 2/23/2017.
 */
public class FileHelper {
    public static void fileExist(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
