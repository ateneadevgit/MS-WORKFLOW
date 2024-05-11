package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "renovation_subject_guide")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RenovationSubjectGuide {

    @Id
    @Column(name =  "id_renovation_subject_guide", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer renovationSubjectGuideId;

    @NonNull
    @Column(name = "content", length = 1000, nullable = false)
    private String content;

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
    @JoinColumn(name = "subject_guide_id", nullable = false)
    private SubjectGuide subjectGuideId;

}
