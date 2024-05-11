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
public class SubcoreNifsRequest {

    private CurriculumRequest subjectRequest;
    private String rae;
    private String competences;
    private List<IntegrativeActivityRequest> activities;
    private SyllabusModel syllabus;
    private SubjectGuideRequest subjectGuide;
    private String createdBy;
    private Integer roleId;

}
