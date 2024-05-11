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
public class SyllabusInformationModel {

    private String programName;
    private Integer levelFormationId;
    private Integer facultyId;
    private List<Integer> campus;
    private List<Integer> modalities;
    private String subjectCode;
    private Integer credits;

}
