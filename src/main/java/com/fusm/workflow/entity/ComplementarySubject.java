package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "complementary_subject")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplementarySubject {

    @Id
    @Column(name =  "id_complementary_subject", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complementarySubjectId;

    @NonNull
    @Column(name = "semester", nullable = false)
    private Integer semester;

    @NonNull
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @NonNull
    @Column(name = "hours_interaction_teacher", nullable = false)
    private Integer hoursInteractionTeacher;

    @NonNull
    @Column(name = "hours_self_work", nullable = false)
    private Integer hourSelfWork;

}
