package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.*;
import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.type.ActionType;
import com.capstone.backend.model.dto.resource.*;
import com.capstone.backend.model.dto.tag.TagSuggestDTOResponse;
import com.capstone.backend.repository.UserResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static com.capstone.backend.utils.Constants.HOST;
import static com.capstone.backend.utils.Constants.HOST_SERVER;

public class ResourceMapper {
    public static ResourceDTOResponse toResourceDTOResponse(Resource resource) {
        return ResourceDTOResponse.builder()
                .id(resource.getId())
                .name(resource.getName())
                .description(resource.getDescription())
                .resourceType(resource.getResourceType())
                .createdAt(resource.getCreatedAt())
                .active(resource.getActive())
                .approveType(resource.getApproveType())
                .visualType(resource.getVisualType())
                .thumbnailSrc(HOST_SERVER + "/" + resource.getThumbnailSrc())
                .resourceSrc(HOST_SERVER + "/" + resource.getResourceSrc())
                .point(resource.getPoint())
                .size(resource.getSize())
                .build();
    }

    public static ResourceViewDTOResponse toResourceViewDTOResponse(Resource resource, boolean isSave) {
        return ResourceViewDTOResponse.builder()
                .id(resource.getId())
                .thumbnailSrc(HOST_SERVER + "/" + resource.getThumbnailSrc())
                .point(resource.getPoint())
                .name(resource.getName())
                .isSave(isSave)
                .build();
    }

    public static ResourceMediaDTOCriteria toResourceMediaDTOCriteria(ResourceTag resourceTag) {
        return ResourceMediaDTOCriteria.builder()
                .tableType(resourceTag.getTableType())
                .detailId(resourceTag.getDetailId())
                .build();
    }

    public static UserSharedDTOResponse toUserSharedDTOResponse(User user, String permission) {
        return UserSharedDTOResponse.builder()
                .userShareId(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .permission(permission)
                .build();
    }

    public static ResourceDTOUpdateResponse toResourceDTOUpdateResponse(Resource resource) {
        Class classObject = null;
        Chapter chapter = null;
        BookVolume bookVolume = null;
        Subject subject = resource.getSubject();
        BookSeries bookSeries = null;
        Lesson lesson = resource.getLesson();
        if(lesson != null) {
            bookSeries = subject.getBookSeriesSubjects().get(0).getBookSeries();
            classObject = bookSeries.getClassObject();
            chapter = lesson.getChapter();
            bookVolume = chapter.getBookVolume();
        }
        List<TagSuggestDTOResponse> tagList = resource.getResourceTagList().stream()
                .map(ResourceMapper::toTagSuggestDTOResponse)
                .toList();

        return ResourceDTOUpdateResponse.builder()
                .id(resource.getId())
                .approveType(resource.getApproveType())
                .description(resource.getDescription())
                .name(resource.getName())
                .resourceSrc(resource.getResourceSrc())
                .resourceType(resource.getResourceType())
                .lessonId(lesson != null ? lesson.getId() : null)
                .chapterId(chapter != null ? chapter.getId() : null)
                .bookVolumeId(bookVolume != null ? bookVolume.getId() : null)
                .subjectId(subject.getId())
                .bookSeriesId(bookSeries != null ? bookSeries.getId() : null)
                .classId(classObject != null ? classObject.getId() : null)
                .tagList(tagList)
                .visualType(resource.getVisualType())
                .build();
    }

    public static TagSuggestDTOResponse toTagSuggestDTOResponse(ResourceTag resourceTag) {
        return TagSuggestDTOResponse.builder()
                .tagId(resourceTag.getTag().getId())
                .tagName(resourceTag.getTag().getName())
                .build();
    }
}
