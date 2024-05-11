package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Modality")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Modality {

    @Id
    @Column(name =  "id_modality", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer modalityId;

    @NonNull
    @Column(name = "value", nullable = false)
    private Integer value;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program programId;

}
