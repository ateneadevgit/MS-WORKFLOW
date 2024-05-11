package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Actions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Action {

    @Id
    @Column(name =  "id_action", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actionId;

    @NonNull
    @Column(name = "name", length = 50, nullable = false)
    private String actionName;

    @Column(name = "description", nullable = true)
    private String description;

}
