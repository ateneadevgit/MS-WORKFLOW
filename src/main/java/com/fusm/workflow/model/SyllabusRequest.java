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
public class SyllabusRequest {

    private Integer syllabusId;
    private String code;
    private Long approvedDate;
    private String cat;
    private String cine;
    private String nbc;
    private String attendance;
    private String modalityObservation;
    private Integer levelFormationCredits;
    private String levelFormationPrerequisites;
    private Integer signatureType;
    private String signatureTypeObservation;
    private String createdBy;
    private Boolean enabled;
    private Integer workflowBaseId;
    private Integer stepId;
    private Integer curriculumId;
    private String subjectConformation;
    private String subjectContext;
    private String subjectDescription;
    private String learningGeneral;
    private String learningSpecific;
    private String content;
    private String pedagogicalPractices;
    private String bibliographyBasic;
    private String bibliographyLenguaje;
    private String bibliographyWeb;
    private List<Integer> programIds;
    private List<Integer> campusIds;
    private List<Integer> levelFormationIds;

}


