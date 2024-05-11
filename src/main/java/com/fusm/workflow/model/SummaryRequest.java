package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryRequest {

    private Integer stepId;
    private Integer workflowId;
    private String summary;
    private Integer roleId;
    private String createdBy;

}
