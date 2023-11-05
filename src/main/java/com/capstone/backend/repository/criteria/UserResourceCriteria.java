package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.entity.UserResourcePermission;
import com.capstone.backend.entity.type.ActionType;
import com.capstone.backend.entity.type.ApproveType;
import com.capstone.backend.entity.type.VisualType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.userresource.MyUserResourceDTOFilter;
import com.capstone.backend.model.dto.userresource.PagingUserResourceDTOResponse;
import com.capstone.backend.model.dto.userresource.UserResourceDTOResponse;
import com.capstone.backend.model.dto.userresource.UserResourceSavedOrSharedDTOFilter;
import com.capstone.backend.model.mapper.UserResourceMapper;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.repository.UserResourcePermissionRepository;
import com.capstone.backend.repository.UserResourceRepository;
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

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResourceCriteria {
    EntityManager em;
    UserRepository userRepository;
    UserHelper userHelper;
    UserResourcePermissionRepository userResourcePermissionRepository;
    MessageException messageException;

    public PagingUserResourceDTOResponse viewSearchUserSavedResource(UserResourceSavedOrSharedDTOFilter request) {
        StringBuilder sql = new StringBuilder("select ur.resource from UserResource ur where ur.resource.active = TRUE ");

        Map<String, Object> params = new HashMap<>();

        if (request.getTabResourceType() != null) {
            sql.append(" and ur.resource.tabResourceType = :tabResourceType ");
            params.put("tabResourceType", request.getTabResourceType());
        }

        if (request.getResourceName() != null) {
            sql.append(" and ur.resource.name like :name ");
            params.put("name", "%" + request.getResourceName() + "%");
        }

        sql.append(" and ur.actionType = :actionType and ur.resource.visualType = :visualType " +
                "and ur.resource.approveType = :approveType");
        params.put("actionType", ActionType.SAVED);
        params.put("visualType", VisualType.PUBLIC);
        params.put("approveType", ApproveType.ACCEPTED);

        sql.append(" order by ur.resource.createdAt asc ");

        Query countQuery = em
                .createQuery(sql.toString().replace("select ur.resource", "select count(ur.resource.id)"));

        Long pageIndex = request.getPageIndex();
        Long pageSize = request.getPageSize();

        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);

        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resources = resourceTypedQuery.getResultList();

        Long totalResource = (Long) countQuery.getSingleResult();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

        List<UserResourceDTOResponse> userResourceDTOResponses = resources.stream()
                .map(resource -> UserResourceMapper.toUserResourceDTOResponse(
                        resource,
                        userRepository.findUsernameByUserId(resource.getAuthor().getId()))
                )
                .toList();

        return PagingUserResourceDTOResponse.builder()
                .totalElement(totalResource)
                .totalPage(totalPage)
                .data(userResourceDTOResponses)
                .build();
    }

    public PagingUserResourceDTOResponse viewSearchUserResourceShared(UserResourceSavedOrSharedDTOFilter request) {
        Map<String, Object> params = new HashMap<>();
        User user = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder("select urp.resource from UserResourcePermission urp where " +
                "urp.user.id = :userId and urp.resource.active = TRUE");
        params.put("userId", user.getId());


        if (request.getTabResourceType() != null) {
            sql.append(" urp.resource.tabResourceType = :tabResourceType ");
            params.put("tabResourceType", request.getTabResourceType());
        }

        if (request.getResourceName() != null) {
            sql.append(" and urp.resource.name like :name ");
            params.put("name", "%" + request.getResourceName() + "%");
        }

        sql.append(" and urp.resource.visualType != :visualType and urp.resource.approveType = :approveType and urp.permission like 'DV' ");
        params.put("visualType", VisualType.PRIVATE);
        params.put("approveType", ApproveType.ACCEPTED);

        sql.append(" order by urp.resource.createdAt asc ");

        Query countQuery = em
                .createQuery(sql.toString().replace("select urp.resource", "select count(urp.resource.id)"));

        Long pageIndex = request.getPageIndex();
        Long pageSize = request.getPageSize();

        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);

        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resources = resourceTypedQuery.getResultList();

        Long totalResource = (Long) countQuery.getSingleResult();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

        List<UserResourceDTOResponse> userResourceDTOResponses = resources.stream()
                .map(resource -> UserResourceMapper.toUserResourceDTOResponse(
                        resource,
                        userRepository.findUsernameByUserId(resource.getAuthor().getId()))
                )
                .toList();

        return PagingUserResourceDTOResponse.builder()
                .totalElement(totalResource)
                .totalPage(totalPage)
                .data(userResourceDTOResponses)
                .build();
    }

    public PagingUserResourceDTOResponse viewSearchMyUserResource(MyUserResourceDTOFilter request) {
        Map<String, Object> params = new HashMap<>();
        User user = userHelper.getUserLogin();
        StringBuilder sql = new StringBuilder("select r from Resource r where r.active = TRUE and r.author.id = :userId");
        params.put("userId", user.getId());

        if (request.getTabResourceType() != null) {
            sql.append(" and r.tabResourceType = :tabResourceType ");
            params.put("tabResourceType", request.getTabResourceType());
        }

        if (request.getResourceName() != null) {
            sql.append(" and r.name like :name ");
            params.put("name", "%" + request.getResourceName() + "%");
        }

        if (request.getApproveType() != null) {
            sql.append(" and r.approveType = :approveType ");
            params.put("approveType", request.getApproveType());
        }

        if (request.getVisualType() != null) {
            sql.append(" and r.visualType = :visualType ");
            params.put("visualType", request.getVisualType());
        }

        sql.append(" order by r.createdAt asc ");

        Query countQuery = em.createQuery(sql.toString().replace("select r", "select count(r.id)"));

        Long pageIndex = request.getPageIndex();
        Long pageSize = request.getPageSize();

        TypedQuery<Resource> resourceTypedQuery = em.createQuery(sql.toString(), Resource.class);

        params.forEach((k, v) -> {
            resourceTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        resourceTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        resourceTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Resource> resources = resourceTypedQuery.getResultList();

        Long totalResource = (Long) countQuery.getSingleResult();
        long totalPage = totalResource / pageSize;
        if (totalResource % pageSize != 0) {
            totalPage++;
        }

        List<UserResourceDTOResponse> userResourceDTOResponses = resources.stream()
                .map(resource -> {
                            UserResourceDTOResponse userResourceDTOResponse = UserResourceMapper
                                    .toUserResourceDTOResponse(
                                            resource,
                                            userRepository.findUsernameByUserId(resource.getAuthor().getId()));
                            UserResourcePermission permission = userResourcePermissionRepository
                                    .findByUserAndResource(user, resource)
                                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_RESOURCE_NOT_FOUND));

                            userResourceDTOResponse.setIsUpdate(permission.getPermission().contains("U"));

                            userResourceDTOResponse.setIsShare(resource.getApproveType() == ApproveType.ACCEPTED);

                            userResourceDTOResponse.setIsDelete(true);
                            return userResourceDTOResponse;
                        }
                )
                .toList();

        return PagingUserResourceDTOResponse.builder()
                .totalElement(totalResource)
                .totalPage(totalPage)
                .data(userResourceDTOResponses)
                .build();
    }
}
