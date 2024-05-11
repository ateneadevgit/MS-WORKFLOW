package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEvaluationRequest {

    private List<Integer> attachment;
    private Integer workflowId;
    private Integer stepId;
    private String createdBy;
    private Integer roleId;

}
