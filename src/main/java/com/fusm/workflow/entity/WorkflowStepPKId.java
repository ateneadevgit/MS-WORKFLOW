package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WorkflowStepPKId implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflowId;

    @OneToOne
    @JoinColumn(name = "step_id", nullable = false)
    private Step stepId;

}
