package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumChatRequest {

    private String review;
    private String createdBy;
    private Integer roleId;
    private Integer replyTo;
    private Integer objectId;
    private Integer objectType;
    private String sendTo;

}
