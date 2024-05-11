package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Step_role_action")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StepRoleAction {

    @Id
    @Column(name =  "step_role_action_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer stepRoleActionId;

    @Column(name = "step_order", nullable = true)
    private Integer stepOrder;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = false)
    private Step stepId;

    @ManyToOne
    @JoinColumn(name = "action_id", nullable = false)
    private Action actionId;

}
