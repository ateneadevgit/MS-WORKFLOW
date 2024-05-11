package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Traceability")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Traceability {

    @Id
    @Column(name =  "id_traceability", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer traceabilityId;

    @Column(name = "proposal_approved_date", nullable = true)
    private Date proposalApprovedDate;

    @OneToOne
    @JoinColumn(name = "superior_council_minute_id", nullable = true)
    private Minute superiorCouncilMinuteId;

    @Column(name = "men_end_date", nullable = true)
    private Date menEndDate;

    @OneToOne
    @JoinColumn(name = "vice_academic_minute_id", nullable = true)
    private Minute viceAcademicMinuteId;

    @Column(name = "nsaces_date", nullable = true)
    private Date nsacesDate;

    @Column(name = "sinies", nullable = false)
    private String sinies;

    @Column(name = "approved_minute", nullable = true)
    private String approvedMinute;

    @OneToOne
    @JoinColumn(name = "academic_council_minute_id", nullable = true)
    private Minute academicCouncilMinuteId;

}
