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
public class SubjectGuidePreLoad {

    private String core;
    private String subCore;
    private List<Integer> campusIds;
    private Integer facultyIds;
    private Integer programIds;
    private List<Integer> modalityIds;
    private String subjectCode;
    private Integer creditNumber;
    private Integer hourInteractionTeacher;
    private Integer hourSelfWork;


}
