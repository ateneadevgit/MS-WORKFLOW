package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ProblemBankDto {

    @Value("#{target.id_problem_bank}")
    Integer getIdProblemBank();

    @Value("#{target.tittle}")
    String getTittle();

    @Value("#{target.semester}")
    Integer getSemester();

    @Value("#{target.moodle_url}")
    String getMoodleUrl();

    @Value("#{target.file}")
    String getFile();

    @Value("#{target.description}")
    String getDescription();

    @Value("#{target.curriculum_name}")
    String getCurriculumName();

    @Value("#{target.curriculum_id}")
    Integer getCurriculumId();

    @Value("#{target.program_id}")
    Integer getProgramId();

    @Value("#{target.id_status}")
    Integer getIdStatus();

    @Value("#{target.enabled}")
    Boolean getEnabled();

}
