package com.capstone.backend.service.impl;


import com.capstone.backend.entity.Class;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTORequest;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.model.mapper.ClassMapper;
import com.capstone.backend.repository.ClassRepository;
import com.capstone.backend.repository.criteria.ClassesCriteria;
import com.capstone.backend.service.ClassService;
import com.capstone.backend.utils.UserHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClassServiceImpl implements ClassService {
    ClassRepository classRepository;
    ClassesCriteria classCriteria;
    UserHelper userHelper;

    @Override
    public ClassDTOResponse createClass(ClassDTORequest request) {
        User userLogged = userHelper.getUserLogin();
        // add entity
        Class classEntity = Class.builder()
                .active(true)
                .createdAt(LocalDateTime.now())
                .name(request.getName())
                .userId(userLogged.getId())
                .build();

        classEntity = classRepository.save(classEntity);
        return ClassMapper.toClassDTOResponse(classEntity);
    }

    @Override
    public ClassDTOResponse updateClass(Long id, ClassDTORequest request) {
        //  current user is login by authentication.getPrincipal()
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userLogged = (User) authentication.getPrincipal();

        // find Class id
        Class classObject = classRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));

        // update class
        classObject.setName(request.getName());
        classObject.setUserId(userLogged.getId());


        classObject = classRepository.save(classObject);
        return ClassMapper.toClassDTOResponse(classObject);
    }

    @Override
    public void deleteClass(Long id) {
        // find Class id
        Class classObject = classRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));
        //change active to false
        classObject.setActive(false);
        classRepository.save(classObject);
    }


    @Override
    public ClassDTOResponse viewClassById(Long id) {
        Class classObject = classRepository
                .findByIdAndActiveTrue(id)
                .orElseThrow(() -> ApiException.notFoundException("Class is not found"));

        return ClassMapper.toClassDTOResponse(classObject);
    }

    @Override
    public PagingDTOResponse searchClass(ClassDTOFilter classDTOFilter) {
        return classCriteria.searchClass(classDTOFilter);
    }

    @Override
    public List<ClassDTOResponse> getListClasses() {
        return classRepository.findClassByActiveIsTrue()
                .stream().map(ClassMapper::toClassDTOResponse)
                .toList();
    }


}
