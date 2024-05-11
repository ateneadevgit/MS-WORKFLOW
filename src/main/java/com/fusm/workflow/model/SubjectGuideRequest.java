package com.fusm.workflow.model;

import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectGuideRequest {

    private Integer subjectGuideId;
    private String guideCode;
    private Date approvedDate;
    private String subjectCode;
    private String prerrequisites;
    private String rating;
    private String attendance;
    private String academicPeriod;
    private String courseSchedule;
    private Integer modality;
    private String studentAcademicWork;
    private Integer hoursFreelanceWork;
    private Integer hoursTeacherDirectWork;
    private String syncAsyncWork;
    private String teacherName;
    private String teacherEmail;
    private String teacherScheduleOperation;
    private String teacherProfile;
    private String monitorName;
    private String monitorEmail;
    private String monitorScheduleOperation;
    private String coreConformation;
    private String coreContext;
    private String coreDescription;
    private String learningGeneral;
    private String learningSpecific;
    private String strategies;
    private String generalContent;
    private String evaluationDescription;
    private String evaluationSystem;
    private String bibliographyBasic;
    private String bibliographyLanguage;
    private String bibliographyWeb;
    private String createdBy;
    private List<Integer> programIds;
    private List<Integer> campusIds;
    private List<Integer> modalityIds;
    private List<Integer> facultyIds;
    List<SubjectActivityRequest> activityRequestList;
    private Integer roleId;

}
