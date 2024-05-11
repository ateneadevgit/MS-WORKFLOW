package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoreAndSubcoreModel {

    private Integer curriculumId;
    private String name;
    private Integer type;
    private Integer creditNumber;
    private Double participation;
    private String code;
    private String description;
    private Integer hoursInteractionTeacher;
    private Integer hourSelfWork;
    private String raeg;
    private Integer fatherId;
    private Boolean isUpdated;
    private Integer semester;
    private String createdBy;

}
