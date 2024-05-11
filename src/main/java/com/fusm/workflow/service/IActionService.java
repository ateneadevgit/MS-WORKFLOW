package com.fusm.workflow.service;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.model.ActionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IActionService {

    List<ActionModel> getActionsByRoleAndStep(Integer roleId, Integer stepId);
    List<Action> getActions();

}
