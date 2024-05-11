package com.fusm.workflow.service;

import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.PastActivityRequest;
import com.fusm.workflow.model.SearchTeacher;
import com.fusm.workflow.model.SubjectActivityRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISubjectActivityService {

    void createSubjectActivity(List<SubjectActivityRequest> activityRequest, Integer subjectGuideId);
    void updateSubjectActivity(SubjectActivityRequest activityRequest, Integer subjectActivityId);
    void updateSubjectActivityMassive(List<SubjectActivityRequest> activityRequest, Integer subjectGuideId);
    void deleteSubjectActivity(Integer subjectActivityId);
    List<SubjectActivityRequest> getPastActivities(SearchTeacher searchTeacher, Integer subjectGuideId);
    void addPastActivityToCurrentPeriod(List<PastActivityRequest> pastActivityRequests);
    void evaluateSubjectActivity(EvaluateGuide evaluateGuide, Integer subjectGuideId);
    void resetActivities();

}
