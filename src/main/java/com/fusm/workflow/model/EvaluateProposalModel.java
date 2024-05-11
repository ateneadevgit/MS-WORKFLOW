package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateProposalModel {

    private String evaluation;
    private String feedback;
    private FileModel fileFeedback;
    private String createdBy;
    private Integer roleId;

}
