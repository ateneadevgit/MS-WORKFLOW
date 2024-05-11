package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "complementary_core")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplementaryCore {

    @Id
    @Column(name =  "id_complementary_core", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer complementaryCoreId;

    @NonNull
    @Column(name = "raeg", length = 10, nullable = false)
    private String raeg;

}
