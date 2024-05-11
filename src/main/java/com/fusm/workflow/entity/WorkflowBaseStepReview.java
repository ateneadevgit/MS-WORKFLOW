package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Workflow_base_step_review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBaseStepReview {

    @Id
    @Column(name =  "id_step_review", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepReviewId;

    @NonNull
    @Column(name = "review", length = 5000, nullable = false)
    private String review;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "replied_to", nullable = true)
    private WorkflowBaseStepReview repliedTo;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = false)
    private WorkflowBaseStep workflowBaseStepId;

}
