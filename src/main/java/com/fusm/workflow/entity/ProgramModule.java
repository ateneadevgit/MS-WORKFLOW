package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "program_module")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramModule {

    @Id
    @Column(name =  "id_program_module", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programModuleId;

    @NonNull
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NonNull
    @Column(name = "module_type", nullable = false)
    private Integer moduleType;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NonNull
    @Column(name = "module_order", nullable = false)
    private Integer moduleOrder;

    @NonNull
    @Column(name = "is_enlarge", nullable = false)
    private Boolean isEnlarge;

    @NonNull
    @Column(name = "allow_edition", nullable = false)
    private Boolean allowEdition;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}
