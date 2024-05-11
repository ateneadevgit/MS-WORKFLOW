package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.entity.Step;
import com.fusm.workflow.entity.StepRoleAction;
import com.fusm.workflow.model.StepRoleActionRequest;
import com.fusm.workflow.repository.IActionRepository;
import com.fusm.workflow.repository.IStepRepository;
import com.fusm.workflow.repository.IStepRoleActionRepository;
import com.fusm.workflow.service.IStepRoleActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepRoleActionService implements IStepRoleActionService {

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private IStepRepository stepRepository;

    @Autowired
    private IActionRepository actionRepository;


    @Override
    public List<Integer> getRolesRelatedWithStep(Integer stepId) {
        return stepRoleActionRepository.findAllByStepId_StepId(stepId).stream().map(
                StepRoleAction::getRoleId
        ).toList();
    }

    @Override
    public void deleteRoleFromStep(Integer roleId, Integer stepId) {
        List<StepRoleAction> stepRoleActionList = stepRoleActionRepository
                .findActionByRoleAndStepId_StepId(roleId, stepId);
        stepRoleActionRepository.deleteAll(stepRoleActionList);
    }

    @Override
    public void addRoleActionToStep(Integer stepId, StepRoleActionRequest roleActions) {
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (stepOptional.isPresent()) {
            for (Action action : roleActions.getActionIds()) {
                Optional<Action> actionOptional = actionRepository.findById(action.getActionId());
                actionOptional.ifPresent(value -> stepRoleActionRepository.save(
                        StepRoleAction.builder()
                                .roleId(roleActions.getRoleId())
                                .stepId(stepOptional.get())
                                .actionId(value)
                                .build()
                ));
            }
        }
    }

    @Override
    public void updateActionToRole(Integer stepId, StepRoleActionRequest roleActions) {
        Optional<Step> stepOptional = stepRepository.findById(stepId);
        if (stepOptional.isPresent()) {
            List<StepRoleAction> stepRoleActionList = stepRoleActionRepository
                    .findActionByRoleAndStepId_StepId(roleActions.getRoleId(), stepId);
            updateOrDeleteActionToRole(stepRoleActionList, roleActions, stepOptional.get());
        }
    }

    private void updateOrDeleteActionToRole(
            List<StepRoleAction> stepRoleActionList, StepRoleActionRequest roleActions, Step step) {
        List<Action> actions = roleActions.getActionIds();

        for (StepRoleAction stepRoleAction : stepRoleActionList) {
            if (!actions.contains(stepRoleAction.getActionId())) {
                // Si no contiene entonces eliminarla
                stepRoleActionRepository.delete(stepRoleAction);
                actions = actions.stream().filter(
                        action -> !action.getActionId().equals(stepRoleAction.getActionId().getActionId())
                ).toList();
            }
        }

        for (Action action: actions) {
            Optional<Action> actionOptional = actionRepository.findById(action.getActionId());
            actionOptional.ifPresent(value -> stepRoleActionRepository.save(
                    StepRoleAction.builder()
                            .roleId(roleActions.getRoleId())
                            .stepId(step)
                            .actionId(value)
                            .build()
            ));
        }
    }

}
