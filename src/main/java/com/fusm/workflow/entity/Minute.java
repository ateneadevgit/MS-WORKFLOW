package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "minute")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Minute {

    @Id
    @Column(name =  "id_minute", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer minuteId;

    @NonNull
    @Column(name = "minute", nullable = false)
    private String minute;

    @NonNull
    @Column(name = "minute_date", nullable = false)
    private Date minuteDate;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

}
