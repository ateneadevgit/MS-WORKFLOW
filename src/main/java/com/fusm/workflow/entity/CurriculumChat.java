package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "curriculum_chat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumChat {

    @Id
    @Column(name =  "id_curriculum_chat", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer curriculumChatId;

    @NonNull
    @Column(name = "sender", length = 50, nullable = false)
    private String sender;

    @Column(name = "receptor", length = 50, nullable = true)
    private String receptor;

    @NonNull
    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @NonNull
    @Column(name = "object_type", nullable = false)
    private Integer objectType;

    @NonNull
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @NonNull
    @Column(name = "review", length = 1000, nullable = false)
    private String review;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @NonNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "reply_to", nullable = true)
    private CurriculumChat replyTo;

    @NonNull
    @Column(name = "is_read", nullable = true)
    private Boolean isRead;

}
