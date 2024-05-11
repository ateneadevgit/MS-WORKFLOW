package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "nifs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Nifs {

    @Id
    @Column(name =  "id_nifs", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nifsId;

    @NonNull
    @Column(name = "image", nullable = false)
    private String image;

    @NonNull
    @Column(name = "content", length = 1000, nullable = false)
    private String content;

    @Column(name = "type", nullable = true)
    private Integer type;

    @NonNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = true)
    private String updatedAt;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "nif_father", nullable = true)
    private Nifs nifFather;

}

