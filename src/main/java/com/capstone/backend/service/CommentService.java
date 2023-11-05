package com.capstone.backend.service;

import com.capstone.backend.model.dto.comment.CommentDTORequest;
import com.capstone.backend.model.dto.comment.CommentDTOResponse;
import com.capstone.backend.model.dto.comment.CommentDetailDTOResponse;

import java.util.List;

public interface CommentService {
    public CommentDTOResponse createComment(CommentDTORequest request);

    public List<CommentDetailDTOResponse> getListCommentDetailDTOResponse(Long resourceId);

    public List<CommentDTOResponse> seeMoreReplyComment(Long id);
}
