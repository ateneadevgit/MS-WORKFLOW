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
public class StepByIdModel {

    private String name;
    private Integer stepOrder;
    private Boolean isPrerrequeriment;
    private Boolean hasSummary;
    private List<StepRoleActionRequest> roleActions;
    private String minimumRequired;

}
