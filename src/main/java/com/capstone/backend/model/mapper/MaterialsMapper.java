package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.*;
import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.type.TabResourceType;
import com.capstone.backend.model.dto.materials.*;
import com.capstone.backend.utils.Constants;

import java.util.List;

public class MaterialsMapper {
    public static LessonMaterialsDTOResponse toLessonMaterialsDTOResponse(Lesson lesson) {
        return LessonMaterialsDTOResponse.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .build();
    }

    public static ChapterMaterialsDTOResponse toChapterMaterialsDTOResponse(Chapter chapter) {
        return ChapterMaterialsDTOResponse.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .build();
    }

    public static BookVolumeMaterialsDTOResponse toBookVolumeMaterialsDTOResponse(BookVolume bookVolume) {
        return BookVolumeMaterialsDTOResponse.builder()
                .id(bookVolume.getId())
                .name(bookVolume.getName())
                .build();
    }

    public static SubjectMaterialsDTOResponse toSubjectMaterialsDTOResponse(Subject subject) {
        return SubjectMaterialsDTOResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .build();
    }

    public static BookSeriesMaterialsDTOResponse toBookSeriesMaterialsDTOResponse(BookSeries bookSeries) {
        return BookSeriesMaterialsDTOResponse.builder()
                .id(bookSeries.getId())
                .name(bookSeries.getName())
                .build();
    }

    public static ClassMaterialsDTOResponse toClassMaterialsDTOResponse(Class classObject) {
        return ClassMaterialsDTOResponse.builder()
                .id(classObject.getId())
                .name(classObject.getName())
                .build();
    }

    public static boolean checkIntegerNumber(String root) {
        if (root == null) return false;
        return root.matches("-?\\d+");
    }

    public static Long parseLong(String root) {
        if (root == null) return Constants.DEFAULT_VALUE;
        return Long.parseLong(root);
    }

    public static MaterialsFilterDTORequest toMaterialsFilterDTORequest(
            MaterialFilterProtectDTORequest request
    ) {
        Long classId = checkIntegerNumber(request.getClassId()) ? parseLong(request.getClassId()) : Constants.DEFAULT_VALUE;
        Long bookSeriesId = checkIntegerNumber(request.getBookSeriesId()) ? parseLong(request.getBookSeriesId()) : Constants.DEFAULT_VALUE;
        Long subjectId = checkIntegerNumber(request.getSubjectId()) ? parseLong(request.getSubjectId()) : Constants.DEFAULT_VALUE;
        Long bookVolumeId = checkIntegerNumber(request.getBookVolumeId()) ? parseLong(request.getBookVolumeId()) : Constants.DEFAULT_VALUE;
        Long chapterId = checkIntegerNumber(request.getChapterId()) ? parseLong(request.getChapterId()) : Constants.DEFAULT_VALUE;
        Long lessonId = checkIntegerNumber(request.getLessonId()) ? parseLong(request.getLessonId()) : Constants.DEFAULT_VALUE;

        boolean isTab = TabResourceType.getTabResourceTypes().stream()
                .anyMatch(tab -> tab.toString().equalsIgnoreCase(request.getTabResourceType()));
        TabResourceType tabResourceType = isTab ? TabResourceType.valueOf(request.getTabResourceType()) : TabResourceType.SLIDE;

        Long pageIndex = checkIntegerNumber(request.getPageIndex()) ? parseLong(request.getPageIndex()) : Constants.DEFAULT_VALUE;
        Long pageSize = checkIntegerNumber(request.getPageSize()) ? parseLong(request.getPageSize()) : Constants.DEFAULT_VALUE;
        return MaterialsFilterDTORequest.builder()
                .classId(classId)
                .bookSeriesId(bookSeriesId)
                .subjectId(subjectId)
                .chapterId(chapterId)
                .lessonId(lessonId)
                .bookVolumeId(bookVolumeId)
                .pageIndex(pageIndex)
                .tabResourceType(tabResourceType)
                .pageSize(pageSize)
                .build();
    }
}
