package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.SystemPermission;
import com.capstone.backend.model.dto.systempermission.PagingSystemPermissionDTOResponse;
import com.capstone.backend.model.dto.systempermission.SystemPermissionDTOFilter;
import com.capstone.backend.model.dto.systempermission.SystemPermissionDTOResponse;
import com.capstone.backend.model.mapper.SystemPermissionMapper;
import com.capstone.backend.repository.UserRepository;
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

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SystemPermissionCriteria {
    EntityManager em;
    UserRepository userRepository;

    public PagingSystemPermissionDTOResponse viewSearchPermission(SystemPermissionDTOFilter request) {
        StringBuilder sql = new StringBuilder("select sp from SystemPermission sp where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();


        if (request.getActive() != null) {
            sql.append(" and sp.active = :active");
            params.put("active", request.getActive());
        }

        if (request.getName() != null) {
            sql.append(" and sp.name like :name ");
            params.put("name", "%" + request.getName() + "%");
        }
        sql.append(" order by sp.name asc ");

        Query countQuery = em.createQuery(sql.toString().replace("select sp", "select count(sp.id)"));

        Long pageIndex = request.getPageIndex();
        Long pageSize = request.getPageSize();

        TypedQuery<SystemPermission> systemPermissionTypedQuery = em.createQuery(sql.toString(), SystemPermission.class);

        // Set param to query
        params.forEach((k, v) -> {
            systemPermissionTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        systemPermissionTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        systemPermissionTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<SystemPermission> systemPermissions = systemPermissionTypedQuery.getResultList();

        Long totalSystemPermission = (Long) countQuery.getSingleResult();
        long totalPage = totalSystemPermission / pageSize;
        if (totalSystemPermission % pageSize != 0) {
            totalPage++;
        }

        List<SystemPermissionDTOResponse> systemPermissionDTOResponses = systemPermissions
                .stream()
                .map(sp -> SystemPermissionMapper
                        .toSystemPermissionDTOResponse(sp, userRepository
                                .findUsernameByUserId(sp.getUserId()))
                )
                .toList();

        return PagingSystemPermissionDTOResponse.builder()
                .totalElement(totalSystemPermission)
                .totalPage(totalPage)
                .data(systemPermissionDTOResponses)
                .build();
    }
}
