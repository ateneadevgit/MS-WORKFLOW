package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IntegrativeActivityRequest {

    private Integer activityId;
    private String tittle;
    private String description;
    private String activity;
    private String createdBy;

}
