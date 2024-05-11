package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Step_role_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StepRoleUsers {

    @Id
    @Column(name =  "step_role_user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepRoleUserId;

    @Column(name = "user_id", length = 50, nullable = true)
    private String userId;

    @Column(name = "user_email", length = 50, nullable = false)
    private String userEmail;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "created_at", nullable = true)
    private Date createdAt;

    @Column(name = "role_user_id", nullable = false)
    private Integer roleUser;

    @ManyToOne
    @JoinColumn(name = "step_role_action_id", nullable = true)
    private StepRoleAction stepRoleActionId;

    @ManyToOne
    @JoinColumn(name = "workflow_base_id", nullable = false)
    private WorkflowBase workflowBaseId;

}
