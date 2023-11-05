package com.capstone.backend.model.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDTORequest {
    @Schema(example = "1", description = "UserId must be integer")
    @NotNull(message = "{userId.not-null}")
    Long userId;
    @Schema(example = "1", description = "ResourceId must be integer")
    @NotNull(message = "{resourceId.not-null}")
    Long resourceId;
    @Schema(example = "1", description = "CommentRootId must be integer")
    Long commentRootId;
    @NotBlank(message = "{content.not-bank}")
    String content;
}
