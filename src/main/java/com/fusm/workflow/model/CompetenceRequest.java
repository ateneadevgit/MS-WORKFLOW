package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetenceRequest {

    private Integer competenceId;
    private Integer categoryId;
    private String code;
    private String description;
    private String createdBy;
    private Integer roleId;

}
