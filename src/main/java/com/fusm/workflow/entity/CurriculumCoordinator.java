package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "curriculum_coordinator")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumCoordinator {

    @Id
    @Column(name =  "id_curriculum_coordinator", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curriculumCoordinatorId;

    @NonNull
    @Column(name = "coordinator_id", length = 50, nullable = false)
    private String coordinatorId;

    @NonNull
    @Column(name = "coordinator_email", length = 50, nullable = false)
    private String coordinatorEmail;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculumId;

}
