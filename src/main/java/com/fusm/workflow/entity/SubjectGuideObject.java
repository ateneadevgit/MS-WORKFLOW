package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "subject_guide_object")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGuideObject {

    @Id
    @Column(name =  "id_guide_object", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer guideObjectId;

    @NonNull
    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @NonNull
    @Column(name = "object_type", nullable = false)
    private Integer objectType;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "subject_guide_id", nullable = false)
    private SubjectGuide subjectGuideId;

}
