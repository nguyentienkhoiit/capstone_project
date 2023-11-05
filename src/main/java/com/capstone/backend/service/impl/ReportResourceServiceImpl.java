package com.capstone.backend.service.impl;

import com.capstone.backend.entity.ReportResource;
import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.reportresource.ReportResourceDTORequest;
import com.capstone.backend.repository.ReportResourceRepository;
import com.capstone.backend.repository.ResourceRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.ReportResourceService;
import com.capstone.backend.utils.FileHelper;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportResourceServiceImpl implements ReportResourceService {
    UserRepository userRepository;
    ResourceRepository resourceRepository;
    ReportResourceRepository reportResourceRepository;
    MessageException messageException;

    @Override
    public Boolean createReportComment(ReportResourceDTORequest request) {
        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        if (FileHelper.checkContentInputValid(request.getMessage()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);

        ReportResource reportResource = ReportResource.builder()
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .active(true)
                .reporter(reporter)
                .resource(resource)
                .build();

        ReportResource reportResourceExist = reportResourceRepository
                .findByReporterIdActive(request.getReporterId())
                .orElse(null);
        if (reportResourceExist == null)
            reportResource = reportResourceRepository.save(reportResource);
        else throw ApiException.badRequestException(messageException.MSG_REPORT_RESOURCE_REPORTED);
        return true;
    }
}
