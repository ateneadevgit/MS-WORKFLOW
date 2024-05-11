package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "subject_guide_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGuideStatus {

    @Id
    @Column(name =  "id_subject_guide_status", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subjectGuideStatusId;

    @NonNull
    @Column(name = "id_status", nullable = false)
    private Integer idStatus;

    @Column(name = "status_type", nullable = false)
    private Integer statusType;

    @Column(name = "user_id", length = 50, nullable = true)
    private String userId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "subject_guide_id", nullable = false)
    private SubjectGuide subjectGuideId;

}
