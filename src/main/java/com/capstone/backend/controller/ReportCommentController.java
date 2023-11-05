package com.capstone.backend.controller;

import com.capstone.backend.model.dto.reportcomment.ReportCommentDTORequest;
import com.capstone.backend.service.ReportCommentService;
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
@RequestMapping(API_VERSION + "/report-comment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Report comment", description = "API for Report Comment")
@CrossOrigin
public class ReportCommentController {
    ReportCommentService reportCommentService;

    @PostMapping
    @Operation(summary = "Create report a comment")
    public ResponseEntity<?> createReportComment(@Valid @RequestBody ReportCommentDTORequest request) {
        return ResponseEntity.ok(reportCommentService.createReportComment(request));
    }
}
