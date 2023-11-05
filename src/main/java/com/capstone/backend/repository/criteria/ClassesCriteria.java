package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.Class;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.mapper.ClassMapper;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClassesCriteria {
    final EntityManager em;

    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Class t where t.active = TRUE");
        Map<String, Object> params = new HashMap<>();

        sql.append(" and t.name like :name ");
        params.put("name", "%" + classDTOFilter.getName() + "%");

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = classDTOFilter.getPageIndex();
        Long pageSize = classDTOFilter.getPageSize();

        TypedQuery<Class> classTypedQuery = em.createQuery(sql.toString(), Class.class);

        // Set param to query
        params.forEach((k, v) -> {
            classTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        classTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        classTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Class> tagList = classTypedQuery.getResultList();

        Long totalClass = (Long) countQuery.getSingleResult();
        Long totalPage = totalClass / pageSize;
        if (totalClass % pageSize != 0) {
            totalPage++;
        }

        List<ClassDTOResponse> classDTOResponseList = tagList.stream().map(ClassMapper::toClassDTOResponse).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalClass)
                .totalPage(totalPage)
                .data(classDTOResponseList)
                .build();
    }
}
