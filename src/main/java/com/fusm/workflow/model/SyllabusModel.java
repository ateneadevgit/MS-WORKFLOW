package com.fusm.workflow.model;
import lombok.*;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusModel {

    private Integer syllabusId;
    private String code;
    private Date approvedDate;
    private String cat;
    private String subjectCode;
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
    private Date createdAt;
    private Date updatedAt;
    private List<Integer> programIds;
    private List<Integer> campusIds;
    private List<Integer> levelFormationIds;
    private List<Integer> facultyIds;
    private List<Integer> modalities;

}
