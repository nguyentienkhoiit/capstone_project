package com.capstone.backend.model.dto.reportcomment;

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
public class ReportCommentDTORequest {
    @NotBlank(message = "{message.not-blank}")
    String message;
    @Schema(example = "1", description = "ReportId must be integer")
    @NotNull(message = "{reporterId.not-null}")
    Long reporterId;
    @Schema(example = "1", description = "CommenterId must be integer")
    @NotNull(message = "{commentId.not-null}")
    Long commentId;
}
