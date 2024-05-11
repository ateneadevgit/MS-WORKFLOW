package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "competences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Competence {

    @Id
    @Column(name =  "id_competence", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer competenceId;

    @NonNull
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @NonNull
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @Column(name = "description", length = 300, nullable = true)
    private String description;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NonNull
    @Column(name = "is_nif", nullable = false)
    private Boolean isNif;

}
