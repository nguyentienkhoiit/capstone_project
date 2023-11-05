package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOFilter;
import com.capstone.backend.model.dto.bookvolume.BookVolumeDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTOFilter;
import com.capstone.backend.model.dto.chapter.ChapterDTOResponse;
import com.capstone.backend.model.dto.chapter.ChapterDTORequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChapterService {
    public ChapterDTOResponse createChapter(ChapterDTORequest request);

    public ChapterDTOResponse updateChapter(Long id, ChapterDTORequest request);

    public void deleteChapter(Long id);

    public PagingDTOResponse searchChapter(ChapterDTOFilter chapterDTOFilter);

    public ChapterDTOResponse viewChapterById(Long id);
}
