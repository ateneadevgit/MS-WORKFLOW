package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Builder
@Entity
@Data
@Table(name = "Workflow_step")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowStep {

    @NonNull
    @EmbeddedId
    private WorkflowStepPKId workflowStepPKId;

    @NonNull
    @Column(name = "step_order", nullable = false)
    private Integer stepOrder;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

}
