package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "problem_bank")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemBank {

    @Id
    @Column(name =  "id_problem_bank", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer problemBankId;

    @NonNull
    @Column(name = "tittle", length = 100, nullable = false)
    private String tittle;

    @NonNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @NonNull
    @Column(name = "file", nullable = false)
    private String file;

    @Column(name = "description", length = 300, nullable = true)
    private String description;

    @Column(name = "moodle_url", length = 200, nullable = true)
    private String moodleUrl;

    @NonNull
    @Column(name = "is_nif", nullable = false)
    private Boolean isNif;

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
    @JoinColumn(name = "curriculum_id", nullable = true)
    private Curriculum curriculumId;

}
