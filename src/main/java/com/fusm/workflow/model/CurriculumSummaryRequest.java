package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumSummaryRequest {

    private String summary;
    private Integer curriculumType;
    private Integer roleId;
    private String createdBy;
    private Integer stepId;
    private Integer workflowId;

}
