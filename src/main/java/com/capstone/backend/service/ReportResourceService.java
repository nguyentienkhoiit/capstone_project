package com.capstone.backend.service;

import com.capstone.backend.model.dto.reportresource.ReportResourceDTORequest;

public interface ReportResourceService {

    public Boolean createReportComment(ReportResourceDTORequest request);
}
