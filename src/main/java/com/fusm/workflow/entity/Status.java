package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Status {

    @Id
    @Column(name =  "id_program_status", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programStatusId;

    @NonNull
    @Column(name = "id_status", nullable = false)
    private Integer statusId;

    @NonNull
    @Column(name = "status_type", nullable = false)
    private Integer statusType;

    @Column(name = "feedback_file_url", length = 100, nullable = true)
    private String feedbackFileUrl;

    @Column(name = "feedback", length = 255, nullable = true)
    private String feedback;

    @Column(name = "roleId", nullable = true)
    private Integer roleId;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", length = 50, nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program programId;

}
