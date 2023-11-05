package com.capstone.backend.controller;

import com.capstone.backend.model.dto.PagingDTOResponse;
import com.capstone.backend.model.dto.subject.SubjectDTOFilter;
import com.capstone.backend.model.dto.subject.SubjectDTORequest;
import com.capstone.backend.model.dto.subject.SubjectDTOResponse;
import com.capstone.backend.service.SubjectService;
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
@RequestMapping(API_VERSION + "/subject")
@Tag(name = "Subject", description = "API for Subject")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class SubjectController {

    SubjectService subjectService;

    @Operation(summary = "create Subject")
    @PostMapping("")
    public ResponseEntity<SubjectDTOResponse> create(@Valid @RequestBody SubjectDTORequest request) {
        SubjectDTOResponse response = subjectService.createSubject(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update subject")
    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTOResponse> update(@Valid @RequestBody SubjectDTORequest request,
                                                        @PathVariable @NotEmpty Long id) {
        SubjectDTOResponse subjectDTOResponse = subjectService.updateSubject(id, request);
        return ResponseEntity.ok(subjectDTOResponse);
    }

    @Operation(summary = "Delete Subject")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable @NotEmpty Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Search Subject")
    @GetMapping("/display")
    public PagingDTOResponse searchSubject(@ModelAttribute SubjectDTOFilter subjectDTOFilter) {
        return subjectService.searchSubject(subjectDTOFilter);
    }

    @Operation(summary = "View Subject by Id")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTOResponse> viewSubject(@PathVariable @NotEmpty Long id) {
       SubjectDTOResponse subjectDTOResponse = subjectService.viewSubjectById(id);
        return ResponseEntity.ok(subjectDTOResponse);
    }

}
