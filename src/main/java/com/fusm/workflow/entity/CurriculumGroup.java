package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "curriculum_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumGroup {

    @Id
    @Column(name =  "id_curriculum_group", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curriculumGroupId;

    @NonNull
    @Column(name = "group_code", length = 15, nullable = false)
    private String groupCode;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculumId;

}
