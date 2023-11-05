package com.capstone.backend.entity.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum TabResourceType {
    SLIDE(List.of(ResourceType.PPTX, ResourceType.PPT)),
    SHEET(List.of(ResourceType.DOC, ResourceType.DOCX, ResourceType.PDF)),
    IMAGE(List.of(ResourceType.PNG, ResourceType.JPEG, ResourceType.JPG)),
    VIDEO(List.of(ResourceType.MP4)),
    AUDIO(List.of(ResourceType.MP3));

    List<ResourceType> resourceTypeList;

    public static TabResourceType findByTabResourceType(ResourceType targetType) {
        for (TabResourceType tabResourceType : values()) {
            if (tabResourceType.resourceTypeList.contains(targetType)) {
                return tabResourceType;
            }
        }
        return null;
    }

    public static List<TabResourceType> getTabResourceTypes() {
        return List.of(SHEET, SLIDE, IMAGE, VIDEO, AUDIO);
    }
}
