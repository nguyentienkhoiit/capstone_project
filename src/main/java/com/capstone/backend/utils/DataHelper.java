package com.capstone.backend.utils;

import org.springframework.http.MediaType;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class DataHelper {
    public static Set<Long> parseStringToLongSet(String str) {
        str = str.replaceAll("[\\[\\] ]", "");
        String[] tagArray = str.split(",");
        Set<Long> tags = new HashSet<>();
        Arrays.stream(tagArray).forEach(tag -> tags.add(Long.parseLong(tag)));
        return tags;
    }

    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replace("đ", "d");
    }

    public static String extractFileExtension(String fileName) {
        if (fileName != null && fileName.lastIndexOf(".") != -1) {
            return fileName.substring(fileName.lastIndexOf("."))
                    .replace(".", "");
        }
        return "";
    }

    public static String extractFilename(String filename) {
        return filename.split("\\.")[0];
    }

    public static String concatExtension(String filename, String extension) {
        return filename.concat(".").concat(extension);
    }

    public static String getContentType(String fileType) {
        return switch (fileType) {
            case "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "mp3" -> "audio/mpeg";
            case "mp4" -> "video/mp4";
            case "pdf" -> "application/pdf";
            case "jpg", "png", "jpeg" -> "image/jpeg";
            default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
        };
    }

    public static String parseArray(Set<Long> numbers) {
        String result = numbers.toString().replaceAll("[\\[\\]]", "").replaceAll(", ", ", ");
        result = "(" + result + ")";
        return result;
    }

    public static String generateFilename(String filename, String extension) {
        String name = filename.concat("-").concat(String.valueOf(System.currentTimeMillis()))
                .concat(".").concat(extension);
        name = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "-");
        return name;
    }
}
