package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Workflow_base_last_action")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBaseLastAction {

    @Id
    @Column(name =  "id_last_action", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lastActionId;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NonNull
    @Column(name = "action_id", nullable = false)
    private Integer actionId;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = false)
    private WorkflowBaseStep workflowBaseStepId;

}
