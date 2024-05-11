package com.fusm.workflow.service;

import com.fusm.workflow.model.StepRoleActionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IStepRoleActionService {

    List<Integer> getRolesRelatedWithStep(Integer stepId);
    void deleteRoleFromStep(Integer roleId, Integer stepId);
    void addRoleActionToStep(Integer stepId, StepRoleActionRequest roleActions);
    void updateActionToRole(Integer stepId, StepRoleActionRequest roleActions);

}
