package com.capstone.backend.service;

import com.capstone.backend.model.dto.reportresource.ReportResourceDTORequest;
import com.capstone.backend.model.dto.userresource.PagingUserResourceDTOResponse;
import com.capstone.backend.model.dto.userresource.ReportResourceDTOFilter;

public interface ReportResourceService {

    public Boolean createReportComment(ReportResourceDTORequest request);

    public PagingUserResourceDTOResponse viewSearchMyReportResource(ReportResourceDTOFilter request);
}
