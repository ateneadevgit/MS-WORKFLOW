package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Syllabus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Syllabus {

    @Id
    @Column(name =  "id_syllabus", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer syllabusId;

    @NonNull
    @Column(name = "code", nullable = false)
    private String code;

    @NonNull
    @Column(name = "approved_date", nullable = false)
    private Date approvedDate;

    @Column(name = "subject_code", nullable = true)
    private String subjectCode;

    @Column(name = "cat", nullable = true)
    private String cat;

    @Column(name = "cine", nullable = true)
    private String cine;

    @Column(name = "nbc", nullable = true)
    private String nbc;

    @NonNull
    @Column(name = "attendance", nullable = false)
    private String attendance;

    @NonNull
    @Column(name = "modality_observation", length = 300, nullable = false)
    private String modalityObservation;

    @NonNull
    @Column(name = "level_formation_credits", nullable = false)
    private Integer levelFormationCredits;

    @NonNull
    @Column(name = "level_formation_prerequisites", length = 300, nullable = false)
    private String levelFormationPrerequisites;

    @NonNull
    @Column(name = "signature_type", nullable = false)
    private Integer signatureType;

    @NonNull
    @Column(name = "signature_type_observation", length = 300, nullable = false)
    private String signatureTypeObservation;

    @NonNull
    @Column(name = "created_by",length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "complementary_information_id", nullable = false)
    private SyllabusComplementaryInformation complementaryInformationId;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = true)
    private WorkflowBaseStep workflowBaseStepId;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculumId;

}
