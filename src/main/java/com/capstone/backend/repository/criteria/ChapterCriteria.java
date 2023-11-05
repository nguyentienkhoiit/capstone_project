package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.Chapter;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.mapper.ChapterMapper;
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
public class ChapterCriteria {

    final EntityManager em;

    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from Chapter t where t.active = TRUE");
        Map<String, Object> params = new HashMap<>();

        sql.append(" and t.name like :name ");
        params.put("name", "%" + chapterDTOFilter.getName() + "%");

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = chapterDTOFilter.getPageIndex();
        Long pageSize = chapterDTOFilter.getPageSize();

        TypedQuery<Chapter> chapterTypedQuery = em.createQuery(sql.toString(), Chapter.class);

        // Set param to query
        params.forEach((k, v) -> {
            chapterTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        chapterTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        chapterTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<Chapter> chapterList = chapterTypedQuery.getResultList();

        Long totalChapter = (Long) countQuery.getSingleResult();
        Long totalPage = totalChapter / pageSize;
        if (totalChapter % pageSize != 0) {
            totalPage++;
        }

        List<ChapterDTOResponse> chapterDTOResponseList = chapterList.stream().map(ChapterMapper::toChapterDTOResponse).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalChapter)
                .totalPage(totalPage)
                .data(chapterDTOResponseList)
                .build();
    }
}
