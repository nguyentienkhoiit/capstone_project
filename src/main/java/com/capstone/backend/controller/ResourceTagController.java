package com.capstone.backend.controller;

import com.capstone.backend.model.dto.resourcetag.ResourceTagDTOResponse;
import com.capstone.backend.model.dto.tag.TagDTOResponse;
import com.capstone.backend.service.ResourceTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(API_VERSION + "/resource-tags")
@Tag(name = "Resource-tag", description = "API for Resource's tags")
@CrossOrigin
public class ResourceTagController {
    ResourceTagService resourceTagService;

    @Operation(summary = "Get all tags of a resource-object table by table name and detail id")
    @GetMapping("/{tableType}/{detailId}")
    public List<ResourceTagDTOResponse> getAllResourceTagByTableTypeAndID(@PathVariable String tableType, @PathVariable long detailId) {
        return resourceTagService.getAllResourceTagByTableTypeAndID(tableType, detailId);
    }

    //Function of moderator and teacher
    //First: call GET api/v1/tags/id -> receive TagDTOResponse
    //dung tra ve mot object class/subject bao gom id
    @Operation(summary = "Apply tag to a resource object")
    @PostMapping("/{tableType}/{detailId}") //chuyen ve request body
    public ResourceTagDTOResponse applyTag(@RequestBody TagDTOResponse tagDTOResponse, @PathVariable String tableType, @PathVariable long detailId) {
        return resourceTagService.applyTagToResource(tagDTOResponse, tableType, detailId);
    }

    @Operation(summary = "Disable a tag from resource")
    @PutMapping("/{id}")
    public ResourceTagDTOResponse disableTag(@PathVariable long id) {
        return resourceTagService.disableTagFromResource(id);
    }
}
