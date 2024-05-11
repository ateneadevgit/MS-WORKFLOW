package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewListModel {

    private Integer workflowId;
    private Integer stepId;
    private Integer roleId;
    private String createdBy;

}
