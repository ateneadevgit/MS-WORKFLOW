package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Summary")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Summary {

    @Id
    @Column(name =  "id_summary", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer summaryId;

    @NonNull
    @Column(name = "summary", length = 10000, nullable = false)
    private String summary;

    @NonNull
    @Column(name = "is_sent", nullable = false)
    private Boolean isSent;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "updated_at", nullable = true)
    private Date updatedAt;

    @Column(name = "status", nullable = true)
    private Integer status;

}
