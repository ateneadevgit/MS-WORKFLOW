package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Data
@Table(name = "Template")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Template {

    @Id
    @Column(name =  "id_template", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer templateId;

    @NonNull
    @Column(name = "template_name", length = 100, nullable = false)
    private String templateName;

    @Column(name = "template_url", nullable = true)
    private String templateUrl;

    @Column(name = "description", length = 5000, nullable = true)
    private String description;

    @Column(name = "enabled", nullable = true)
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "step_id", nullable = true)
    private Step stepId;

    @ManyToOne
    @JoinColumn(name = "template_father", nullable = true)
    private Template templateFatherId;

}
