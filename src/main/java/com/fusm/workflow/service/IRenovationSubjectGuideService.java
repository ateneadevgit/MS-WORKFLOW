package com.fusm.workflow.service;

import com.fusm.workflow.dto.RenovationSubjectGuideDto;
import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.RenovationSubjectGuideRequest;
import com.fusm.workflow.model.UserData;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRenovationSubjectGuideService {

    void createRenovationSubjectGuide(RenovationSubjectGuideRequest renovationSubjectGuideRequest, Integer subjectGuideId);
    void evaluateRenovationSubjectGuide(EvaluateGuide evaluateGuide, Integer renovationId);
    List<RenovationSubjectGuideDto> getRenovationsBySubjectGuide(UserData userData, Integer subjectGuideId);

}
