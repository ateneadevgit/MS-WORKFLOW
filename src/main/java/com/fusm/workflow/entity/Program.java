package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Programs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Program {

    @Id
    @Column(name =  "id_program", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programId;

    @NonNull
    @Column(name = "logo", length = 100, nullable = false)
    private String logo;

    @NonNull
    @Column(name = "cover", length = 100, nullable = false)
    private String cover;

    @NonNull
    @Column(name = "name", length = 200, nullable = false)
    private String name;

    @NonNull
    @Column(name = "id_faculty", nullable = false)
    private Integer facultyId;

    @NonNull
    @Column(name = "id_type_formation", nullable = false)
    private Integer typeFormationId;

    @NonNull
    @Column(name = "id_level_formation", nullable = false)
    private Integer levelFormationId;

    @NonNull
    @Column(name = "id_ans", nullable = false)
    private Integer ansId;

    @NonNull
    @Column(name = "file_url", length = 100, nullable = false)
    private String fileUrl;

    @NonNull
    @Column(name = "development_date", nullable = false)
    private String developmentDate;

    @NonNull
    @Column(name = "is_enlarge", nullable = false)
    private Boolean isEnlarge;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "disabled_date", nullable = true)
    private Date disabledDate;

    @Column(name = "id_registry_type", nullable = true)
    private Integer registryTypeId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "program_father", nullable = true)
    private Program programFather;

    @OneToOne
    @JoinColumn(name = "traceability_id", nullable = true)
    private Traceability traceabilityId;

}
