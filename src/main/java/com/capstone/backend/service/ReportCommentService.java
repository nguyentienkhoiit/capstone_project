package com.capstone.backend.service;

import com.capstone.backend.model.dto.reportcomment.ReportCommentDTORequest;

public interface ReportCommentService {
    public Boolean createReportComment(ReportCommentDTORequest request);
}
