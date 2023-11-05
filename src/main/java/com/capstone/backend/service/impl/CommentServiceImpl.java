package com.capstone.backend.service.impl;

import com.capstone.backend.entity.Comment;
import com.capstone.backend.entity.Resource;
import com.capstone.backend.entity.User;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.comment.CommentDTORequest;
import com.capstone.backend.model.dto.comment.CommentDTOResponse;
import com.capstone.backend.model.dto.comment.CommentDetailDTOResponse;
import com.capstone.backend.model.mapper.CommentMapper;
import com.capstone.backend.repository.CommentRepository;
import com.capstone.backend.repository.ResourceRepository;
import com.capstone.backend.repository.UserRepository;
import com.capstone.backend.service.CommentService;
import com.capstone.backend.utils.FileHelper;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {
    CommentRepository commentRepository;
    UserRepository userRepository;
    ResourceRepository resourceRepository;
    MessageException messageException;

    @Override
    public CommentDTOResponse createComment(CommentDTORequest request) {
        User commenter = userRepository.findById(request.getUserId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_USER_NOT_FOUND));

        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_RESOURCE_NOT_FOUND));

        Comment commentRoot = null;
        if (request.getCommentRootId() != null) {
            commentRoot = commentRepository.findById(request.getCommentRootId())
                    .orElseThrow(() -> ApiException.notFoundException(messageException.MSG_COMMENT_ROOT_ID_NOT_FOUND));
        }

        if (FileHelper.checkContentInputValid(request.getContent()))
            throw ApiException.badRequestException(messageException.MSG_TEXT_NO_STANDARD_WORD);

        Comment comment = Comment.builder()
                .commenter(commenter)
                .resource(resource)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .active(true)
                .commentRoot(commentRoot)
                .build();
        comment = commentRepository.save(comment);

        return CommentMapper.toCommentDTOResponse(comment);
    }

    @Override
    public List<CommentDetailDTOResponse> getListCommentDetailDTOResponse(Long resourceId) {
        List<Comment> listCommentRoot = commentRepository.findByResourceIdAndCommentRootIdIsNull(resourceId);
        return listCommentRoot.stream()
                .map(comment -> {
                    CommentDTOResponse commentDTOResponse = CommentMapper.toCommentDTOResponse(comment);
                    Long countReplyComments = commentRepository.countReplyComments(commentDTOResponse.getCommentId(), resourceId);
                    return CommentDetailDTOResponse.builder()
                            .commentDTOResponse(commentDTOResponse)
                            .numberOfReplyComments(countReplyComments)
                            .build();
                }).toList();
    }

    @Override
    public List<CommentDTOResponse> seeMoreReplyComment(Long id) {
        return commentRepository
                .findAllCommentReply(id).stream()
                .map(CommentMapper::toCommentDTOResponse)
                .toList();
    }
}
