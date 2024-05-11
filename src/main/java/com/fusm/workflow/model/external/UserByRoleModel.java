package com.fusm.workflow.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserByRoleModel {

    private String userId;
    private String userEmail;
    private Integer roleId;

}
