package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningAssessmentRequest<T> {

    private Integer learningAssessmentId;
    private String tittle;
    private String urlMoodle;
    private String evaluationMode;
    private T file;
    private String createdBy;

}
