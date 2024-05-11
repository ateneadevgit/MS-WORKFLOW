package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface RenovationSubjectGuideDto {

    @Value("#{target.id_renovation_subject_guide}")
    Integer getIdRenovationSubjectGuide();

    @Value("#{target.user_id}")
    String getUserId();

    @Value("#{target.content}")
    String getContent();

    @Value("#{target.id_status}")
    Integer getIdStatus();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.created_at}")
    Date getCreatedAt();

}
