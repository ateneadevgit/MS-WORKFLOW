package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.entity.IntegrativeActivity;
import com.fusm.workflow.model.IntegrativeActivityRequest;
import com.fusm.workflow.repository.ICurriculumRepository;
import com.fusm.workflow.repository.IIntegrativeActivityRepository;
import com.fusm.workflow.service.IIntegrativeActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class IntegrativeActivityService implements IIntegrativeActivityService {

    @Autowired
    private IIntegrativeActivityRepository integrativeActivityRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;


    @Override
    public void createActivity(List<IntegrativeActivityRequest> activityRequestList, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        if (curriculumOptional.isPresent()) {
            for (IntegrativeActivityRequest activityRequest : activityRequestList) {
                integrativeActivityRepository.save(
                        IntegrativeActivity.builder()
                                .name(activityRequest.getTittle())
                                .description(activityRequest.getDescription())
                                .activityValue(activityRequest.getActivity())
                                .createdBy(activityRequest.getCreatedBy())
                                .createdAt(new Date())
                                .curriculumId(curriculumOptional.get())
                                .enabled(true)
                                .build()
                );
            }
        }
    }

    @Override
    public void updateActivity(IntegrativeActivityRequest activityRequest, Integer activityId) {
        Optional<IntegrativeActivity> integrativeActivityOptional = integrativeActivityRepository.findById(activityId);
        if (integrativeActivityOptional.isPresent()) {
            IntegrativeActivity integrativeActivity = integrativeActivityOptional.get();
            integrativeActivity.setName(activityRequest.getTittle());
            integrativeActivity.setDescription(activityRequest.getDescription());
            integrativeActivity.setActivityValue(activityRequest.getActivity());
            integrativeActivityRepository.save(integrativeActivity);
        }
    }

    @Override
    public void disableActivity(Integer activityId) {
        Optional<IntegrativeActivity> integrativeActivityOptional = integrativeActivityRepository.findById(activityId);
        if (integrativeActivityOptional.isPresent()) {
            IntegrativeActivity integrativeActivity = integrativeActivityOptional.get();
            integrativeActivity.setEnabled(false);
            integrativeActivityRepository.save(integrativeActivity);
        }
    }

}
