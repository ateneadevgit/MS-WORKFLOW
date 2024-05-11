package com.fusm.workflow.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface UserAssignedToProgram {

    @Value("#{target.user_email}")
    String getUserEmail();

    @Value("#{target.created_at}")
    Date getCreatedAt();

}
