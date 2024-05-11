package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Attach")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attach {

    @Id
    @Column(name =  "id_attach", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attachId;

    @NonNull
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NonNull
    @Column(name = "is_sent", nullable = false)
    private Boolean isSent;

    @NonNull
    @Column(name = "url", nullable = false)
    private String url;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NonNull
    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "role_id", nullable = true)
    private Integer roleId;

    @Column(name = "feedback", nullable = true)
    private String feedback;

    @NonNull
    @Column(name = "is_original", nullable = false)
    private Boolean isOriginal;

    @NonNull
    @Column(name = "is_declined", nullable = false)
    private Boolean isDeclined;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "referenced_attach", nullable = true)
    private Attach referencedAttach;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "attach_father", nullable = true)
    private Attach attachFather;

    @ManyToOne
    @JoinColumn(name = "workflow_base_step_id", nullable = false)
    private WorkflowBaseStep workflowBaseStepId;

}
