package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ProblemCompetencePKId implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToOne
    @JoinColumn(name = "competence_id", nullable = false)
    private Competence competenceId;

    @OneToOne
    @JoinColumn(name = "problem_bank_id", nullable = false)
    private ProblemBank problemBankId;

}
