package com.fusm.workflow.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRoleStepActionService {

    List<Integer> getRolesRelatedWithStep(Integer stepId);

}
