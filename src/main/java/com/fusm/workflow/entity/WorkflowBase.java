package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "WorkflowBase")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowBase {

    @Id
    @Column(name =  "id_workflow_base", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workflowBaseId;

    @NonNull
    @Column(name = "status", nullable = false)
    private String status;

    @NonNull
    @Column(name = "workflow_object_id", nullable = false)
    private Integer workflowObjectId;

    @NonNull
    @Column(name = "workflow_object_type", nullable = false)
    private String workflowObjectType;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @Column(name = "end_date", nullable = true)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflowId;

}
