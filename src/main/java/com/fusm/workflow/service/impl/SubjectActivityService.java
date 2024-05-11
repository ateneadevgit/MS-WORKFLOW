package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.SubjectGuideActivity;
import com.fusm.workflow.entity.SubjectGuide;
import com.fusm.workflow.entity.SubjectGuideStatus;
import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.PastActivityRequest;
import com.fusm.workflow.model.SearchTeacher;
import com.fusm.workflow.model.SubjectActivityRequest;
import com.fusm.workflow.repository.ISubjectActivityRepository;
import com.fusm.workflow.repository.ISubjectGuideRepository;
import com.fusm.workflow.repository.ISubjectGuideStatusRepository;
import com.fusm.workflow.service.ISubjectActivityService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectActivityService implements ISubjectActivityService {

    @Autowired
    private ISubjectActivityRepository subjectActivityRepository;

    @Autowired
    private ISubjectGuideRepository subjectGuideRepository;

    @Autowired
    private ISubjectGuideStatusRepository subjectGuideStatusRepository;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void createSubjectActivity(List<SubjectActivityRequest> activityRequestList, Integer subjectGuideId) {
        Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
        if (subjectGuideOptional.isPresent()) {
            for (SubjectActivityRequest subjectActivityRequest : activityRequestList) {
                subjectActivityRepository.save(
                        SubjectGuideActivity.builder()
                                .session(subjectActivityRequest.getSession())
                                .activityDate(subjectActivityRequest.getActivityDate())
                                .result(subjectActivityRequest.getResult())
                                .topic(subjectActivityRequest.getTopic())
                                .syncActivities(subjectActivityRequest.getSyncActivities())
                                .previusActivities(subjectActivityRequest.getPreviusActivities())
                                .strategies(subjectActivityRequest.getStrategies())
                                .url(subjectActivityRequest.getUrl())
                                .enabled(true)
                                .createdBy(subjectActivityRequest.getCreatedBy())
                                .createdAt(new Date())
                                .teacher(subjectActivityRequest.getCreatedBy())
                                .subjectGuideId(subjectGuideOptional.get())
                                .period(sharedMethods.getPeriod())
                                .canUpdate(true)
                                .build()
                );
            }
        }
    }

    @Override
    public void updateSubjectActivity(SubjectActivityRequest activityRequest, Integer subjectActivityId) {
        Optional<SubjectGuideActivity> subjectActivityOptional = subjectActivityRepository.findById(subjectActivityId);
        buildAndSaveSubjectActivity(activityRequest, subjectActivityOptional);
    }

    @Override
    public void updateSubjectActivityMassive(List<SubjectActivityRequest> activityRequest, Integer subjectGuideId) {
        Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
        if (subjectGuideOptional.isPresent()) {
            for (SubjectActivityRequest subjectActivityRequest : activityRequest) {
                Optional<SubjectGuideActivity> subjectActivityOptional = subjectActivityRepository
                        .findById(subjectActivityRequest.getSubjectActivityId());
                buildAndSaveSubjectActivity(subjectActivityRequest, subjectActivityOptional);
            }
        }
    }

    private void buildAndSaveSubjectActivity(SubjectActivityRequest subjectActivityRequest, Optional<SubjectGuideActivity> subjectActivityOptional) {
        if (subjectActivityOptional.isPresent()) {
            SubjectGuideActivity subjectGuideActivity = subjectActivityOptional.get();
            subjectGuideActivity.setSession(subjectGuideActivity.getSession());
            subjectGuideActivity.setActivityDate(subjectActivityRequest.getActivityDate());
            subjectGuideActivity.setResult(subjectActivityRequest.getResult());
            subjectGuideActivity.setTopic(subjectActivityRequest.getTopic());
            subjectGuideActivity.setSyncActivities(subjectActivityRequest.getSyncActivities());
            subjectGuideActivity.setPreviusActivities(subjectActivityRequest.getPreviusActivities());
            subjectGuideActivity.setStrategies(subjectActivityRequest.getStrategies());
            subjectGuideActivity.setUrl(subjectActivityRequest.getUrl());
            subjectActivityRepository.save(subjectGuideActivity);
        }
    }

    @Override
    public void deleteSubjectActivity(Integer subjectActivityId) {
        Optional<SubjectGuideActivity> subjectActivityOptional = subjectActivityRepository.findById(subjectActivityId);
        if (subjectActivityOptional.isPresent()) {
            SubjectGuideActivity subjectGuideActivity = subjectActivityOptional.get();
            subjectGuideActivity.setEnabled(false);
            subjectActivityRepository.save(subjectGuideActivity);
        }
    }

    @Override
    public List<SubjectActivityRequest> getPastActivities(SearchTeacher searchTeacher, Integer subjectGuideId) {
        return subjectActivityRepository.findAllByGuideAndTeacherAndPeriod(
                subjectGuideId, searchTeacher.getTeacher(), sharedMethods.getPeriod()).stream().map(
                subjectActivity -> SubjectActivityRequest.builder()
                        .subjectActivityId(subjectActivity.getSubjectActivityId())
                        .session(subjectActivity.getSession())
                        .activityDate(subjectActivity.getActivityDate())
                        .result(subjectActivity.getResult())
                        .topic(subjectActivity.getTopic())
                        .syncActivities(subjectActivity.getSyncActivities())
                        .previusActivities(subjectActivity.getPreviusActivities())
                        .strategies(subjectActivity.getStrategies())
                        .url(subjectActivity.getUrl())
                        .enabled(subjectActivity.getEnabled())
                        .build()
        ).toList();
    }

    @Override
    public void addPastActivityToCurrentPeriod(List<PastActivityRequest> pastActivityRequests) {
        for (PastActivityRequest pastActivityRequest : pastActivityRequests) {
            Optional<SubjectGuideActivity> subjectGuideActivityOptional = subjectActivityRepository.findById(pastActivityRequest.getActivityId());
            if (subjectGuideActivityOptional.isPresent()) {
                SubjectGuideActivity subjectGuideActivity = subjectGuideActivityOptional.get();
                subjectGuideActivity.setUrl(pastActivityRequest.getLinkMoodle());
                subjectGuideActivity.setCreatedBy(pastActivityRequest.getCreatedBy());
                subjectGuideActivity.setPeriod(sharedMethods.getPeriod());
                subjectActivityRepository.save(subjectGuideActivity);
            }
        }
    }

    @Override
    public void evaluateSubjectActivity(EvaluateGuide evaluateGuide, Integer subjectGuideId) {
        Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
        subjectGuideOptional.ifPresent(subjectGuide ->
                saveStatus(
                        subjectGuide,
                        Constant.CREATE_ACTIVITY_TYPE,
                        getStatus(evaluateGuide.getFeedbackStatus()),
                        evaluateGuide.getCreatedBy(),
                        evaluateGuide.getUserId()));
    }

    @Scheduled(cron = "0 0 0 1 1,7 ?")
    @Override
    public void resetActivities() {
        List<SubjectGuideStatus> usersToReset = subjectGuideStatusRepository.findUsersToReset();
        for (SubjectGuideStatus subjectGuideStatus : usersToReset) {
            if (subjectGuideStatus.getSubjectGuideId().getCurriculumId().getComplementaryNifs() == null) {
                saveStatus(
                        subjectGuideStatus.getSubjectGuideId(),
                        Constant.CREATE_ACTIVITY_TYPE,
                        Constant.STATUS_GUIDE_CLEAN,
                        subjectGuideStatus.getUserId(),
                        subjectGuideStatus.getUserId());
            }
        }
    }

    private void saveStatus(SubjectGuide subjectGuide, Integer type, Integer status, String createdBy, String userId) {
        subjectGuideStatusRepository.save(
                SubjectGuideStatus.builder()
                        .idStatus(status)
                        .statusType(type)
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .subjectGuideId(subjectGuide)
                        .userId(userId)
                        .build()
        );
    }

    private Integer getStatus(String status) {
        Integer statusGuide = Constant.STATUS_GUIDE_ON_REVIEW;
        if (status.equals(Constant.STATUS_CLEAN)) statusGuide = Constant.STATUS_GUIDE_CLEAN;
        if (status.equals(Constant.STATUS_APPROVED)) statusGuide = Constant.STATUS_GUIDE_APPROVED;
        if (status.equals(Constant.STATUS_COMPLETENESS)) statusGuide = Constant.STATUS_GUIDE_COMPLETENESS;
        return statusGuide;
    }

}
