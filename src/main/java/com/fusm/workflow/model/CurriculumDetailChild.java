package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumDetailChild {

    private Integer curriculumId;
    private String name;
    private Integer type;
    private Integer creditNumber;
    private double participation;

}
