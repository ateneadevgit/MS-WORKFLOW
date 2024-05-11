package com.fusm.workflow.model;

import com.fusm.workflow.entity.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepRoleActionRequest {

    private Integer roleId;
    private List<Action> actionIds;

}
