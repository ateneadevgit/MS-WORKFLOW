package com.fusm.workflow.service;

import com.fusm.workflow.dto.WorkflowStepDto;
import com.fusm.workflow.model.ActionModel;
import com.fusm.workflow.model.AttachmentModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IAttachmentService {

    List<AttachmentModel> hasViewAttachment(List<ActionModel> actions, WorkflowStepDto stepDto, Integer workflowBaseId, Integer roleId);
    void changeAttachStatus(Integer status, Integer workflowId, Integer stepId, Integer roleId);

}
