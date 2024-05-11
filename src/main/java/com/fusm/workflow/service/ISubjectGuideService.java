package com.fusm.workflow.service;

import com.fusm.workflow.model.*;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

@Service
public interface ISubjectGuideService {

    void createSubjectGuide(SubjectGuideRequest subjectGuideRequest, Integer curriculumId);
    void updateSubjectGuide(SubjectGuideRequest subjectGuideRequest, Integer curriculumId);
    SubjectGuideRequest getSubjectGuide(SearchTeacher searchTeacher, Integer curriculumId);
    String getSubjectGuidePdf(SearchTeacher searchTeacher, Integer curriculumId) throws DocumentException;
    SubjectGuidePreLoad getPreloadData(Integer curriculumId);
    void evaluateSubejctGuide(EvaluateGuide evaluateGuide, Integer subjectGuideId);

}
