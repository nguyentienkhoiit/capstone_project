package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.type.ActionType;
import com.capstone.backend.entity.type.PermissionResourceType;
import com.capstone.backend.entity.type.TableType;
import com.capstone.backend.model.dto.resource.PagingResourceDTOResponse;
import com.capstone.backend.model.dto.resource.ResourceMediaDTOCriteria;
import com.capstone.backend.model.dto.resource.ResourceMediaDTOFilter;
import com.capstone.backend.model.dto.resource.ResourceViewDTOResponse;
import com.capstone.backend.model.mapper.ResourceMapper;
import com.capstone.backend.repository.UserResourceRepository;
import com.capstone.backend.utils.CheckPermissionResource;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceCriteria {
    EntityManager em;
    UserHelper userHelper;
    UserResourceRepository userResourceRepository;
    CheckPermissionResource checkPermissionResource;

    public PagingResourceDTOResponse searchMediaResource(
            Set<ResourceMediaDTOCriteria> resourceMediaDTOCriteriaList,
            ResourceMediaDTOFilter resourceMediaDTOFilter
    ) {

        User userLoggedIn = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        Set<Long> listClassIds = getResourceIdsByCriteria(TableType.class_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listBookSeriesIds = getResourceIdsByCriteria(TableType.book_series_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listSubjectIds = getResourceIdsByCriteria(TableType.subject_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listBookVolumeIds = getResourceIdsByCriteria(TableType.book_volume_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listChapterIds = getResourceIdsByCriteria(TableType.chapter_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listLessonIds = getResourceIdsByCriteria(TableType.lesson_tbl, resourceMediaDTOCriteriaList);
        Set<Long> listResourceIds = getResourceIdsByCriteria(TableType.resource_tbl, resourceMediaDTOCriteriaList);

        // Create SQL based on criteria
        if (!listClassIds.isEmpty()) {
            appendSql(sql, "c.id IN (:listClassId)");
            params.put("listClassId", listClassIds);
        }

        if (!listBookSeriesIds.isEmpty()) {
            appendSql(sql, "bs.id IN (:listBookSeriesId)");
            params.put("listBookSeriesId", listBookSeriesIds);
        }

        if (!listSubjectIds.isEmpty()) {
            appendSql(sql, "su.id IN (:listSubjectId)");
            params.put("listSubjectId", listSubjectIds);
        }

        if (!listBookVolumeIds.isEmpty()) {
            appendSql(sql, "bv.id IN (:listBookVolumeId)");
            params.put("listBookVolumeId", listBookVolumeIds);
        }

        if (!listChapterIds.isEmpty()) {
            appendSql(sql, "cha.id IN (:listChapterId)");
            params.put("listChapterId", listChapterIds);
        }

        if (!listLessonIds.isEmpty()) {
            appendSql(sql, "le.id IN (:listLessonId)");
            params.put("listLessonId", listLessonIds);
        }

        if (!listResourceIds.isEmpty()) {
            appendResourceSql(sql);
            params.put("listResourceId", listResourceIds);
        }

        if(resourceMediaDTOFilter.getName() == null) {
            resourceMediaDTOFilter.setName("");
        }
        appendSql(sql, "r.name like :name");
        params.put("name", resourceMediaDTOFilter.getName() + "%");

        Long pageIndex = resourceMediaDTOFilter.getPageIndex();
        Long pageSize = resourceMediaDTOFilter.getPageSize();


        NativeQuery<Resource> nativeQuery = (NativeQuery<Resource>) em.createNativeQuery(sql.toString(), Resource.class);

        params.forEach(nativeQuery::setParameter);
        TypedQuery<Resource> resourceTypedQuery = nativeQuery.unwrap(TypedQuery.class);

        System.out.println(sql);
        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resourceList = resourceTypedQuery.getResultList();

        resourceList = resourceList.stream()
                .filter(resource -> {
                    if (resourceMediaDTOFilter.getName() == null) {
                        resourceMediaDTOFilter.setName("");
                    }
                    return resource.getTabResourceType() == resourceMediaDTOFilter.getTabResourceType();
                })
                .filter(r -> checkPermissionResource.needCheckPermissionResource(userLoggedIn, r, PermissionResourceType.V))
                .toList();

        long totalResource = resourceList.size();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

        List<ResourceViewDTOResponse> resourceViewDTOResponses = resourceList.stream()
                .map(resource -> {
                    boolean isSave = userLoggedIn != null && userResourceRepository
                            .findUserResourceHasActionType(userLoggedIn.getId(), resource.getId(), ActionType.SAVED)
                            .isPresent();
                    return ResourceMapper.toResourceViewDTOResponse(resource, isSave);
                })
                .toList();

        return PagingResourceDTOResponse.builder()
                .totalElement(totalResource)
                .totalPage(totalPage)
                .data(resourceViewDTOResponses)
                .build();
    }

    private void appendSql(StringBuilder sql, String condition) {
        if (!sql.isEmpty()) {
            sql.append(" union ");
        }
        sql.append("( select r.* from resource_tbl r join lesson_tbl le on le.id = r.lesson_id " +
                        "    join chapter_tbl cha on le.chapter_id = cha.id " +
                        "    join book_volume_tbl bv on bv.id = cha.book_volume_id " +
                        "    join book_series_subject_tbl bss on bss.id = bv.book_series_subject_id " +
                        "    join subject_tbl s on bss.subject_id = s.id " +
                        "    join book_series_tbl bs on bss.book_series_id = bs.id " +
                        "    join class_tbl c on bs.class_id = c.id where 1 = 1 and ")
                .append(condition).append(" )");
    }

    private void appendResourceSql(StringBuilder sql) {
        if (!sql.isEmpty()) {
            sql.append(" union ");
        }
        sql.append("( select r.* from resource_tbl r where r.id IN (:listResourceId))");
    }

    private Set<Long> getResourceIdsByCriteria(TableType tableType, Set<ResourceMediaDTOCriteria> criteriaList) {
        return criteriaList.stream()
                .filter(criteria -> criteria.getTableType() == tableType)
                .map(ResourceMediaDTOCriteria::getDetailId)
                .collect(Collectors.toSet());
    }
}
