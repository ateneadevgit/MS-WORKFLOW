package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.model.ActionModel;
import com.fusm.workflow.repository.IActionRepository;
import com.fusm.workflow.repository.IStepRoleActionRepository;
import com.fusm.workflow.service.IActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionService implements IActionService {

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private IActionRepository actionRepository;


    @Override
    public List<ActionModel> getActionsByRoleAndStep(Integer roleId, Integer stepId) {
        List<StepRoleAction> actions = stepRoleActionRepository.findActionByRoleAndStepId_StepId(roleId, stepId);
        return actions.stream().map(action ->
                ActionModel.builder()
                        .actionId(action.getActionId().getActionId())
                        .actionName(action.getActionId().getActionName())
                        .build()).toList();
    }

    @Override
    public List<Action> getActions() {
        return actionRepository.getActionsOrdered();
    }

}
