package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.UserAssignedToProgram;
import com.fusm.workflow.repository.IStepRoleUserRepository;
import com.fusm.workflow.service.IStepRoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StepRoleUserService implements IStepRoleUserService {

    @Autowired
    private IStepRoleUserRepository stepRoleUserRepository;


    @Override
    public List<Integer> getUserWithPermissionInWorkflow(String userEmail, Integer roleId) {
        return stepRoleUserRepository.findObjectByUserPermission(userEmail, roleId);
    }

    @Override
    public List<UserAssignedToProgram> getUserRelatedWithProgram(Integer programId, Integer roleId) {
        return stepRoleUserRepository.findUserAssignedToProgram(roleId, programId);
    }

}
