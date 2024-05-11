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
public class CurriculumSemester {

    private List<CurriculumModel> curriculumList;
    private List<SubjectSemesterModel> semesterList;

}
