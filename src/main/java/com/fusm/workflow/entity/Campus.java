package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Campus")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Campus {

    @Id
    @Column(name =  "id_campus", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campusId;

    @NonNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "resolution", nullable = true)
    private String resolution;

    @Column(name = "resolution_date", nullable = true)
    private Date resolutionDate;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program programId;

}
