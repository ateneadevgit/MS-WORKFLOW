package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "Attach_last_action")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachLastAction {

    @Id
    @Column(name =  "id_attach_last_action", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attachlastActionId;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "send_to", length = 10, nullable = true)
    private String sendTo;

    @Column(name = "status", nullable = true)
    private String status;

    @ManyToOne
    @JoinColumn(name = "attach_id", nullable = false)
    private Attach attachId;

}
