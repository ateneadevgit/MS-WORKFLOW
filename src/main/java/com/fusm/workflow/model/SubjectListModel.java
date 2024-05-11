package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectListModel {

    private Integer subjectId;
    private String name;
    private Integer semester;
    private Integer creditNumber;
    private Boolean isUpdated;
    private String createdBy;

}
