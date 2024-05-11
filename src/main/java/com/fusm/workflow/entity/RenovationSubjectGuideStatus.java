package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "renovation_subject_guide_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenovationSubjectGuideStatus {

    @Id
    @Column(name =  "id_renovation_status", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer renovationSubjectGuideStatusId;

    @NonNull
    @Column(name = "id_status", nullable = false)
    private Integer idStatus;

    @NonNull
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "renovation_subject_guide_id", nullable = false)
    private RenovationSubjectGuide renovationSubjectGuideId;

}
