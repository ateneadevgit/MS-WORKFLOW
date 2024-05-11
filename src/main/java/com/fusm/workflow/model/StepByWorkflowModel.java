package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepByWorkflowModel {

    private Integer stepId;
    private String name;
    private Boolean isRequired;
    private Boolean hasSummary;
    private Integer orderStep;
    private Boolean enabled;
    private String minimumRequired;
    private Integer minimumRequiredId;

}
