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
public class CurriculumDetail {

    private Integer curriculumId;
    private String name;
    private String description;
    private Integer type;
    private String raeg;
    private Integer semester;
    private Integer hoursInteractionTeacher;
    private Integer hoursSelfWork;
    private Integer credits;
    private List<CurriculumDetailChild> childs;
    private Integer totalCredits;
    private double totalParticipation;
    private String rae;
    private String competences;
    private String code;
    private List<IntegrativeActivityRequest> activities;

}
