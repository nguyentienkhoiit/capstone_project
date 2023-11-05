package com.capstone.backend.repository.criteria;

import com.capstone.backend.entity.BookSeries;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.mapper.BookSeriesMapper;
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
public class BookSeriesCriteria {
    final EntityManager em;

    public PagingDTOResponse searchBookSeries(BookSeriesDTOFilter bookSeriesDTOFilter) {
        StringBuilder sql = new StringBuilder("select t from BookSeries t where t.active = TRUE");
        Map<String, Object> params = new HashMap<>();

        sql.append(" and t.name like :name ");
        params.put("name", "%" + bookSeriesDTOFilter.getName() + "%");

        Query countQuery = em.createQuery(sql.toString().replace("select t", "select count(t.id)"));

        Long pageIndex = bookSeriesDTOFilter.getPageIndex();
        Long pageSize = bookSeriesDTOFilter.getPageSize();

        TypedQuery<BookSeries> bookSeriesTypedQuery = em.createQuery(sql.toString(), BookSeries.class);

        // Set param to query
        params.forEach((k, v) -> {
            bookSeriesTypedQuery.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        //paging
        bookSeriesTypedQuery.setFirstResult((int) ((pageIndex - 1) * pageSize));
        bookSeriesTypedQuery.setMaxResults(Math.toIntExact(pageSize));
        List<BookSeries> tagList = bookSeriesTypedQuery.getResultList();

        Long totalBookSeries = (Long) countQuery.getSingleResult();
        Long totalPage = totalBookSeries / pageSize;
        if (totalBookSeries % pageSize != 0) {
            totalPage++;
        }

        List<BookSeriesDTOResponse> bookSeriesDTOResponseList = tagList.stream().map(BookSeriesMapper::toBookseriesDTOResponse).toList();

        return PagingDTOResponse.builder()
                .totalElement(totalBookSeries)
                .totalPage(totalPage)
                .data(bookSeriesDTOResponseList)
                .build();
    }
}
