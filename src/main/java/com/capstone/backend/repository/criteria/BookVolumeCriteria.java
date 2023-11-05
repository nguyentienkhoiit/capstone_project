package com.capstone.backend.repository.criteria;


import com.capstone.backend.entity.BookVolume;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.mapper.BookVolumeMapper;
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
public class BookVolumeCriteria {
    final EntityManager em;

    public PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from BookVolume t where t.active = TRUE");
        Map<String, Object> params = new HashMap<>();

        sql.append(" and t.name like :name ");
        params.put("name", "%" + bookVolumeDTOFilter.getName() + "%");

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = bookVolumeDTOFilter.getPageIndex();
        Long pageSize = bookVolumeDTOFilter.getPageSize();

        TypedQuery<BookVolume> bookVolumeTypedQuery = em.createQuery(sql.toString(), BookVolume.class);

        // Set param to query
        params.forEach((k, v) -> {
            bookVolumeTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        bookVolumeTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        bookVolumeTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<BookVolume> bookVolumeList = bookVolumeTypedQuery.getResultList();

        Long totalBookVolume = (Long) countQuery.getSingleResult();
        Long totalPage = totalBookVolume / pageSize;
        if (totalPage % pageSize != 0) {
            totalPage++;
        }

        List<BookVolumeDTOResponse> bookVolumeDTOResponseList = bookVolumeList.stream().map(BookVolumeMapper::toBookVolumeDTOResponse).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalBookVolume)
                .totalPage(totalPage)
                .data(bookVolumeDTOResponseList)
                .build();
    }
}
