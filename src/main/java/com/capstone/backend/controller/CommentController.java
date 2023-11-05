package com.capstone.backend.controller;

import com.capstone.backend.model.dto.comment.CommentDTORequest;
import com.capstone.backend.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.capstone.backend.utils.Constants.API_VERSION;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_VERSION + "/comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Comment", description = "API for Comment")
@CrossOrigin
public class CommentController {
    CommentService commentService;

    @PostMapping
    @Operation(summary = "Create comment or reply comment(has comment root id)")
    public ResponseEntity<?> createComment(@Valid @RequestBody CommentDTORequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @GetMapping("/{id}/more")
    @Operation(summary = "See more reply of a comment")
    public ResponseEntity<?> seeMoreReplyComment(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(commentService.seeMoreReplyComment(id));
    }
}
