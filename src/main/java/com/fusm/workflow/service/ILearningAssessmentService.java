package com.fusm.workflow.service;

import com.fusm.workflow.model.FileModel;
import com.fusm.workflow.model.LearningAssessmentModel;
import com.fusm.workflow.model.LearningAssessmentRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ILearningAssessmentService {

    void createLearningAssessment(LearningAssessmentRequest<FileModel> learningAssessmentRequest, Integer curriculumId);
    void updateLearningAssessment(LearningAssessmentRequest<FileModel> learningAssessmentRequest, Integer learningAssessmentId);
    LearningAssessmentModel getLearningAssessmentByCurriculumId(Integer curriculumId);
    void disableLearningAssessment(Integer learningAssessmentId);

}
