package com.capstone.backend.controller;


import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.classes.ClassDTOFilter;
import com.capstone.backend.model.dto.classes.ClassDTORequest;
import com.capstone.backend.model.dto.classes.ClassDTOResponse;
import com.capstone.backend.service.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/class")
@Tag(name = "Class", description = "API for Class")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class ClassController {


    ClassService classService;

    @Operation(summary = "create Class")
    @PostMapping("")
    public ResponseEntity<ClassDTOResponse> create(@Valid @RequestBody ClassDTORequest request) {
        ClassDTOResponse response = classService.createClass(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update Class")
    @PutMapping("/{id}")
    public ResponseEntity<ClassDTOResponse> update(@Valid @RequestBody ClassDTORequest request,
                                                   @PathVariable @NotEmpty Long id) {
        ClassDTOResponse classDTOResponse = classService.updateClass(id, request);
        return ResponseEntity.ok(classDTOResponse);
    }

    @Operation(summary = "Delete Class")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable @NotEmpty Long id) {
        classService.deleteClass(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Class")
    @GetMapping("/display")
    public PagingDTOResponse searchClass(@ModelAttribute ClassDTOFilter classDTOFilter) {
        return classService.searchClass(classDTOFilter);
    }

    @Operation(summary = "View Class by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ClassDTOResponse> viewClass(@PathVariable @NotEmpty Long id) {
        ClassDTOResponse classDTOResponse = classService.viewClassById(id);
        return ResponseEntity.ok(classDTOResponse);
    }


}
