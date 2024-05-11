package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.RenovationSubjectGuideDto;
import com.fusm.workflow.entity.RenovationSubjectGuide;
import com.fusm.workflow.entity.RenovationSubjectGuideStatus;
import com.fusm.workflow.entity.SubjectGuide;
import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.RenovationSubjectGuideRequest;
import com.fusm.workflow.model.UserData;
import com.fusm.workflow.repository.IRenovationSubjectGuideRepository;
import com.fusm.workflow.repository.IRenovationSubjectGuideStatusRepository;
import com.fusm.workflow.repository.ISubjectGuideRepository;
import com.fusm.workflow.service.IRenovationSubjectGuideService;
import com.fusm.workflow.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RenovationSubjectGuideService implements IRenovationSubjectGuideService {

    @Autowired
    private IRenovationSubjectGuideRepository renovationSubjectGuideRepository;

    @Autowired
    private IRenovationSubjectGuideStatusRepository renovationSubjectGuideStatusRepository;

    @Autowired
    private ISubjectGuideRepository subjectGuideRepository;


    @Override
    public void createRenovationSubjectGuide(RenovationSubjectGuideRequest renovationSubjectGuideRequest, Integer subjectGuideId) {
        Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
        if (subjectGuideOptional.isPresent()) {
            RenovationSubjectGuide renovationSubjectGuide = renovationSubjectGuideRepository.save(
                    RenovationSubjectGuide.builder()
                            .content(renovationSubjectGuideRequest.getContent())
                            .userId(renovationSubjectGuideRequest.getCreatedBy())
                            .createdBy(renovationSubjectGuideRequest.getCreatedBy())
                            .createdAt(new Date())
                            .subjectGuideId(subjectGuideOptional.get())
                            .build()
            );
            saveStatus(
                    Constant.STATUS_SENT_REVIEW,
                    renovationSubjectGuideRequest.getCreatedBy(),
                    renovationSubjectGuideRequest.getCreatedBy(),
                    renovationSubjectGuide);
        }
    }

    @Override
    public void evaluateRenovationSubjectGuide(EvaluateGuide evaluateGuide, Integer renovationId) {
        Optional<RenovationSubjectGuide> optionalRenovationSubjectGuide = renovationSubjectGuideRepository.findById(renovationId);
        optionalRenovationSubjectGuide.ifPresent(renovationSubjectGuide -> saveStatus(
                evaluateGuide.getFeedbackStatus(),
                evaluateGuide.getCreatedBy(),
                evaluateGuide.getUserId(),
                renovationSubjectGuide
        ));
    }

    @Override
    public List<RenovationSubjectGuideDto> getRenovationsBySubjectGuide(UserData userData, Integer subjectGuideId) {
        return renovationSubjectGuideRepository.findAllRenovation(userData.getRoleId(), subjectGuideId);
    }

    private void saveStatus(String status, String createdBy, String userId, RenovationSubjectGuide renovationSubjectGuide) {
        renovationSubjectGuideStatusRepository.save(
                RenovationSubjectGuideStatus.builder()
                        .idStatus(getStatus(status))
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .userId(userId)
                        .renovationSubjectGuideId(renovationSubjectGuide)
                        .build()
        );
    }

    private Integer getStatus(String status) {
        Integer statusGuide = Constant.STATUS_RENOVATION_GUIDE_ON_REQUEST;
        if (status.equals(Constant.STATUS_APPROVED)) statusGuide = Constant.STATUS_RENOVATION_GUIDE_APPROVED;
        if (status.equals(Constant.STATUS_DECLINED)) statusGuide = Constant.STATUS_RENOVATION_GUIDE_DECLINED;
        if (status.equals(Constant.STATUS_DONE)) statusGuide = Constant.STATUS_RENOVATION_DONE;
        return statusGuide;
    }

}
