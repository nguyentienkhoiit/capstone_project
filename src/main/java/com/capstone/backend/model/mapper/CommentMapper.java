package com.capstone.backend.model.mapper;

import com.capstone.backend.entity.Comment;
import com.capstone.backend.model.dto.comment.CommentDTOResponse;

public class CommentMapper {
    public static CommentDTOResponse toCommentDTOResponse(Comment comment) {
        Long commentIdRoot = null;
        if (comment.getCommentRoot() != null) {
            commentIdRoot = comment.getCommentRoot().getCommentId();
        }
        return CommentDTOResponse.builder()
                .commentId(comment.getCommentId())
                .commenterDTOResponse(UserMapper.toUserDTOResponse(comment.getCommenter()))
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .active(comment.getActive())
                .commentRootId(commentIdRoot)
                .build();
    }
}
