package com.fusm.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignCoordinator {

    private String userId;
    private String userEmail;
    private Integer roleId;
    private String createdBy;

}
