package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumListModel {

    private Integer curriculumId;
    private String name;
    private String code;
    private Integer creditsNumber;
    private Boolean hasSyllabus;

}
