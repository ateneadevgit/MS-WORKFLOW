package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface SubjectTeacherDto {

    @Value("#{target.id_curriculum}")
    Integer getIdCurriculum();

    @Value("#{target.user_id}")
    String getUserId();

    @Value("#{target.created_at}")
    Date getCreatedAt();

    @Value("#{target.id_status}")
    Integer getIdStatus();

    @Value("#{target.id_subject_guide}")
    Integer getIdSubjectGuide();

    @Value("#{target.comment_count}")
    Integer getCommentCount();

}
