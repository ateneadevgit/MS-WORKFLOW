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
public class SearchSubject {

    private Integer programId;
    private Integer statusId;
    private Integer semester;
    private String createdBy;
    private Integer roleId;
    private Boolean isActivity;
    private String userId;
}
