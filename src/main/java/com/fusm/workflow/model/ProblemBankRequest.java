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
public class ProblemBankRequest {

    private Integer semester;
    private String tittle;
    private FileModel file;
    private String description;
    private List<Integer> competences;
    private String linkMoodle;
    private String createdBy;
    private Integer roleId;
    private Integer curriculumId;
    private FacultyProgramModel facultyProgram;

}
