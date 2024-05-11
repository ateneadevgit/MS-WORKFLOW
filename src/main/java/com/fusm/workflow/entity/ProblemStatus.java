package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "problem_status")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProblemStatus {

    @Id
    @Column(name =  "id_problem_status", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer problemStatusId;

    @NonNull
    @Column(name = "id_status", nullable = false)
    private Integer statusId;

    @Column(name = "status_type", nullable = true)
    private Integer statusType;

    @Column(name = "file", length = 200, nullable = true)
    private String file;

    @Column(name = "feedback", length = 300, nullable = true)
    private String feedback;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "problem_bank_id", nullable = false)
    private ProblemBank problemBankId;

}
