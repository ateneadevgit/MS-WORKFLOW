package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.entity.*;
import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.ISinuService;
import com.fusm.workflow.model.*;
import com.fusm.workflow.model.external.CodeModel;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.*;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SubjectGuideService implements ISubjectGuideService {

    @Autowired
    private ISubjectGuideRepository subjectGuideRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;

    @Autowired
    private ISubjectActivityService subjectActivityService;

    @Autowired
    private ISubjectGuideObjectRepository subjectGuideObjectRepository;

    @Autowired
    private ISubjectActivityRepository subjectActivityRepository;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private IProgramService programService;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private IModalityRepository modalityRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private ISubjectGuideStatusRepository subjectGuideStatusRepository;

    @Autowired
    private ISinuService sinuService;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void createSubjectGuide(SubjectGuideRequest subjectGuideRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        if (curriculumOptional.isPresent()) {
            SubjectGuide subjectGuide = subjectGuideRepository.save(
                    SubjectGuide.builder()
                            .guideCode(subjectGuideRequest.getGuideCode())
                            .approvedDate(subjectGuideRequest.getApprovedDate())
                            .subjectCode(subjectGuideRequest.getSubjectCode())
                            .prerrequisites(subjectGuideRequest.getPrerrequisites())
                            .rating(subjectGuideRequest.getRating())
                            .attendance(subjectGuideRequest.getAttendance())
                            .academicPeriod(subjectGuideRequest.getAcademicPeriod())
                            .courseSchedule(subjectGuideRequest.getCourseSchedule())
                            .modality(subjectGuideRequest.getModality())
                            .studentAcademicWork(subjectGuideRequest.getStudentAcademicWork())
                            .hoursFreelanceWork(subjectGuideRequest.getHoursFreelanceWork())
                            .hoursTeacherDirectWork(subjectGuideRequest.getHoursTeacherDirectWork())
                            .syncAsyncWork(subjectGuideRequest.getSyncAsyncWork())
                            .teacherName(subjectGuideRequest.getTeacherName())
                            .teacherEmail(subjectGuideRequest.getTeacherEmail())
                            .teacherScheduleOperation(subjectGuideRequest.getTeacherScheduleOperation())
                            .teacherProfile(subjectGuideRequest.getTeacherProfile())
                            .monitorName(subjectGuideRequest.getMonitorName())
                            .monitorEmail(subjectGuideRequest.getMonitorEmail())
                            .monitorScheduleOperation(subjectGuideRequest.getMonitorScheduleOperation())
                            .coreConformation(subjectGuideRequest.getCoreConformation())
                            .coreContext(subjectGuideRequest.getCoreContext())
                            .coreDescription(subjectGuideRequest.getCoreDescription())
                            .learningGeneral(subjectGuideRequest.getLearningGeneral())
                            .learningSpecific(subjectGuideRequest.getLearningSpecific())
                            .strategies(subjectGuideRequest.getStrategies())
                            .generalContent(subjectGuideRequest.getGeneralContent())
                            .evaluationDescription(subjectGuideRequest.getEvaluationDescription())
                            .evaluationSystem(subjectGuideRequest.getEvaluationSystem())
                            .bibliographyBasic(subjectGuideRequest.getBibliographyBasic())
                            .bibliographyLanguage(subjectGuideRequest.getBibliographyLanguage())
                            .bibliographyWeb(subjectGuideRequest.getBibliographyWeb())
                            .createdBy(subjectGuideRequest.getCreatedBy())
                            .createdAt(new Date())
                            .curriculumId(curriculumOptional.get())
                            .build()
            );
            subjectActivityService.createSubjectActivity(subjectGuideRequest.getActivityRequestList(), subjectGuide.getSubjectGuideId());

            if (!subjectGuideRequest.getRoleId().equals(Constant.VR_ROLE)) {
                saveStatus(subjectGuide, Constant.CREATE_GUIDE_TYPE, Constant.STATUS_GUIDE_ON_REVIEW, subjectGuideRequest.getCreatedBy());
            } else {
                saveSyllabusObject(subjectGuideRequest.getFacultyIds(), subjectGuide, Constant.SYLLABUS_FACULTY);
                saveSyllabusObject(subjectGuideRequest.getProgramIds(), subjectGuide, Constant.SYLLABUS_PROGRAM);
                saveSyllabusObject(subjectGuideRequest.getModalityIds(), subjectGuide, Constant.SYLLABUS_MODALITY);
                saveSyllabusObject(subjectGuideRequest.getCampusIds(), subjectGuide, Constant.SYLLABUS_CAMPUS);
            }
        }
    }

    @Override
    public void updateSubjectGuide(SubjectGuideRequest subjectGuideRequest, Integer curriculumId) {
        List<SubjectGuide> subjectGuideList = subjectGuideRepository.findAllByCurriculumId(curriculumId);
        if (!subjectGuideList.isEmpty()) {
            SubjectGuide subjectGuide = subjectGuideList.get(0);
            subjectGuide.setGuideCode(subjectGuideRequest.getGuideCode());
            subjectGuide.setApprovedDate(subjectGuideRequest.getApprovedDate());
            subjectGuide.setSubjectCode(subjectGuideRequest.getSubjectCode());
            subjectGuide.setPrerrequisites(subjectGuideRequest.getPrerrequisites());
            subjectGuide.setRating(subjectGuideRequest.getRating());
            subjectGuide.setAttendance(subjectGuideRequest.getAttendance());
            subjectGuide.setAcademicPeriod(subjectGuideRequest.getAcademicPeriod());
            subjectGuide.setCourseSchedule(subjectGuideRequest.getCourseSchedule());
            subjectGuide.setModality(subjectGuideRequest.getModality());
            subjectGuide.setStudentAcademicWork(subjectGuideRequest.getStudentAcademicWork());
            subjectGuide.setHoursFreelanceWork(subjectGuideRequest.getHoursFreelanceWork());
            subjectGuide.setHoursTeacherDirectWork(subjectGuideRequest.getHoursTeacherDirectWork());
            subjectGuide.setSyncAsyncWork(subjectGuideRequest.getSyncAsyncWork());
            subjectGuide.setTeacherName(subjectGuideRequest.getTeacherName());
            subjectGuide.setTeacherEmail(subjectGuideRequest.getTeacherEmail());
            subjectGuide.setTeacherScheduleOperation(subjectGuideRequest.getTeacherScheduleOperation());
            subjectGuide.setTeacherProfile(subjectGuideRequest.getTeacherProfile());
            subjectGuide.setMonitorName(subjectGuideRequest.getMonitorName());
            subjectGuide.setMonitorEmail(subjectGuideRequest.getMonitorEmail());
            subjectGuide.setMonitorScheduleOperation(subjectGuideRequest.getMonitorScheduleOperation());
            subjectGuide.setCoreConformation(subjectGuideRequest.getCoreConformation());
            subjectGuide.setCoreContext(subjectGuideRequest.getCoreContext());
            subjectGuide.setCoreDescription(subjectGuideRequest.getCoreDescription());
            subjectGuide.setLearningGeneral(subjectGuideRequest.getLearningGeneral());
            subjectGuide.setLearningSpecific(subjectGuideRequest.getLearningSpecific());
            subjectGuide.setStrategies(subjectGuideRequest.getStrategies());
            subjectGuide.setGeneralContent(subjectGuideRequest.getGeneralContent());
            subjectGuide.setEvaluationDescription(subjectGuideRequest.getEvaluationDescription());
            subjectGuide.setEvaluationSystem(subjectGuideRequest.getEvaluationSystem());
            subjectGuide.setBibliographyBasic(subjectGuideRequest.getBibliographyBasic());
            subjectGuide.setBibliographyLanguage(subjectGuideRequest.getBibliographyLanguage());
            subjectGuide.setBibliographyWeb(subjectGuideRequest.getBibliographyWeb());
            subjectGuide.setUpdatedAt(new Date());
            subjectGuideRepository.save(subjectGuide);

            if (!subjectGuideRequest.getRoleId().equals(Constant.VR_ROLE)) {
                saveStatus(subjectGuide, Constant.CREATE_GUIDE_TYPE, Constant.STATUS_GUIDE_ON_REVIEW, subjectGuideRequest.getCreatedBy());
            } else {
                updateSyllabusObject(subjectGuideRequest.getFacultyIds(), Constant.SYLLABUS_FACULTY, subjectGuide);
                updateSyllabusObject(subjectGuideRequest.getCampusIds(), Constant.SYLLABUS_CAMPUS, subjectGuide);
                updateSyllabusObject(subjectGuideRequest.getProgramIds(), Constant.SYLLABUS_PROGRAM, subjectGuide);
                updateSyllabusObject(subjectGuideRequest.getModalityIds(), Constant.SYLLABUS_MODALITY, subjectGuide);
            }
        }
    }

    @Override
    public SubjectGuideRequest getSubjectGuide(SearchTeacher searchTeacher, Integer curriculumId) {
        SubjectGuideRequest subjectGuideRequest = new SubjectGuideRequest();
        List<SubjectGuide> subjectGuideList = subjectGuideRepository.findAllByCurriculumId(curriculumId);
        if (!subjectGuideList.isEmpty()) {
            if (searchTeacher.getRoleId().equals(Constant.DIR_ROLE)) {
                subjectGuideRequest = buildSubjectGuideRequest(subjectGuideList.get(0), false);
            } else if (subjectGuideList.get(0).getCurriculumId().getComplementaryNifs() != null) {
                subjectGuideRequest = buildSubjectGuideRequest(subjectGuideList.get(0), true);
            } else if (subjectGuideList.get(0).getCurriculumId().getComplementaryNifs() == null &&
                    searchTeacher.getCreatedBy().equals(subjectGuideList.get(0).getCreatedBy())) {
                subjectGuideRequest = buildSubjectGuideRequest(subjectGuideList.get(0), false);
            } else {
                List<SubjectGuideStatus> subjectGuideStatus = subjectGuideStatusRepository
                        .findLastStatus(subjectGuideList.get(0).getSubjectGuideId());
                if (!subjectGuideStatus.isEmpty()) {
                    if (subjectGuideStatus.get(0).getIdStatus().equals(Constant.STATUS_GUIDE_APPROVED)) {
                        subjectGuideRequest = buildSubjectGuideRequest(subjectGuideList.get(0), false);
                    }
                }
            }
            subjectGuideRequest.setActivityRequestList(getActivities(searchTeacher, subjectGuideList.get(0).getSubjectGuideId()));
        }
        return subjectGuideRequest;
    }

    @Override
    public String getSubjectGuidePdf(SearchTeacher searchTeacher, Integer curriculumId) throws DocumentException {
        String template = documentManagerService.getTemplate(Constant.SUBJECT_GUIDE);
        List<SubjectGuide> subjectGuideList = subjectGuideRepository.findAllByCurriculumId(curriculumId);
        if (!subjectGuideList.isEmpty()) {
            if (subjectGuideList.get(0).getCurriculumId().getComplementaryNifs() != null) {
                template = buildPdf(searchTeacher, template, subjectGuideList.get(0), true);
            } else if (subjectGuideList.get(0).getCurriculumId().getComplementaryNifs() == null &&
                    searchTeacher.getCreatedBy().equals(subjectGuideList.get(0).getCreatedBy())) {
                template = buildPdf(searchTeacher, template, subjectGuideList.get(0), false);
            }else {
                List<SubjectGuideStatus> subjectGuideStatus = subjectGuideStatusRepository
                        .findLastStatus(subjectGuideList.get(0).getSubjectGuideId());
                if (subjectGuideStatus.get(0).getIdStatus().equals(Constant.STATUS_GUIDE_APPROVED)) {
                    template = buildPdf(searchTeacher, template, subjectGuideList.get(0), false);
                }
            }
        }
        if (template.contains("--")) {
            template = cleanTemplate(template);
        }
        return pdfService.savePdf(template);
    }

    @Override
    public SubjectGuidePreLoad getPreloadData(Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        SubjectGuidePreLoad subjectGuidePreLoad = new SubjectGuidePreLoad();
        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();
            Optional<Program> programOptional = programRepository.findById(curriculum.getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId());
            if (programOptional.isPresent()) {
                subjectGuidePreLoad = SubjectGuidePreLoad.builder()
                        .core(curriculum.getCurrirriculumFatherId().getName())
                        .subCore(curriculum.getName())
                        .campusIds(campusRepository.findByProgramId_ProgramId(programOptional.get().getProgramId()))
                        .facultyIds(programOptional.get().getFacultyId())
                        .programIds(programOptional.get().getProgramId())
                        .modalityIds(modalityRepository.findByProgramId_ProgramId(programOptional.get().getProgramId()))
                        .subjectCode(curriculum.getComplementarySubject().getCode())
                        .creditNumber(curriculum.getNumberCredits())
                        .hourInteractionTeacher(curriculum.getComplementarySubject().getHoursInteractionTeacher())
                        .hourSelfWork(curriculum.getComplementarySubject().getHourSelfWork())
                        .build();
            }
        }
        return subjectGuidePreLoad;
    }

    @Override
    public void evaluateSubejctGuide(EvaluateGuide evaluateGuide, Integer subjectGuideId) {
        Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
        subjectGuideOptional.ifPresent(subjectGuide ->
                saveStatus(
                        subjectGuide,
                        Constant.CREATE_GUIDE_TYPE,
                        getStatus(evaluateGuide.getFeedbackStatus()),
                        evaluateGuide.getCreatedBy()));
    }

    private Integer getStatus(String status) {
        Integer statusGuide = Constant.STATUS_GUIDE_ON_REVIEW;
        if (status.equals(Constant.STATUS_APPROVED)) statusGuide = Constant.STATUS_GUIDE_APPROVED;
        if (status.equals(Constant.STATUS_COMPLETENESS)) statusGuide = Constant.STATUS_GUIDE_COMPLETENESS;
        return statusGuide;
    }

    private void saveStatus(SubjectGuide subjectGuide, Integer type, Integer status, String createdBy) {
        subjectGuideStatusRepository.save(
                SubjectGuideStatus.builder()
                        .idStatus(status)
                        .statusType(type)
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .subjectGuideId(subjectGuide)
                        .build()
        );
    }

    private List<SubjectActivityRequest> getActivities(SearchTeacher searchTeacher, Integer subjectGuideId) {
        List<SubjectGuideActivity> subjectGuideActivityList = new ArrayList<>();

        if (searchTeacher.getTeacher() == null) {
            Optional<SubjectGuide> subjectGuideOptional = subjectGuideRepository.findById(subjectGuideId);
            if (subjectGuideOptional.isPresent()) {
                if ((searchTeacher.getRoleId().equals(Constant.STU_ROLE) || searchTeacher.getRoleId().equals(Constant.STU_NF_ROLE))
                    && subjectGuideOptional.get().getCurriculumId().getComplementaryNifs() != null) {
                    subjectGuideActivityList = subjectActivityRepository
                            .findAllBySubjectGuideId(subjectGuideId, sharedMethods.getPeriod());
                } else if (searchTeacher.getRoleId().equals(Constant.STU_ROLE) || searchTeacher.getRoleId().equals(Constant.STU_NF_ROLE)) {
                    subjectGuideActivityList = subjectActivityRepository
                            .findAllActivitiesApproved(
                                    subjectGuideId,
                                    getTeacherByStudentAndSubjectCode(subjectGuideOptional.get(), searchTeacher.getCreatedBy()),
                                    sharedMethods.getPeriod());
                } else if (subjectGuideOptional.get().getCurriculumId().getComplementaryNifs() != null) {
                    subjectGuideActivityList = subjectActivityRepository
                            .findAllBySubjectGuideId(subjectGuideId, sharedMethods.getPeriod());
                } else {
                    subjectGuideActivityList = subjectActivityRepository.findAllActivitiesApproved(
                            subjectGuideId,
                            getTeachersWithApprovedActivities(subjectGuideOptional.get()),
                            sharedMethods.getPeriod());
                }
            }
        } else{
            subjectGuideActivityList = subjectActivityRepository
                    .findAllActivitiesApproved(subjectGuideId, searchTeacher.getTeacher().split(" "), sharedMethods.getPeriod());
        }
        return mapSubjectActivity(subjectGuideActivityList);
    }

    private List<Integer> getGuideObject(Integer type, Integer syllabusId) {
        return subjectGuideObjectRepository.findAllByEnabledAndObjectTypeAndSubjectGuideId_SubjectGuideId
                (true, type, syllabusId).stream().map(
                SubjectGuideObject::getObjectId
        ).toList();
    }

    private void saveSyllabusObject(List<Integer> objectsIds, SubjectGuide subjectGuide, Integer type) {
        if (objectsIds != null) {
            for (Integer id : objectsIds) saveObject(id, type, subjectGuide);
        }
    }

    private void saveObject(Integer id, Integer type, SubjectGuide subjectGuide) {
        subjectGuideObjectRepository.save(
                SubjectGuideObject.builder()
                        .objectId(id)
                        .objectType(type)
                        .subjectGuideId(subjectGuide)
                        .enabled(true)
                        .build()
        );
    }

    private void updateSyllabusObject(List<Integer> objectIds, Integer type, SubjectGuide subjectGuide) {
        List<SubjectGuideObject> subjectGuideObjectList = subjectGuideObjectRepository
                .findAllByObjectTypeAndSubjectGuideId_SubjectGuideId(type, subjectGuide.getSubjectGuideId());

        for (SubjectGuideObject subjectGuideObject : subjectGuideObjectList) {
            if (objectIds.contains(subjectGuideObject.getObjectId()) && subjectGuideObject.getEnabled().equals(true)) {
                objectIds.remove(subjectGuideObject.getObjectId());
            } else if (subjectGuideObject.getEnabled().equals(false) && objectIds.contains(subjectGuideObject.getObjectId())) {
                subjectGuideObject.setEnabled(true);
                subjectGuideObjectRepository.save(subjectGuideObject);
                objectIds.remove(subjectGuideObject.getObjectId());
            } else if (!objectIds.contains(subjectGuideObject.getObjectId())) {
                subjectGuideObject.setEnabled(false);
                subjectGuideObjectRepository.save(subjectGuideObject);
                objectIds.remove(subjectGuideObject.getObjectId());
            }
        }
        saveSyllabusObject(objectIds, subjectGuide, type);
    }

    private String buildPdf(SearchTeacher searchTeacher, String template, SubjectGuide subjectGuide, Boolean isNif) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        Optional<Program> programOptional = (isNif) ? Optional.empty() :
                programRepository.findById(subjectGuide.getCurriculumId().getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId());

        template = template.replace(Constant.CODE_FLAG, subjectGuide.getGuideCode());
        template = template.replace(Constant.APPROVED_DATE_FLAG, format.format(subjectGuide.getApprovedDate()));
        template = template.replace(Constant.CORE_FLAG, subjectGuide.getCurriculumId().getCurrirriculumFatherId().getName());
        template = template.replace(Constant.SUBJECT_FLAG, subjectGuide.getCurriculumId().getName());
        template = template.replace(Constant.FACULTY_FLAG, (isNif) ? getObjectValue(getGuideObject(Constant.SYLLABUS_FACULTY, subjectGuide.getSubjectGuideId()))
                : ((programOptional.isPresent()) ?
                getObjectValue(List.of(programOptional.get().getFacultyId())) : ""));
        template = template.replace(Constant.PROGRAM_FLAG, (isNif) ? programService.getProgramName(getGuideObject(Constant.SYLLABUS_PROGRAM, subjectGuide.getSubjectGuideId()))
                : (programOptional.map(Program::getName).orElse("")));
        template = template.replace(Constant.CAMPUS_FLAG, (isNif) ? getObjectValue(getGuideObject(Constant.SYLLABUS_CAMPUS, subjectGuide.getSubjectGuideId()))
                : ((programOptional.isPresent()) ?
                getObjectValue(campusRepository.findByProgramId_ProgramId(programOptional.get().getProgramId())) : ""));
        template = template.replace(Constant.MODALITY_FLAG, (isNif) ? getObjectValue(getGuideObject(Constant.SYLLABUS_MODALITY, subjectGuide.getSubjectGuideId()))
                : ((programOptional.isPresent()) ?
                getObjectValue(modalityRepository.findByProgramId_ProgramId(programOptional.get().getProgramId())) : ""));
        template = template.replace(Constant.SUBJECT_CODE_FLAG, (subjectGuide.getCurriculumId().getComplementarySubject() != null) ?
                subjectGuide.getCurriculumId().getComplementarySubject().getCode() : "-");
        template = template.replace(Constant.PREREQUIREMENT_FLAG, subjectGuide.getPrerrequisites());
        template = template.replace(Constant.CREDIT_FLAG, subjectGuide.getCurriculumId().getNumberCredits().toString());
        template = template.replace(Constant.HOUR_INTERACTION_TEACHER_FLAG, ((subjectGuide.getCurriculumId().getComplementarySubject() != null) ?
                subjectGuide.getCurriculumId().getComplementarySubject().getHoursInteractionTeacher().toString() : "0"));
        template = template.replace(Constant.HOUR_SELF_WORK_FLAG, ((subjectGuide.getCurriculumId().getComplementarySubject() != null) ?
                subjectGuide.getCurriculumId().getComplementarySubject().getHourSelfWork().toString() : "0"));
        template = template.replace(Constant.RATING_FLAG, subjectGuide.getRating());
        template = template.replace(Constant.ATTENDANCE_FLAG, subjectGuide.getAttendance());
        template = template.replace(Constant.ACADEMIC_PERIOD_FLAG, subjectGuide.getAcademicPeriod());
        template = template.replace(Constant.COURSE_SCHEDULE_FLAG, subjectGuide.getCourseSchedule());
        template = template.replace(Constant.MODALITY_SUBJECT_FLAG, getObjectValue(List.of(subjectGuide.getModality())));
        template = template.replace(Constant.STUDENT_ACADEMIC_WORK_FLAG, subjectGuide.getStudentAcademicWork());
        template = template.replace(Constant.HOUR_FREELANCE_WORK_FLAG, subjectGuide.getHoursFreelanceWork().toString());
        template = template.replace(Constant.HOUR_DIRECT_TEACHER_FLAG, subjectGuide.getHoursTeacherDirectWork().toString());
        template = template.replace(Constant.TEACHER_NAME_FLAG, subjectGuide.getTeacherName());
        template = template.replace(Constant.TECHAER_EMAIL_FLAG, subjectGuide.getTeacherEmail());
        template = template.replace(Constant.TEACHER_SCHEDULE_FLAG, subjectGuide.getTeacherScheduleOperation());
        template = template.replace(Constant.TEACHER_PROFILE_FLAG, subjectGuide.getTeacherProfile());
        template = template.replace(Constant.MONITOR_NAME_FLAG, subjectGuide.getMonitorName());
        template = template.replace(Constant.MONITOR_EMAIL_FLAG, subjectGuide.getMonitorEmail());
        template = template.replace(Constant.MONITOR_SCHEDULE_FLAG, subjectGuide.getMonitorScheduleOperation());
        template = template.replace(Constant.CONFORMATION_FLAG, subjectGuide.getCoreConformation());
        template = template.replace(Constant.CONTEXT_FLAG, subjectGuide.getCoreContext());
        template = template.replace(Constant.DESCRIPTION_FLAG, subjectGuide.getCoreDescription());
        template = template.replace(Constant.GENERAL_FLAG, subjectGuide.getLearningGeneral());
        template = template.replace(Constant.SPECIFIC_FLAG, subjectGuide.getLearningSpecific());
        template = template.replace(Constant.STRATEGIES_FLAG, subjectGuide.getStrategies());
        template = template.replace(Constant.CONTENT_FLAG, subjectGuide.getGeneralContent());
        template = template.replace(Constant.SYNC_WORK_FLAG, subjectGuide.getSyncAsyncWork());
        template = template.replace(Constant.STRATEGY_DESCRIPTION_FLAG, subjectGuide.getEvaluationDescription());
        template = template.replace(Constant.STRATEGY_SYSTEM_FLAG, subjectGuide.getEvaluationSystem());
        template = template.replace(Constant.BIBLIOGRAPHY_BASIC_FLAG, subjectGuide.getBibliographyBasic());
        template = template.replace(Constant.BIBLIOGRAPHY_LANGUAGE_FLAG, subjectGuide.getBibliographyLanguage());
        template = template.replace(Constant.BIBLIOGRAPHY_WEB_FLAG, subjectGuide.getBibliographyWeb());
        template = template.replace(Constant.ACTIVITIES_FLAG, buildActivities(searchTeacher, subjectGuide.getSubjectGuideId(), format));

        return template;
    }

    public String getObjectValue(List<Integer> campusIdList) {
        StringBuilder campus = new StringBuilder();
        for (Integer id : campusIdList) {
            campus.append(catalogService.getCatalogItemValue(id)).append(", ");
        }
        campus.deleteCharAt(campus.length() - 2);
        return campus.toString();
    }

    private String buildActivities(SearchTeacher searchTeacher, Integer subjectGuideId, SimpleDateFormat format) {
        StringBuilder response = new StringBuilder();
        String aux = "";
        int i = 1;

        for (SubjectActivityRequest activity : getActivities(searchTeacher, subjectGuideId)) {
            aux = "<tr> " +
                    "    <td>" + i + "</td>" +
                    "    <td>" + format.format(activity.getActivityDate()) + "</td>" +
                    "    <td>" + activity.getResult() + "</td>" +
                    "    <td>" + activity.getTopic() + "</td>" +
                    "    <td>" + activity.getSyncActivities() + "</td>" +
                    "    <td>" + activity.getPreviusActivities() + "</td>" +
                    "    <td>" + activity.getStrategies() + "</td>" +
                    "</tr>";
            response.append(aux);
            i++;
        }

        return response.toString();
    }

    private List<SubjectActivityRequest> mapSubjectActivity(List<SubjectGuideActivity> subjectGuideActivityList) {
        return subjectGuideActivityList.stream().map(
                subjectActivity -> SubjectActivityRequest.builder()
                        .subjectActivityId(subjectActivity.getSubjectActivityId())
                        .activityDate(subjectActivity.getActivityDate())
                        .result(subjectActivity.getResult())
                        .topic(subjectActivity.getTopic())
                        .syncActivities(subjectActivity.getSyncActivities())
                        .previusActivities(subjectActivity.getPreviusActivities())
                        .strategies(subjectActivity.getStrategies())
                        .url(subjectActivity.getUrl())
                        .enabled(subjectActivity.getEnabled())
                        .session(subjectActivity.getSession())
                        .canUpdate(subjectActivity.getCanUpdate())
                        .build()
        ).toList();
    }

    private String[] getTeachersWithApprovedActivities(SubjectGuide subjectGuide) {
        List<SubjectTeacherDto> teacherList = getTeachersBySubject(
                subjectGuide.getCurriculumId().getCurriculumId());
        List<String> teacherActivityApproved = new ArrayList<>();
        for (SubjectTeacherDto subjectTeacherDto : teacherList) {
            if (subjectTeacherDto.getIdStatus() != null) {
                if (subjectTeacherDto.getIdStatus().equals(Constant.STATUS_GUIDE_APPROVED)) {
                    teacherActivityApproved.add(subjectTeacherDto.getUserId());
                }
            }
        }
        return teacherActivityApproved.toArray(new String[0]);
    }

    public List<SubjectTeacherDto> getTeachersBySubject(Integer subjectId) {
        List<SubjectTeacherDto> subjectTeacherDtos = new ArrayList<>();
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(subjectId);
        if (curriculumOptional.isPresent()) {
            if (curriculumOptional.get().getComplementarySubject() != null) {
                subjectTeacherDtos = curriculumRepository.findAllTeacherBySubject(
                        getUserIds(curriculumOptional.get().getComplementarySubject().getCode()), subjectId, null, null);
            }
        }
        return subjectTeacherDtos;
    }

    private String getUserIds(String subjectCode) {
        StringBuilder ids = new StringBuilder();
        List<String> userIds = sinuService.getTeacherIdBySubjectCode(subjectCode).getCode();
        for (String userId : userIds) {
            ids.append(userId).append(", ");
        }
        if (!ids.isEmpty()) ids.delete(ids.length() - 2, ids.length());
        return ids.toString();
    }

    private SubjectGuideRequest buildSubjectGuideRequest(SubjectGuide subjectGuide, Boolean isNif) {
        Optional<Program> programOptional = (isNif) ? Optional.empty() :
                programRepository.findById(subjectGuide.getCurriculumId().getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId());
        return SubjectGuideRequest.builder()
                .subjectGuideId(subjectGuide.getSubjectGuideId())
                .guideCode(subjectGuide.getGuideCode())
                .approvedDate(subjectGuide.getApprovedDate())
                .subjectCode(subjectGuide.getSubjectCode())
                .prerrequisites(subjectGuide.getPrerrequisites())
                .rating(subjectGuide.getRating())
                .attendance(subjectGuide.getAttendance())
                .academicPeriod(subjectGuide.getAcademicPeriod())
                .courseSchedule(subjectGuide.getCourseSchedule())
                .modality(subjectGuide.getModality())
                .studentAcademicWork(subjectGuide.getStudentAcademicWork())
                .hoursFreelanceWork(subjectGuide.getHoursFreelanceWork())
                .hoursTeacherDirectWork(subjectGuide.getHoursTeacherDirectWork())
                .syncAsyncWork(subjectGuide.getSyncAsyncWork())
                .teacherName(subjectGuide.getTeacherName())
                .teacherEmail(subjectGuide.getTeacherEmail())
                .teacherScheduleOperation(subjectGuide.getTeacherScheduleOperation())
                .teacherProfile(subjectGuide.getTeacherProfile())
                .monitorName(subjectGuide.getMonitorName())
                .monitorEmail(subjectGuide.getMonitorEmail())
                .monitorScheduleOperation(subjectGuide.getMonitorScheduleOperation())
                .coreConformation(subjectGuide.getCoreConformation())
                .coreContext(subjectGuide.getCoreContext())
                .coreDescription(subjectGuide.getCoreDescription())
                .learningGeneral(subjectGuide.getLearningGeneral())
                .learningSpecific(subjectGuide.getLearningSpecific())
                .strategies(subjectGuide.getStrategies())
                .generalContent(subjectGuide.getGeneralContent())
                .evaluationDescription(subjectGuide.getEvaluationDescription())
                .evaluationSystem(subjectGuide.getEvaluationSystem())
                .bibliographyBasic(subjectGuide.getBibliographyBasic())
                .bibliographyLanguage(subjectGuide.getBibliographyLanguage())
                .bibliographyWeb(subjectGuide.getBibliographyWeb())
                .createdBy(subjectGuide.getCreatedBy())
                .programIds((isNif) ? getGuideObject(Constant.SYLLABUS_PROGRAM, subjectGuide.getSubjectGuideId())
                        : List.of(subjectGuide.getCurriculumId().getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId()))
                .modalityIds((isNif) ? getGuideObject(Constant.SYLLABUS_MODALITY, subjectGuide.getSubjectGuideId())
                        : ((programOptional.isPresent()) ?
                        modalityRepository.findByProgramId_ProgramId(programOptional.get().getProgramId()) : new ArrayList<>()))
                .facultyIds((isNif) ? getGuideObject(Constant.SYLLABUS_FACULTY, subjectGuide.getSubjectGuideId())
                        : (programOptional.map(program -> List.of(program.getFacultyId())).orElseGet(ArrayList::new)))
                .campusIds((isNif) ? getGuideObject(Constant.SYLLABUS_CAMPUS, subjectGuide.getSubjectGuideId())
                        : ((programOptional.isPresent()) ?
                        campusRepository.findByProgramId_ProgramId(programOptional.get().getProgramId()) : new ArrayList<>()))
                .build();
    }

    private String cleanTemplate(String template) {
        String patron = "--[^-]+--";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(template);
        return matcher.replaceAll("");
    }

    private String[] getTeacherByStudentAndSubjectCode(SubjectGuide subjectGuide, String createdBy) {
        CodeModel codeModel = sinuService.getTeacherOfStudentBYSubjectCode(
                UserGroup.builder()
                        .createdBy(createdBy)
                        .subjectCode(subjectGuide.getCurriculumId().getComplementarySubject().getCode())
                        .build()
        );
        return codeModel.getCode().toArray(new String[0]);
    }

}
