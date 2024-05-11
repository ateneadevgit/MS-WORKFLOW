package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Workflow_base_step_feedback")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBaseStepFeedback {

    @Id
    @Column(name =  "id_step_feedback", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepFeedbackId;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "feedback", nullable = true)
    private String feedback;

    @Column(name = "role_id", nullable = true)
    private Integer roleId;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "is_summary", nullable = false)
    private Boolean isSummary;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = false)
    private WorkflowBaseStep workflowBaseStepId;

}
