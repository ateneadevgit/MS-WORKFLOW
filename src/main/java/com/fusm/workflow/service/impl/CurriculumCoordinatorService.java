package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.entity.CurriculumCoordinator;
import com.fusm.workflow.model.AssignCoordinator;
import com.fusm.workflow.repository.ICurriculumCoordinatorRepository;
import com.fusm.workflow.repository.ICurriculumRepository;
import com.fusm.workflow.service.ICurriculumCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CurriculumCoordinatorService implements ICurriculumCoordinatorService {

    @Autowired
    private ICurriculumCoordinatorRepository curriculumCoordinatorRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;


    @Override
    public void assignCoordinator(AssignCoordinator assignCoordinator, Integer subjectId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(subjectId);
        curriculumOptional.ifPresent(curriculum -> curriculumCoordinatorRepository.save(
                CurriculumCoordinator.builder()
                        .coordinatorEmail(assignCoordinator.getUserEmail())
                        .coordinatorId(assignCoordinator.getUserId())
                        .roleId(assignCoordinator.getRoleId())
                        .curriculumId(curriculum)
                        .createdAt(new Date())
                        .createdBy(assignCoordinator.getCreatedBy())
                        .build()
        ));
    }

    @Override
    public AssignCoordinator coordinatorAssigned(Integer subjectId) {
        List<CurriculumCoordinator> curriculumCoordinatorList = curriculumCoordinatorRepository
                .findAssignedCoordinator(subjectId);
        AssignCoordinator coordinator = new AssignCoordinator();
        if (!curriculumCoordinatorList.isEmpty()) {
            coordinator = AssignCoordinator.builder()
                    .userEmail(curriculumCoordinatorList.get(0).getCoordinatorEmail())
                    .userId(curriculumCoordinatorList.get(0).getCoordinatorId())
                    .roleId(curriculumCoordinatorList.get(0).getRoleId())
                    .build();
        }
        return coordinator;
    }

}
