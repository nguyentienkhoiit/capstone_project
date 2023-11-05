package com.capstone.backend.service;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTORequest;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassService {
    public ClassDTOResponse createClass(ClassDTORequest request);

    public ClassDTOResponse updateClass(Long id, ClassDTORequest request);

    public void deleteClass(Long id);


    public ClassDTOResponse viewClassById(Long id);

    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter);

    public List<ClassDTOResponse> getListClasses();
}
