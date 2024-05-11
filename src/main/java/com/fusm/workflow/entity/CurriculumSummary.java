package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Curriculum_summary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumSummary {

    @Id
    @Column(name =  "id_curriculum_summary", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curriculumSummaryId;

    @NonNull
    @Column(name = "summary", length = 5000, nullable = false)
    private String summary;

    @NonNull
    @Column(name = "curriculum_type", nullable = false)
    private Integer curriculumType;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = false)
    private WorkflowBaseStep workflowBaseStepId;

}
