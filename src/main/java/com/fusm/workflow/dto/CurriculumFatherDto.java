package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

public interface CurriculumFatherDto {

    @Value("#{target.id_curriculum}")
    Integer getCurriculumId();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.code}")
    String getCode();

    @Value("#{target.number_credits}")
    Integer getNumberCredits();

    @Value("#{target.has_syllabus}")
    Boolean getHasSyllabus();

}
