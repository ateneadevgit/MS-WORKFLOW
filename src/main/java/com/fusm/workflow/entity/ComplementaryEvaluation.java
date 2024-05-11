package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "complementary_evaluation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplementaryEvaluation {

    @Id
    @Column(name =  "id_complementary_evaluation", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complementaryEvaluationId;

    @Column(name = "description", length = 500, nullable = false)
    private String decription;

}
