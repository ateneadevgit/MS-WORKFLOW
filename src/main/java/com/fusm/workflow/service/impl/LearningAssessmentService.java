package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.entity.LearningAssessment;
import com.fusm.workflow.model.FileModel;
import com.fusm.workflow.model.LearningAssessmentModel;
import com.fusm.workflow.model.LearningAssessmentRequest;
import com.fusm.workflow.repository.ICurriculumRepository;
import com.fusm.workflow.repository.ILearningAssessmentRepository;
import com.fusm.workflow.service.ILearningAssessmentService;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class LearningAssessmentService implements ILearningAssessmentService {

    @Autowired
    private ILearningAssessmentRepository learningAssessmentRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void createLearningAssessment(LearningAssessmentRequest<FileModel> learningAssessmentRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        curriculumOptional.ifPresent(curriculum -> learningAssessmentRepository.save(
                LearningAssessment.builder()
                        .tittle(learningAssessmentRequest.getTittle())
                        .urlMoodle(learningAssessmentRequest.getUrlMoodle())
                        .evaluationMode(learningAssessmentRequest.getEvaluationMode())
                        .fileUrl(sharedMethods.saveFile(learningAssessmentRequest.getFile(), learningAssessmentRequest.getCreatedBy()))
                        .createdAt(new Date())
                        .createdBy(learningAssessmentRequest.getCreatedBy())
                        .enabled(true)
                        .curriulumId(curriculum)
                        .build()
        ));
    }

    @Override
    public void updateLearningAssessment(LearningAssessmentRequest<FileModel> learningAssessmentRequest, Integer learningAssessmentId) {
        Optional<LearningAssessment> learningAssessmentOptional = learningAssessmentRepository.findById(learningAssessmentId);
        if (learningAssessmentOptional.isPresent()) {
            LearningAssessment learningAssessment = learningAssessmentOptional.get();
            learningAssessment.setTittle(learningAssessmentRequest.getTittle());
            learningAssessment.setUrlMoodle(learningAssessmentRequest.getUrlMoodle());
            learningAssessment.setEvaluationMode(learningAssessmentRequest.getEvaluationMode());
            if (learningAssessmentRequest.getFile() != null)
                learningAssessment.setFileUrl(sharedMethods.saveFile(learningAssessmentRequest.getFile(), learningAssessmentRequest.getCreatedBy()));
            learningAssessment.setUpdatedAt(new Date());
            learningAssessmentRepository.save(learningAssessment);
        }
    }

    @Override
    public LearningAssessmentModel getLearningAssessmentByCurriculumId(Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        String description = null;
        if (curriculumOptional.isPresent()) {
            if (curriculumOptional.get().getComplementaryEvaluation() != null) {
                description = curriculumOptional.get().getComplementaryEvaluation().getDecription();
            }
        }
        return LearningAssessmentModel.builder()
                .description(description)
                .learningAssessmentList(learningAssessmentRepository.findAllByCurriculum(curriculumId).stream().map(
                        learningAssessment -> LearningAssessmentRequest.<String>builder()
                                .learningAssessmentId(learningAssessment.getLearningAssessmentId())
                                .tittle(learningAssessment.getTittle())
                                .urlMoodle(learningAssessment.getUrlMoodle())
                                .evaluationMode(learningAssessment.getEvaluationMode())
                                .file(learningAssessment.getFileUrl())
                                .createdBy(learningAssessment.getCreatedBy())
                                .build()
                ).toList())
                .build();
    }

    @Override
    public void disableLearningAssessment(Integer learningAssessmentId) {
        Optional<LearningAssessment> learningAssessmentOptional = learningAssessmentRepository.findById(learningAssessmentId);
        if (learningAssessmentOptional.isPresent()) {
            LearningAssessment learningAssessment = learningAssessmentOptional.get();
            learningAssessment.setEnabled(false);
            learningAssessmentRepository.save(learningAssessment);
        }
    }

}
