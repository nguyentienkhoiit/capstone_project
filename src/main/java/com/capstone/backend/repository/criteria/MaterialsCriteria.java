package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.*;
import com.capstone.backend.entity.type.*;
import com.capstone.backend.model.dto.materials.DataMaterialsDTOResponse;
import com.capstone.backend.model.dto.materials.MaterialsFilterDTORequest;
import com.capstone.backend.model.dto.materials.PagingMaterialDTOResponse;
import com.capstone.backend.model.dto.resource.ClassLessonId;
import com.capstone.backend.model.dto.resource.ResourceViewDTOResponse;
import com.capstone.backend.model.mapper.MaterialsMapper;
import com.capstone.backend.model.mapper.ResourceMapper;
import com.capstone.backend.repository.*;
import com.capstone.backend.utils.CheckPermissionResource;
import com.capstone.backend.utils.Constants;
import com.capstone.backend.utils.MessageException;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MaterialsCriteria {
    EntityManager em;
    UserHelper userHelper;
    ClassRepository classRepository;
    BookSeriesRepository bookSeriesRepository;
    SubjectRepository subjectRepository;
    BookVolumeRepository bookVolumeRepository;
    ChapterRepository chapterRepository;
    LessonRepository lessonRepository;
    UserResourceRepository userResourceRepository;
    ResourceRepository resourceRepository;
    CheckPermissionResource checkPermissionResource;

    public DataMaterialsDTOResponse searchMaterials(MaterialsFilterDTORequest request) {
        User userLoggedIn = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder("select re from Resource re left join re.lesson le left join " +
                "le.chapter cha left join cha.bookVolume bv left join bv.bookSeriesSubject bvs left join " +
                "bvs.subject s left join bvs.bookSeries bs left join bs.classObject c where 1 = 1");

        Map<String, Object> params = new HashMap<>();

        List<Class> classes = classRepository.findClassByActiveIsTrue();

        var data = new Object() {
            Class classCurrent = classes.get(0);
            BookSeries bookSeriesCurrent = classCurrent.getBookSeriesList().get(0);
            Subject subjectCurrent = bookSeriesCurrent.getBookSeriesSubjects().get(0).getSubject();
            BookVolume bookVolumeCurrent = subjectCurrent.getBookSeriesSubjects().get(0).getBookVolumes().get(0);
            Chapter chapterCurrent = bookVolumeCurrent.getChapterList().get(0);
            Lesson lessonCurrent = chapterCurrent.getLessonList().get(0);
            TabResourceType tabResourceType = TabResourceType.SLIDE;
            Long pageIndex = Constants.DEFAULT_PAGE_INDEX;
            Long pageSize = Constants.DEFAULT_PAGE_SIZE;
            Long totalPage = 0L;
            Long totalResource = 0L;
        };

        if (request.getClassId() != null) {
            data.classCurrent = classRepository
                    .findByIdAndActiveTrue(request.getClassId())
                    .orElseGet(() -> data.classCurrent);
            sql.append(" and c.id = :classId and c.active = true");
            params.put("classId", data.classCurrent.getId());
        }

        if (request.getBookSeriesId() != null) {
            data.bookSeriesCurrent = bookSeriesRepository
                    .findByIdAndActiveTrue(request.getBookSeriesId())
                    .orElseGet(() -> data.bookSeriesCurrent);
            sql.append(" and bs.id = :bookSeriesId and bs.active = true");
            params.put("bookSeriesId", data.bookSeriesCurrent.getId());
        }

        if (request.getSubjectId() != null) {
            data.subjectCurrent = subjectRepository
                    .findByIdAndActiveTrue(request.getSubjectId())
                    .orElseGet(() -> data.subjectCurrent);
            sql.append(" and s.id = :subjectId and s.active = true");
            params.put("subjectId", data.subjectCurrent.getId());
        }

        if (request.getBookVolumeId() != null) {
            data.bookVolumeCurrent = bookVolumeRepository
                    .findByIdAndActiveTrue(request.getBookVolumeId())
                    .orElseGet(() -> data.bookVolumeCurrent);
            sql.append(" and bv.id = :bookVolumeId and bv.active = true");
            params.put("bookVolumeId", data.bookVolumeCurrent.getId());
        }

        if (request.getChapterId() != null) {
            data.chapterCurrent = chapterRepository
                    .findByIdAndActiveTrue(request.getChapterId())
                    .orElseGet(() -> data.chapterCurrent);
            sql.append(" and cha.id = :chapterId and cha.active = true");
            params.put("chapterId", data.chapterCurrent.getId());
        }

        if (request.getLessonId() != null) {
            data.lessonCurrent = lessonRepository
                    .findByIdAndActiveTrue(request.getLessonId())
                    .orElseGet(() -> data.lessonCurrent);
            sql.append(" and le.id = :lessonId and le.active = true");
            params.put("lessonId", data.lessonCurrent.getId());
        }

        if (request.getTabResourceType() != null) {
            data.tabResourceType = request.getTabResourceType();
        }
        sql.append(" and re.tabResourceType = :tabResourceType");
        params.put("tabResourceType", data.tabResourceType);

        sql.append(" and re.approveType = :approveType ");
        params.put("approveType", ApproveType.ACCEPTED);

        sql.append(" and re.visualType = :visualType ");
        params.put("visualType", VisualType.PUBLIC);

        sql.append(" order by re.createdAt ");

        data.pageIndex = request.getPageIndex() != null ? request.getPageIndex() : data.pageIndex;
        data.pageSize = request.getPageSize() != null ? request.getPageSize() : data.pageSize;

        Query countQuery = em.createQuery(sql.toString().replace("select re", "select count(re.id)"));


        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);
        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });


        resourceTypedQuery.setFirstResult((int) ((data.pageIndex - 1) * data.pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(data.pageSize));
        List<Resource> resourceList = resourceTypedQuery.getResultList().stream()
                .filter(r -> checkPermissionResource.needCheckPermissionResource(userLoggedIn, r, PermissionResourceType.V))
                .toList();

        data.totalResource = (Long) countQuery.getSingleResult();
        data.totalPage = data.totalResource / data.pageSize;
        if (data.totalResource % data.pageSize != 0) {
            data.totalPage++;
        }
        System.out.println(data.totalResource);

        List<ResourceViewDTOResponse> resourceViewDTOResponses = resourceList.stream()
                .map(resource -> {
                    boolean isSave = false;
                    if (userLoggedIn != null)
                        isSave = userResourceRepository
                                .findUserResourceHasActionType(userLoggedIn.getId(), resource.getId(), ActionType.SAVED)
                                .isPresent();
                    return ResourceMapper.toResourceViewDTOResponse(resource, isSave);
                })
                .toList();

        var classMaterialsDTOResponseList = classes.stream().map(c -> {
            var bookSeriesMaterialsDTOResponseList = c.getBookSeriesList().stream().map(bs -> {
                var subjectMaterialsDTOResponses = bs.getBookSeriesSubjects().stream().map(s -> {
                    var bookVolumeMaterialsDTOResponses = s.getBookVolumes().stream().map(bv -> {
                        var chapterMaterialsDTOResponses = bv.getChapterList().stream().map(ch -> {
                            var lessonMaterialsDTOResponses = ch.getLessonList().stream().map(le -> {
                                var pagingMaterialDTOResponse = PagingMaterialDTOResponse.builder()
                                        .totalElement(data.totalResource)
                                        .totalPage(data.totalPage)
                                        .data(resourceViewDTOResponses)
                                        .build();
                                var lessonMaterialsDTOResponse = MaterialsMapper.toLessonMaterialsDTOResponse(le);
                                if (Objects.equals(data.lessonCurrent.getId(), le.getId())) {
                                    lessonMaterialsDTOResponse.setActive(true);
                                    if (resourceRepository.existsResourceByLessonId(data.lessonCurrent.getId())) {
                                        lessonMaterialsDTOResponse.setPagingMaterialDTOResponse(pagingMaterialDTOResponse);
                                    }
                                }
                                return lessonMaterialsDTOResponse;
                            }).toList();
                            var chapterMaterialsDTOResponse = MaterialsMapper.toChapterMaterialsDTOResponse(ch);
                            if (Objects.equals(data.chapterCurrent.getId(), ch.getId())) {
                                chapterMaterialsDTOResponse.setActive(true);

                                chapterMaterialsDTOResponse.setLessons(lessonMaterialsDTOResponses);
                            }
                            return chapterMaterialsDTOResponse;
                        }).toList();
                        var bookVolumeMaterialsDTOResponse = MaterialsMapper.toBookVolumeMaterialsDTOResponse(bv);
                        if (Objects.equals(data.bookVolumeCurrent.getId(), bv.getId())) {
                            bookVolumeMaterialsDTOResponse.setActive(true);
                            bookVolumeMaterialsDTOResponse.setChapters(chapterMaterialsDTOResponses);
                        }
                        return bookVolumeMaterialsDTOResponse;
                    }).toList();

                    var subjectMaterialsDTOResponse = MaterialsMapper.toSubjectMaterialsDTOResponse(s.getSubject());
                    if (Objects.equals(data.subjectCurrent.getId(), s.getId())) {
                        subjectMaterialsDTOResponse.setActive(true);
                        subjectMaterialsDTOResponse.setBookVolumes(bookVolumeMaterialsDTOResponses);
                    }
                    return subjectMaterialsDTOResponse;
                }).toList();

                var bookSeriesMaterialsDTOResponse = MaterialsMapper.toBookSeriesMaterialsDTOResponse(bs);
                if (Objects.equals(data.bookSeriesCurrent.getId(), bs.getId())) {
                    bookSeriesMaterialsDTOResponse.setActive(true);
                    bookSeriesMaterialsDTOResponse.setSubjects(subjectMaterialsDTOResponses);
                }
                return bookSeriesMaterialsDTOResponse;
            }).toList();

            var classMaterialsDTOResponse = MaterialsMapper.toClassMaterialsDTOResponse(c);
            if (Objects.equals(data.classCurrent.getId(), c.getId())) {
                classMaterialsDTOResponse.setBookSeries(bookSeriesMaterialsDTOResponseList);
                classMaterialsDTOResponse.setActive(true);
            }
            return classMaterialsDTOResponse;
        }).toList();

        return DataMaterialsDTOResponse.builder()
                .classes(classMaterialsDTOResponseList)
                .build();
    }


    public DataMaterialsDTOResponse getFromClassToLesson(ClassLessonId request) {
        User userLoggedIn = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder("select re from Resource re left join re.lesson le left join " +
                "le.chapter cha left join cha.bookVolume bv left join bv.bookSeriesSubject bvs left join " +
                "bvs.subject s left join bvs.bookSeries bs left join bs.classObject c where 1 = 1");

        Map<String, Object> params = new HashMap<>();

        List<Class> classes = classRepository.findClassByActiveIsTrue();

        var data = new Object() {
            Class classCurrent = classes.get(0);
            BookSeries bookSeriesCurrent = classCurrent.getBookSeriesList().get(0);
            Subject subjectCurrent = bookSeriesCurrent.getBookSeriesSubjects().get(0).getSubject();
            BookVolume bookVolumeCurrent = subjectCurrent.getBookSeriesSubjects().get(0).getBookVolumes().get(0);
            Chapter chapterCurrent = bookVolumeCurrent.getChapterList().get(0);
            Lesson lessonCurrent = chapterCurrent.getLessonList().get(0);
        };

        if (request.getClassId() != null) {
            data.classCurrent = classRepository
                    .findByIdAndActiveTrue(request.getClassId())
                    .orElseGet(() -> data.classCurrent);
            sql.append(" and c.id = :classId and c.active = true");
            params.put("classId", data.classCurrent.getId());
        }

        if (request.getBookSeriesId() != null) {
            data.bookSeriesCurrent = bookSeriesRepository
                    .findByIdAndActiveTrue(request.getBookSeriesId())
                    .orElseGet(() -> data.bookSeriesCurrent);
            sql.append(" and bs.id = :bookSeriesId and bs.active = true");
            params.put("bookSeriesId", data.bookSeriesCurrent.getId());
        }

        if (request.getSubjectId() != null) {
            data.subjectCurrent = subjectRepository
                    .findByIdAndActiveTrue(request.getSubjectId())
                    .orElseGet(() -> data.subjectCurrent);
            sql.append(" and s.id = :subjectId and s.active = true");
            params.put("subjectId", data.subjectCurrent.getId());
        }

        if (request.getBookVolumeId() != null) {
            data.bookVolumeCurrent = bookVolumeRepository
                    .findByIdAndActiveTrue(request.getBookVolumeId())
                    .orElseGet(() -> data.bookVolumeCurrent);
            sql.append(" and bv.id = :bookVolumeId and bv.active = true");
            params.put("bookVolumeId", data.bookVolumeCurrent.getId());
        }

        if (request.getChapterId() != null) {
            data.chapterCurrent = chapterRepository
                    .findByIdAndActiveTrue(request.getChapterId())
                    .orElseGet(() -> data.chapterCurrent);
            sql.append(" and cha.id = :chapterId and cha.active = true");
            params.put("chapterId", data.chapterCurrent.getId());
        }

        if (request.getLessonId() != null) {
            data.lessonCurrent = lessonRepository
                    .findByIdAndActiveTrue(request.getLessonId())
                    .orElseGet(() -> data.lessonCurrent);
            sql.append(" and le.id = :lessonId and le.active = true");
            params.put("lessonId", data.lessonCurrent.getId());
        }
        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);
        params.forEach(resourceTypedQuery::setParameter);

        var classMaterialsDTOResponseList = classes.stream().map(c -> {
            var bookSeriesMaterialsDTOResponseList = c.getBookSeriesList().stream().map(bs -> {
                var subjectMaterialsDTOResponses = bs.getBookSeriesSubjects().stream().map(s -> {
                    var bookVolumeMaterialsDTOResponses = s.getBookVolumes().stream().map(bv -> {
                        var chapterMaterialsDTOResponses = bv.getChapterList().stream().map(ch -> {
                            var lessonMaterialsDTOResponses = ch.getLessonList().stream().map(le -> {
                                var lessonMaterialsDTOResponse = MaterialsMapper.toLessonMaterialsDTOResponse(le);
                                if (Objects.equals(data.lessonCurrent.getId(), le.getId())) {
                                    lessonMaterialsDTOResponse.setActive(true);
                                }
                                return lessonMaterialsDTOResponse;
                            }).toList();
                            var chapterMaterialsDTOResponse = MaterialsMapper.toChapterMaterialsDTOResponse(ch);
                            if (Objects.equals(data.chapterCurrent.getId(), ch.getId())) {
                                chapterMaterialsDTOResponse.setActive(true);

                                chapterMaterialsDTOResponse.setLessons(lessonMaterialsDTOResponses);
                            }
                            return chapterMaterialsDTOResponse;
                        }).toList();
                        var bookVolumeMaterialsDTOResponse = MaterialsMapper.toBookVolumeMaterialsDTOResponse(bv);
                        if (Objects.equals(data.bookVolumeCurrent.getId(), bv.getId())) {
                            bookVolumeMaterialsDTOResponse.setActive(true);
                            bookVolumeMaterialsDTOResponse.setChapters(chapterMaterialsDTOResponses);
                        }
                        return bookVolumeMaterialsDTOResponse;
                    }).toList();

                    var subjectMaterialsDTOResponse = MaterialsMapper.toSubjectMaterialsDTOResponse(s.getSubject());
                    if (Objects.equals(data.subjectCurrent.getId(), s.getId())) {
                        subjectMaterialsDTOResponse.setActive(true);
                        subjectMaterialsDTOResponse.setBookVolumes(bookVolumeMaterialsDTOResponses);
                    }
                    return subjectMaterialsDTOResponse;
                }).toList();

                var bookSeriesMaterialsDTOResponse = MaterialsMapper.toBookSeriesMaterialsDTOResponse(bs);
                if (Objects.equals(data.bookSeriesCurrent.getId(), bs.getId())) {
                    bookSeriesMaterialsDTOResponse.setActive(true);
                    bookSeriesMaterialsDTOResponse.setSubjects(subjectMaterialsDTOResponses);
                }
                return bookSeriesMaterialsDTOResponse;
            }).toList();

            var classMaterialsDTOResponse = MaterialsMapper.toClassMaterialsDTOResponse(c);
            if (Objects.equals(data.classCurrent.getId(), c.getId())) {
                classMaterialsDTOResponse.setBookSeries(bookSeriesMaterialsDTOResponseList);
                classMaterialsDTOResponse.setActive(true);
            }
            return classMaterialsDTOResponse;
        }).toList();

        return DataMaterialsDTOResponse.builder()
                .classes(classMaterialsDTOResponseList)
                .build();
    }
}
