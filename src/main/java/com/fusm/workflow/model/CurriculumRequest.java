package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumRequest {

    private Integer curriculumId;
    private String name;
    private Integer type;
    private Integer numberCredits;
    private String description;
    private Integer fatherId;
    private String raeg;
    private SubjectRequest subjectRequest;

}
