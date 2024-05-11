package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepModel {

    private Integer stepId;
    private String stepName;
    private Integer orderId;
    private Integer controlId;
    private Boolean isEditable;
    private Boolean isSent;
    private Boolean isDone;
    private Integer status;
    private Boolean isLastTouched;
    private Boolean hasEvaluated;
    private Boolean canCreateSummary;
    private Boolean hasAlreadySummary;
    private List<TemplateModel> template;
    private List<AttachmentModel> attachment;
    private List<ActionModel> actions;

}
