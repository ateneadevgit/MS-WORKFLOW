package com.fusm.workflow.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Builder
@Entity
@Data
@Table(name = "subject_guide")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGuide {

    @Id
    @Column(name =  "id_subject_guide", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer subjectGuideId;

    @NonNull
    @Column(name = "guide_code", length = 20, nullable = false)
    private String guideCode;

    @NonNull
    @Column(name = "approved_date", nullable = false)
    private Date approvedDate;

    @Column(name = "subject_code", length = 20, nullable = true)
    private String subjectCode;

    @NonNull
    @Column(name = "prerrequisites", length = 1000, nullable = false)
    private String prerrequisites;

    @NonNull
    @Column(name = "rating", length = 50, nullable = false)
    private String rating;

    @NonNull
    @Column(name = "attendance", length = 1000, nullable = false)
    private String attendance;

    @NonNull
    @Column(name = "academic_period", length = 1000, nullable = false)
    private String academicPeriod;

    @NonNull
    @Column(name = "course_schedule", length = 1000, nullable = false)
    private String courseSchedule;

    @NonNull
    @Column(name = "modality", nullable = false)
    private Integer modality;

    @NonNull
    @Column(name = "student_academic_work", length = 1000, nullable = false)
    private String studentAcademicWork;

    @NonNull
    @Column(name = "hours_freelance_work", nullable = false)
    private Integer hoursFreelanceWork;

    @NonNull
    @Column(name = "hours_teacher_direct_work", nullable = false)
    private Integer hoursTeacherDirectWork;

    @NonNull
    @Column(name = "sync_async_work", length = 1000, nullable = false)
    private String syncAsyncWork;

    @Column(name = "teacher_name", length = 200, nullable = true)
    private String teacherName;

    @Column(name = "teacher_email", length = 200, nullable = true)
    private String teacherEmail;

    @Column(name = "teacher_schedule_operation", length = 200, nullable = true)
    private String teacherScheduleOperation;

    @Column(name = "teacher_profile", length = 1000, nullable = true)
    private String teacherProfile;

    @Column(name = "monitor_name", length = 200, nullable = true)
    private String monitorName;

    @Column(name = "monitor_email", length = 200, nullable = true)
    private String monitorEmail;

    @Column(name = "monitor_schedule_operation", length = 1000, nullable = true)
    private String monitorScheduleOperation;

    @NonNull
    @Column(name = "core_conformation", length = 1000, nullable = false)
    private String coreConformation;

    @NonNull
    @Column(name = "core_context", length = 1000, nullable = false)
    private String coreContext;

    @NonNull
    @Column(name = "core_description", length = 1000, nullable = false)
    private String coreDescription;

    @NonNull
    @Column(name = "learning_general", length = 1000, nullable = false)
    private String learningGeneral;

    @NonNull
    @Column(name = "learning_specific", length = 1000, nullable = false)
    private String learningSpecific;

    @NonNull
    @Column(name = "strategies", length = 1000, nullable = false)
    private String strategies;

    @NonNull
    @Column(name = "general_content", length = 1000, nullable = false)
    private String generalContent;

    @NonNull
    @Column(name = "evaluation_description", length = 1000, nullable = false)
    private String evaluationDescription;

    @NonNull
    @Column(name = "evaluation_system", length = 1000, nullable = false)
    private String evaluationSystem;

    @NonNull
    @Column(name = "bibliography_basic", length = 1000, nullable = false)
    private String bibliographyBasic;

    @NonNull
    @Column(name = "bibliography_language", length = 1000, nullable = false)
    private String bibliographyLanguage;

    @NonNull
    @Column(name = "bibliography_web", length = 1000, nullable = false)
    private String bibliographyWeb;

    @NonNull
    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @NonNull
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updatedAt", nullable = true)
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "curriculum_id", nullable = false)
    private Curriculum curriculumId;

}
