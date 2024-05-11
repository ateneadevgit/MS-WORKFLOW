package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Workflow")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Workflow {

    @Id
    @Column(name =  "id_workflow", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowId;

    @NonNull
    @Column(name = "name", length = 150, nullable = false)
    private String name;

    @Column(name = "description", length = 100, nullable = true)
    private String description;

    @Column(name = "is_base", nullable = true)
    private Boolean isBase;

    @Column(name = "workflow_type", nullable = true)
    private Integer workflowType;

}
