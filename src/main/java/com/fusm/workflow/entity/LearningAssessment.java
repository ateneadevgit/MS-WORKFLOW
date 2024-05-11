package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "learning_assessment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningAssessment {

    @Id
    @Column(name =  "id_learning_assessment", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer learningAssessmentId;

    @NonNull
    @Column(name = "tittle", length = 200, nullable = false)
    private String tittle;

    @NonNull
    @Column(name = "url_moodle", length = 300, nullable = false)
    private String urlMoodle;

    @NonNull
    @Column(name = "evaluation_mode", length = 1000, nullable = false)
    private String evaluationMode;

    @NonNull
    @Column(name = "file_url", length = 200, nullable = false)
    private String fileUrl;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriulumId;

}
