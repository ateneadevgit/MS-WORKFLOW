package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepAttachRequest {

    private List<AttachRequest> attachments;
    private Integer workflowId;
    private Integer stepId;
    private Integer roleId;
    private String createdBy;

}
