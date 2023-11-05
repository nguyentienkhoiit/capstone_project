package com.capstone.backend.controller;

import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.materials.MaterialFilterProtectDTORequest;
import com.capstone.backend.model.dto.materials.MaterialsFilterDTORequest;
import com.capstone.backend.model.dto.resource.*;
import com.capstone.backend.model.dto.tag.TagSuggestDTORequest;
import com.capstone.backend.model.mapper.MaterialsMapper;
import com.capstone.backend.service.FileService;
import com.capstone.backend.service.ResourceService;
import com.capstone.backend.utils.DataHelper;
import com.capstone.backend.utils.MessageException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static com.capstone.backend.utils.Constants.API_VERSION;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/resource")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Resource", description = "API for Resource")
@CrossOrigin
public class ResourceController {
    ResourceService resourceService;
    MessageException messageException;

    @GetMapping("/show-class-to-lesson")
    public ResponseEntity<?> getFromClassToLesson(@ModelAttribute ClassLessonId classLessonId) {
        return ResponseEntity.ok(resourceService.getFromClassToLesson(classLessonId));
    }

    //check permission
    @GetMapping("/get-link/{filename}")
    public ResponseEntity<?> getLinkResource(@PathVariable String filename) {
        try {
            Resource resource = resourceService.getResourceIOByFilename(filename);
            String contentType = DataHelper
                    .getContentType(DataHelper.extractFileExtension(filename));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.add(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=" + resource.getFilename()
            );
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (Exception e) {
            throw ApiException.notFoundException(messageException.MSG_FILE_NOT_FOUND);
        }
    }

    //checked permission
    @GetMapping("/detail/{id}")
    @Operation(summary = "See a detail resource (comment, resource, like, unlike, resource relate ...)")
    public ResponseEntity<?> getResourceDetailById(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(resourceService.getResourceDetailById(id));
    }

    //checked permission
    @GetMapping("/materials")
    public ResponseEntity<?> searchMaterials(
            @Valid @ModelAttribute MaterialsFilterDTORequest request
    ) {
        return ResponseEntity.ok(resourceService.searchMaterials(request));
    }

    @Operation(summary = "Tag for search media")
    @PostMapping("/tags")
    public ResponseEntity<?> getListTagsSuggest(@RequestBody TagSuggestDTORequest request) {
        return ResponseEntity.ok(resourceService.getListTagsSuggest(request));
    }

    @Operation(summary = "Tag for add resource")
    @PostMapping("/tags-global")
    public ResponseEntity<?> getListTagsGlobalSuggest(@RequestBody TagSuggestDTORequest request) {
        return ResponseEntity.ok(resourceService.getListTagsGlobalSuggest(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewResource(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.viewResource(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateResource(
            @PathVariable Long id,
            @ModelAttribute ResourceDTOUpdateRequest request,
            @RequestParam("file") MultipartFile[] file
    ) {
        request.setId(id);
        return ResponseEntity.ok(resourceService.updateResource(request, file));
    }

    //check permission resource
    @GetMapping("/medias")
    public ResponseEntity<?> searchMediaResource(
            @Valid @ModelAttribute ResourceMediaDTOFilter resourceDTOFilter
    ) {
        return ResponseEntity.ok(resourceService.searchMediaResource(resourceDTOFilter));
    }

    @GetMapping("/download/{filename:.+}")
    @Operation(summary = "Download a resource")
    public ResponseEntity<?> downloadResource(@PathVariable(name = "filename", required = true) String fileName) {
        Resource file = resourceService.downloadResource(fileName);
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\""
                )
                .body(file);
    }

    //check permission resource
    @GetMapping("/share/{resourceId}")
    public ResponseEntity<?> viewResourceShareById(@PathVariable Long resourceId) {
        return ResponseEntity.ok(resourceService.viewResourceShareById(resourceId));
    }

    //check permission resource
    @PostMapping("/share/suggest")
    public ResponseEntity<?> suggestionUserShare(@RequestBody UserShareSuggestDTORequest request) {
        return ResponseEntity.ok(resourceService.suggestionUserShare(request.getText()));
    }

    //check permission resource
    @PostMapping("/share/{resourceId}")
    public ResponseEntity<?> shareResource(
            @RequestBody ResourceSharedDTORequest request,
            @PathVariable Long resourceId
    ) {
        request.setResourceId(resourceId);
        return ResponseEntity.ok(resourceService.shareResource(request));
    }

}
