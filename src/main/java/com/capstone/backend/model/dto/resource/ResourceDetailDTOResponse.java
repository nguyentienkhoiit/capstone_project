package com.capstone.backend.model.dto.resource;

import com.capstone.backend.entity.User;
import com.capstone.backend.model.dto.comment.CommentDetailDTOResponse;
import com.capstone.backend.model.dto.user.UserDTOResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceDetailDTOResponse {
    Boolean isLike;
    Boolean isUnlike;
    Long numberOfLike;
    Long numberOfUnlike;
    Boolean isSave;
    List<String> listTagRelate;
    ResourceDTOResponse resourceDTOResponse;
    List<CommentDetailDTOResponse> commentDetailDTOResponses;
    List<ResourceViewDTOResponse> listResourceRelates;
    UserDTOResponse owner;
}
