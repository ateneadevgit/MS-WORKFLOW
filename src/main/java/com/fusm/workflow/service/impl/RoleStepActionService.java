package com.fusm.workflow.service.impl;

import com.fusm.workflow.repository.IStepRoleActionRepository;
import com.fusm.workflow.service.IRoleStepActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleStepActionService implements IRoleStepActionService {

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;


    @Override
    public List<Integer> getRolesRelatedWithStep(Integer stepId) {
        return stepRoleActionRepository.getRolesRelatedWithStep(stepId);
    }

}
