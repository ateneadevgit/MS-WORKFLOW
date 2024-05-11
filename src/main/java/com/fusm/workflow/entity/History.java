package com.fusm.workflow.entity;

import com.fusm.workflow.entity.Program;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "History")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @Id
    @Column(name =  "id_history", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer historyId;

    @NonNull
    @Column(name = "value", length = 10000, nullable = false)
    private String value;

    @Column(name = "object_type", nullable = true)
    private Integer objectType;

    @NonNull
    @Column(name = "version", nullable = false)
    private Double version;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "minute", nullable = true)
    private String minute;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program programId;

    @ManyToOne
    @JoinColumn(name = "program_module_id", nullable = false)
    private ProgramModule programModuleId;

}
