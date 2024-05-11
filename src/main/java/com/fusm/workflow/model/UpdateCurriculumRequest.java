package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCurriculumRequest {

    private String name;
    private Integer numberCredits;
    private String description;
    private String raeg;
    private SubjectRequest subjectRequest;
    private String createdBy;
    private Integer roleId;
    private String rae;
    private String competences;

}
