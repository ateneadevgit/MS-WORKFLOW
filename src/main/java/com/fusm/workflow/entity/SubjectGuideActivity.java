package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "subject_guide_activity")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGuideActivity {

    @Id
    @Column(name =  "id_subject_activity", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subjectActivityId;

    @NonNull
    @Column(name = "activity_date", nullable = false)
    private Date activityDate;

    @NonNull
    @Column(name = "session", nullable = false)
    private Integer session;

    @NonNull
    @Column(name = "result", length = 1000, nullable = false)
    private String result;

    @NonNull
    @Column(name = "topic", length = 1000, nullable = false)
    private String topic;

    @NonNull
    @Column(name = "sync_activities", length = 1000, nullable = false)
    private String syncActivities;

    @NonNull
    @Column(name = "previus_activities", length = 1000, nullable = false)
    private String previusActivities;

    @NonNull
    @Column(name = "strategies", length = 1000, nullable = false)
    private String strategies;

    @Column(name = "url", length = 300, nullable = true)
    private String url;

    @Column(name = "teacher", length = 50, nullable = true)
    private String teacher;

    @NonNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NonNull
    @Column(name = "period", length = 10, nullable = false)
    private String period;

    @NonNull
    @Column(name = "can_update", nullable = false)
    private Boolean canUpdate;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "subject_guide_id", nullable = false)
    private SubjectGuide subjectGuideId;

}
