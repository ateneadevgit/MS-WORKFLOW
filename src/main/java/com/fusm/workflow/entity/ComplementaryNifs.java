package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "complementary_nifs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplementaryNifs {

    @Id
    @Column(name =  "id_complementary_nifs", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complementaryNifsId;

    @NonNull
    @Column(name = "rae", length = 10000, nullable = false)
    private String rae;

    @NonNull
    @Column(name = "competences", length = 10000, nullable = false)
    private String competences;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

}

