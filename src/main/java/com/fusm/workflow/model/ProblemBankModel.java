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
public class ProblemBankModel {

    private Integer problemBankId;
    private Integer semester;
    private String tittle;
    private String file;
    private String description;
    private String linkMoodle;
    private Boolean enabled;
    private String program;
    private Integer statusId;
    private Integer subjectId;
    private String subject;
    private List<CompetenceRequest> competences;

}
