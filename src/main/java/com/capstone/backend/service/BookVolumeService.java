package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTORequest;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookVolumeService {
    BookVolumeDTOResponse createBookVolume(BookVolumeDTORequest request);

    BookVolumeDTOResponse updateBookVolume(Long id, BookVolumeDTORequest request);

    void deleteBookVolume(Long id);

    PagingDTOResponse searchBookVolume(BookVolumeDTOFilter bookVolumeDTOFilter);


    BookVolumeDTOResponse viewBookVolumeById(Long id);
}
