package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.bookseries.BookSeriesDTOFilter;
import com.capstone.backend.model.dto.lesson.LessonDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubjectService {
    public SubjectDTOResponse createSubject(SubjectDTORequest request);


    public SubjectDTOResponse updateSubject(Long id, SubjectDTORequest request);

    public void deleteSubject(Long id);

    public PagingDTOResponse searchSubject(SubjectDTOFilter subjectDTOFilter);

    public SubjectDTOResponse viewSubjectById(Long id);
}
