package com.capstone.backend.entity.type;

import com.capstone.backend.entity.ResourceTag;
import lombok.Getter;

import java.util.List;

@Getter
public enum ResourceType {
    PDF,
    PPT,
    PPTX,
    DOC,
    DOCX,
    JPG,
    JPEG,
    PNG,
    MP3,
    MP4;

    public static List<ResourceType> getFeeList() {
        return List.of(PPT, PPTX, PDF, DOC, DOCX);
    }
    public static List<ResourceType> getFreeList() {
        return List.of(JPG, JPEG, PNG, MP3, MP4);
    }
    public static List<ResourceType> getListImages() {
        return List.of(JPG, JPEG, PNG);
    }
    public static List<ResourceType> getResourceByLesson() {
        return List.of(PPT, PPTX, PDF, DOC, DOCX, MP3);
    }
    public static List<ResourceType> getResourceByMedia() {
        return List.of(MP4, PNG, JPEG, JPG);
    }
}
