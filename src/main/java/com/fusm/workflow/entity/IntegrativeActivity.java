package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "integrative_activity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IntegrativeActivity {

    @Id
    @Column(name =  "id_integrative_activity", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer integrativeActivityId;

    @NonNull
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @Column(name = "description", length = 500, nullable = true)
    private String description;

    @NonNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private String updatedAt;

    @NonNull
    @Column(name = "activity_value", length = 1000, nullable = false)
    private String activityValue;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculumId;

}
