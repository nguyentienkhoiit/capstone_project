package com.capstone.backend.repository;

import com.capstone.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.resource.id = :resourceId and c.commentRoot.commentId is null order by c.createdAt desc")
    public List<Comment> findByResourceIdAndCommentRootIdIsNull(Long resourceId);

    @Query("select count(c) from Comment c where c.commentRoot.commentId = :commentId and c.resource.id = :resourceId")
    public Long countReplyComments(Long commentId, Long resourceId);

    @Query("select c from Comment c where c.commentRoot.commentId = :id")
    public List<Comment> findAllCommentReply(Long id);
}
