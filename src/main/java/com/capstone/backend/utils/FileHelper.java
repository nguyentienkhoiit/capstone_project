package com.capstone.backend.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

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
                String finalText = text;
                boolean invalid = Arrays.stream(content.split("\\s+"))
                        .anyMatch(t -> t.equalsIgnoreCase(finalText));
                if (invalid) return true;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
