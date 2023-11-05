package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Role;
import com.capstone.backend.model.dto.role.PagingRoleDTOResponse;
import com.capstone.backend.model.dto.role.RoleDTOFilter;
import com.capstone.backend.model.dto.role.RoleDTOResponse;
import com.capstone.backend.model.mapper.RoleMapper;
import com.capstone.backend.repository.UserRepository;
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

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleCriteria {
    EntityManager em;
    UserHelper userHelper;
    UserRepository userRepository;


    public PagingRoleDTOResponse viewSearchRole(RoleDTOFilter request) {
        StringBuilder sql = new StringBuilder("select r from Role r where 1 = 1 ");

        Map<String, Object> params = new HashMap<>();


        if (request.getActive() != null) {
            sql.append(" and r.active = :active");
            params.put("active", request.getActive());
        }

        if (request.getName() != null) {
            sql.append(" and r.name like :name ");
            params.put("name", "%" + request.getName() + "%");
        }
        sql.append(" order by r.name asc ");

        Query countQuery = em.createQuery(sql.toString().replace("select r", "select count(r.id)"));

        Long pageIndex = request.getPageIndex();
        Long pageSize = request.getPageSize();

        TypedQuery<Role> roleTypedQuery = em.createQuery(sql.toString(), Role.class);

        // Set param to query
        params.forEach((k, v) -> {
            roleTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        roleTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        roleTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Role> roleList = roleTypedQuery.getResultList();

        Long totalRoles = (Long) countQuery.getSingleResult();
        long totalPage = totalRoles / pageSize;
        if (totalRoles % pageSize != 0) {
            totalPage++;
        }

        List<RoleDTOResponse> roleDTOResponses = roleList
                .stream()
                .map(role -> RoleMapper.toRoleDTOResponse(role, userRepository
                                .findUsernameByUserId(role.getUserId()))
                )
                .toList();

        return PagingRoleDTOResponse.builder()
                .totalElement(totalRoles)
                .totalPage(totalPage)
                .data(roleDTOResponses)
                .build();
    }
}
