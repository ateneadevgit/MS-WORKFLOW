package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBaseRequest {

    private UserData userData;
    private Integer workflowObjectId;
    private String workflowType;
    private String createdBy;
    private Integer roleId;

}
