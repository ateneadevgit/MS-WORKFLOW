package com.fusm.workflow.dto;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

public interface WorkflowStepDto {

    @Value("#{target.step_id}")
    Integer getStepId();

    @Value("#{target.is_editable}")
    Boolean getIsEditable();

    @Value("#{target.is_done}")
    Boolean getIsDone();

    @Value("#{target.is_sent}")
    Boolean getIsSent();

    @Value("#{target.step_name}")
    String getStepName();

    @Value("#{target.step_order}")
    Integer getStepOrder();

    @Value("#{target.status}")
    Integer getStatus();

    @Value("#{target.control_id}")
    Integer getControlId();

    @Value("#{target.workflow_step_id}")
    Integer getWorkflowStepId();

}
