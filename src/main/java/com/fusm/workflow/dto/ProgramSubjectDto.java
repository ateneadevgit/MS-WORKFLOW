package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface ProgramSubjectDto {

    @Value("#{target.id_curriculum}")
    Integer getIdCurriculum();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.semester}")
    Integer getSemester();

    @Value("#{target.id_subject_guide}")
    Integer getIdSubjectGuide();

    @Value("#{target.id_status}")
    Integer getIdStatus();

    @Value("#{target.created_at}")
    Date getCreatedAt();

    @Value("#{target.coordinator_id}")
    String getCoordinatorId();

    @Value("#{target.activity_status}")
    Integer getActivityStatus();

    @Value("#{target.activity_created_at}")
    Date getActivityCreatedAt();

    @Value("#{target.update_status}")
    Integer getUpdateStatus();

    @Value("#{target.update_created_at}")
    Date getUpdateCreatedAt();

    @Value("#{target.comment_count}")
    Integer getCommentCount();

    @Value("#{target.request_update_count}")
    Integer getRequestUpdateCount();

}
