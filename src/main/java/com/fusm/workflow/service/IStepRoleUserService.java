package com.fusm.workflow.service;

import com.fusm.workflow.dto.UserAssignedToProgram;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IStepRoleUserService {

    List<Integer> getUserWithPermissionInWorkflow(String userEmail, Integer roleId);
    List<UserAssignedToProgram> getUserRelatedWithProgram(Integer programId, Integer roleId);

}
