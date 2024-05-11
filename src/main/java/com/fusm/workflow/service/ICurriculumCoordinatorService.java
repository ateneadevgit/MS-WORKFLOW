package com.fusm.workflow.service;

import com.fusm.workflow.model.AssignCoordinator;
import org.springframework.stereotype.Service;

@Service
public interface ICurriculumCoordinatorService {

    void assignCoordinator(AssignCoordinator assignCoordinator, Integer subjectId);
    AssignCoordinator coordinatorAssigned(Integer subjectId);

}
