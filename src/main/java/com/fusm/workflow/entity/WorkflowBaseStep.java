package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Workflow_base_step")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBaseStep {

    @Id
    @Column(name =  "workflow_base_step_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowBaseStepId;

    @NonNull
    @Column(name = "is_done", nullable = false)
    private Boolean isDone;

    @NonNull
    @Column(name = "is_sent", nullable = false)
    private Boolean isSent;

    @NonNull
    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "workflow_base_id", nullable = false)
    private WorkflowBase workflowBaseId;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    private Step stepId;

    @ManyToOne
    @JoinColumn(name = "summary_id", nullable = true)
    private Summary summaryId;

}
