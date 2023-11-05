package com.capstone.backend.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {

    public static boolean checkContentInputValid(String content) {
        String filename = "validate_word.txt";
        try {
            File file = new File(filename);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String text;
            while ((text = bufferedReader.readLine()) != null) {
                if (content.toLowerCase().contains(text.toLowerCase())) {
                    System.out.println(text);
                    return true;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
