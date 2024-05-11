package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Curriculum")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Curriculum {

    @Id
    @Column(name =  "id_curriculum", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curriculumId;

    @NonNull
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @NonNull
    @Column(name = "type", nullable = false)
    private Integer curriculumType;

    @NonNull
    @Column(name = "number_credits", nullable = false)
    private Integer numberCredits;

    @Column(name = "percentage_participation", nullable = true)
    private Double percentageParticipation;

    @Column(name = "description", length = 1000, nullable = true)
    private String description;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "is_nif", length = 50, nullable = true)
    private Boolean isNif;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "curriculum_father_id", nullable = true)
    private Curriculum currirriculumFatherId;

    @OneToOne
    @JoinColumn(name = "complementary_subject_id", nullable = true)
    private ComplementarySubject complementarySubject;

    @OneToOne
    @JoinColumn(name = "complementary_core_id", nullable = true)
    private ComplementaryCore complementaryCore;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = true)
    private WorkflowBaseStep workflowBaseStepId;

    @OneToOne
    @JoinColumn(name = "complementary_nifs_id", nullable = true)
    private ComplementaryNifs complementaryNifs;

    @OneToOne
    @JoinColumn(name = "complementary_evaluation", nullable = true)
    private ComplementaryEvaluation complementaryEvaluation;

}
