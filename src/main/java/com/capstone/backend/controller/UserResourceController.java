package com.capstone.backend.controller;

import com.capstone.backend.model.dto.resource.ResourceDTORequest;
import com.capstone.backend.model.dto.userresource.MyUserResourceDTOFilter;
import com.capstone.backend.model.dto.userresource.UserResourceRequest;
import com.capstone.backend.model.dto.userresource.UserResourceSavedOrSharedDTOFilter;
import com.capstone.backend.service.ResourceService;
import com.capstone.backend.service.UserResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/user-resource")
@Tag(name = "User Resource", description = "API for Action With Resource")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin
public class UserResourceController {
    UserResourceService userResourceService;
    ResourceService resourceService;

    @Operation(summary = "Upload multi resource")
    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadResource(
            @RequestParam("files") MultipartFile[] files,
            @ModelAttribute ResourceDTORequest request
    ) {
        return ResponseEntity.ok(resourceService.uploadResource(request, files));
    }

    @PostMapping
    @Operation(summary = "Action with resource (LIKE, UNLIKE, SAVED, UNSAVED;)")
    public ResponseEntity<?> actionResource(@Valid @RequestBody UserResourceRequest request) {
        return ResponseEntity.ok(userResourceService.actionResource(request));
    }

    @GetMapping("/display-shared")
    public ResponseEntity<?> viewSearchUserResourceShared(
            @ModelAttribute UserResourceSavedOrSharedDTOFilter request
    ) {
        return ResponseEntity.ok(userResourceService.viewSearchUserResourceShared(request));
    }

    @DeleteMapping("/display-shared/{id}")
    public ResponseEntity<?> deleteSharedResource(@PathVariable Long id) {
        return ResponseEntity.ok(userResourceService.deleteSharedResource(id));
    }

    @GetMapping("/display-saved")
    public ResponseEntity<?> viewSearchUserResourceSaved(
            @ModelAttribute UserResourceSavedOrSharedDTOFilter request
    ) {
        return ResponseEntity.ok(userResourceService.viewSearchUserResourceSaved(request));
    }

    @DeleteMapping("/display-saved/{id}")
    public ResponseEntity<?> deleteSavedResource(@PathVariable Long id) {
        return ResponseEntity.ok(userResourceService.deleteSavedResource(id));
    }

    @GetMapping("/display-my-resource")
    public ResponseEntity<?> viewSearchMyUserResource(
            @ModelAttribute MyUserResourceDTOFilter request
    ) {
        return ResponseEntity.ok(userResourceService.viewSearchMyUserResource(request));
    }


}
