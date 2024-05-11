package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "problem_competence")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemCompetence {

    @NonNull
    @EmbeddedId
    private ProblemCompetencePKId problemCompetencePKId;

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

}
