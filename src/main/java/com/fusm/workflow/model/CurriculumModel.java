package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumModel {

    private Integer curriculumId;
    private String name;
    private Integer type;
    private String description;
    private Integer numberCredits;
    private Double percentageParticipation;
    private CoreModel coreModel;
    private SubjectRequest subjectRequest;
    private Integer fatherId;
    private Long createdAt;
    private Boolean isUpdated;
    private Boolean isNif;
    private List<CurriculumModel> childs;
    private String createdBy;

    public void addChild(CurriculumModel child) {
        childs.add(child);
    }

}
