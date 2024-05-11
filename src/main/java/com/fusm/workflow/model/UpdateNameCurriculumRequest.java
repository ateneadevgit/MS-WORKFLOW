package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UpdateNameCurriculumRequest {

    private String name;
    private String description;
    private Integer curriculumId;
    private Integer hoursInteractionTeacher;
    private Integer hourSelfWork;

}
