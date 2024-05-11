package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateTraceability {

    private String feedbackStatus;
    private FileModel fileFeedback;
    private Integer roleId;
    private String createdBy;

}
