package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.type.ActionType;
import com.capstone.backend.model.dto.resource.PagingResourceDTOResponse;
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
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResourceCriteria {
    EntityManager em;
    UserHelper userHelper;
    UserResourceRepository userResourceRepository;
    CheckPermissionResource checkPermissionResource;

    public PagingResourceDTOResponse searchMediaResource(Set<Long> listResourceGrouped, ResourceMediaDTOFilter resourceDTOFilter) {
        User userLoggedIn = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder("select r from Resource r where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();

        if (!listResourceGrouped.isEmpty()) {
            sql.append("and r.id in (:listResourceGrouped) ");
            params.put("listResourceGrouped", listResourceGrouped);
        }

        if (resourceDTOFilter.getName() != null) {
            sql.append(" and r.name like :name ");
            params.put("name", "%" + resourceDTOFilter.getName() + "%");
        }

        sql.append(" and r.tabResourceType = :tabResourceType ");
        params.put("tabResourceType", resourceDTOFilter.getTabResourceType());

        Query countQuery = em.createQuery(sql.toString().replace("select r", "select count(r.id)"));

        sql.append(" order by r.createdAt ");

        Long pageIndex = resourceDTOFilter.getPageIndex();
        Long pageSize = resourceDTOFilter.getPageSize();

        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);

        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resourceList = resourceTypedQuery.getResultList().stream()
                .filter(checkPermissionResource::needCheckPermissionSearchResource)
                .toList();

        Long totalResource = (Long) countQuery.getSingleResult();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

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

        return PagingResourceDTOResponse.builder()
                .totalElement(totalResource)
                .totalPage(totalPage)
                .data(resourceViewDTOResponses)
                .build();

    }


}
