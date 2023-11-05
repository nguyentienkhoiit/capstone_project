package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.Subject;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.capstone.backend.model.mapper.SubjectMapper;
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
public class SubjectCriteria {
    final EntityManager em;

    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Subject t where t.active = TRUE");
        Map<String, Object> params = new HashMap<>();

        sql.append(" and t.name like :name ");
        params.put("name", "%" + subjectDTOFilter.getName() + "%");

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = subjectDTOFilter.getPageIndex();
        Long pageSize = subjectDTOFilter.getPageSize();

        TypedQuery<Subject> subjectTypedQuery = em.createQuery(sql.toString(), Subject.class);

        // Set param to query
        params.forEach((k, v) -> {
            subjectTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        subjectTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        subjectTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Subject> tagList = subjectTypedQuery.getResultList();

        Long totalSubject = (Long) countQuery.getSingleResult();
        Long totalPage = totalSubject / pageSize;
        if (totalSubject % pageSize != 0) {
            totalPage++;
        }

        List<SubjectDTOResponse> subjectDTOResponseList = tagList.stream().map(SubjectMapper::toSubjectDTOResponse).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalSubject)
                .totalPage(totalPage)
                .data(subjectDTOResponseList)
                .build();
    }
}
