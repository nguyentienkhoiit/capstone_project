package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Comment;
import com.capstone.backend.entity.ReportComment;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.reportcomment.ReportCommentDTORequest;
import com.capstone.backend.repository.CommentRepository;
import com.capstone.backend.repository.ReportCommentRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.ReportCommentService;
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
public class ReportCommentServiceImpl implements ReportCommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    ReportCommentRepository reportCommentRepository;
    MessageException messageException;

    @Override
    public Boolean createReportComment(ReportCommentDTORequest request) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_COMMENT_NOT_FOUND));
        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        if (FileHelper.checkContentInputValid(request.getMessage()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);


        ReportComment reportComment = ReportComment.builder()
                .message(request.getMessage())
                .createdAt(LocalDateTime.now())
                .active(true)
                .comment(comment)
                .reporter(reporter)
                .build();

        ReportComment reportCommentExist = reportCommentRepository
                .findByReporterIdActive(request.getReporterId())
                .orElse(null);

        if (reportCommentExist == null)
            reportComment = reportCommentRepository.save(reportComment);
        else throw ApiException.badRequestException(messageException.MSG_REPORT_COMMENT_REPORTED);
        return true;
    }
}
