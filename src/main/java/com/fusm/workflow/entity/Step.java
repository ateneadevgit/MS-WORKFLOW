package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Step")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Step {

    @Id
    @Column(name =  "id_step", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepId;

    @NonNull
    @Column(name = "name", length = 100, nullable = false)
    private String stepName;

    @NonNull
    @Column(name = "control_id", nullable = false)
    private Integer controlId;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory;

    @Column(name = "step_type", nullable = true)
    private Integer stepType;

    @NonNull
    @Column(name = "has_summary", nullable = false)
    private Boolean hasSummary;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

}
