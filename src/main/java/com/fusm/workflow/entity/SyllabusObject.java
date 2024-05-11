package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "syllabus_object")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyllabusObject {

    @Id
    @Column(name =  "id_syllabus_object", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer syllabusObjectId;

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
    @JoinColumn(name = "syllabus_id", nullable = false)
    private Syllabus syllabusId;

}
