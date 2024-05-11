package com.fusm.workflow.service;

import com.fusm.workflow.model.IntegrativeActivityRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IIntegrativeActivityService {

    void createActivity(List<IntegrativeActivityRequest> activityRequestList, Integer curriculumId);
    void updateActivity(IntegrativeActivityRequest activityRequest, Integer activityId);
    void disableActivity(Integer activityId);

}
