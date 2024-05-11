package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "proposal_renovation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposalRenovation {

    @Id
    @Column(name =  "id_proposal_renovation", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proposalRenovationId;

    @NonNull
    @Column(name = "has_evaluation", nullable = false)
    private Boolean hasEvaluation;

    @NonNull
    @Column(name = "minute_renovation", nullable = false)
    private String minuteRenovation;

    @Column(name = "minute_response", nullable = true)
    private String minuteResponse;

    @NonNull
    @Column(name = "status", nullable = false)
    private Integer status;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program programId;

}
