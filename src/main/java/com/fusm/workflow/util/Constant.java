package com.fusm.workflow.util;

public class Constant {

    public static final long CURRENT_TIME = System.currentTimeMillis() - 5 * 60 * 60 * 1000;

    public static final String PROPOSAL_APPROVED = "PROPOSAL_APPROVED";
    public static final String PROPOSAL_COMPLETENESS = "PROPOSAL_COMPLETENESS";
    public static final String PROPOSAL_DECLINED = "PROPOSAL_DECLINED";
    public static final String PROPOSAL_REQUEST_ON_REVIEW = "PROPOSAL_REQUEST_ON_REVIEW";
    public static final String PROPOSAL_REQUEST_SENT = "PROPOSAL_REQUEST_SENT";
    public static final String STATUS_PROPOSAL = "STATUS_PROPOSAL";
    public static final String STATUS_PROGRAM = "STATUS_PROGRAM";
    public static final String STATUS_RENOVATION = "STATUS_RENOVATION";
    public static final String RENOVATION_REQUESTS_STATUS = "RENOVATION_REQUESTS_STATUS";
    public static final String RENOVATION_ON_RENOVATION_STATUS = "RENOVATION_ON_RENOVATION_STATUS";
    public static final String RENOVATION_APPROVED_STATUS = "RENOVATION_APPROVED_STATUS";
    public static final String RENOVATION_STATUS_DECLINED = "RENOVATION_STATUS_DECLINED";

    public static final String DEFAULT_ANS = "DEFAULT_ANS";

    public static final String PROGRAM_ON_CONSTRUCTION = "PROGRAM_ON_CONSTRUCTION";
    public static final String PROGRAM_DISABLED = "PROGRAM_DISABLED";
    public static final String PROGRAM_DECLINED = "PROGRAM_DECLINED";
    public static final String PROGRAM_APPROVED = "PROGRAM_APPROVED";

    public static final String PROPOSAL_CREATION_TEMPLATE = "PROPOSAL_CREATION_TEMPLATE";
    public static final String REQUEST_PROPOSAL_CREATION_TEMPLATE = "REQUEST_PROPOSAL_CREATION_TEMPLATE";
    public static final String PROPOSAL_REVIEW_TEMPLATE = "PROPOSAL_REVIEW_TEMPLATE";
    public static final String OBSERVATION_PROPOSAL_TEMPLATE = "OBSERVATION_PROPOSAL_TEMPLATE";
    public static final String PROPOSAL_APPROVED_TEMPLATE = "PROPOSAL_APPROVED_TEMPLATE";
    public static final String PROPOSAL_DECLINED_TEMPLATE = "PROPOSAL_DECLINED_TEMPLATE";
    public static final String PROGRAM_DECLINED_TEMPLATE = "PROGRAM_DECLINED_TEMPLATE";
    public static final String PROGRAM_DISABLED_TEMPLATE = "PROGRAM_DISABLED_TEMPLATE";
    public static final String DC_UPGRADE_STEP_TEMPLATE = "DC_UPGRADE_STEP_TEMPLATE";
    public static final String DISAPPROVE_STEP_TEMPLATE = "DISAPPROVE_STEP_TEMPLATE";
    public static final String VA_APPROVE_STEP_TEMPLATE = "VA_APPROVE_STEP_TEMPLATE";
    public static final String CA_APPROVE_STEP_TEMPLATE = "CA_APPROVE_STEP_TEMPLATE";
    public static final String APPROVE_PROGRAM_TEMPLATE = "APPROVE_PROGRAM_TEMPLATE";

    public static final String PROGRAM_FLAG = "--PROGRAM--";
    public static final String TOTAL_SEMESTER_FLAG = "--TOTAL_SEMESTER--";
    public static final String SNIES_FLAG = "--SNIES--";
    public static final String FACULTY_FLAG = "--FACULTY--";
    public static final String URL_FLAG = "--URL--";
    public static final String STEP_FLAG = "--STEP--";

    public static final String ATENEA_URL = "ATENEA_URL";

    public static final String CREATE_PROGRAM_BASE_CONDITION_WORKFLOW = "CREATE_PROGRAM_BASE_CONDITION_WORKFLOW";
    public static final String CREATE_PROGRAM_BASE_PAPER_WORKFLOW = "CREATE_PROGRAM_BASE_PAPER_WORKFLOW";
    public static final String CREATE_PROGRAM_BASE_NO_FORMAL = "CREATE_PROGRAM_BASE_NO_FORMAL";

    public static final String FORMAL_PROGRAM = "FORMAL_PROGRAM";
    public static final String NO_FORMAL_PROGRAM = "NO_FORMAL_PROGRAM";

    public static final String STATUS_CREATED = "created";
    public static final String STATUS_FINISHED = "finished";

    public static final String WORKFLOW_PROGRAM_TYPE = "PROGRAM WORKFLOW";

    public static final String STEP_ON_PROJECTION = "STEP_ON_PROJECTION";
    public static final String STEP_ON_REVIEW = "STEP_ON_REVIEW";
    public static final String STEP_ON_UPDATE = "STEP_ON_UPDATE";
    public static final String STEP_ON_SUMMARY = "STEP_ON_SUMMARY";
    public static final String STEP_APPROVED = "STEP_APPROVED";
    public static final Integer STEP_ON_SUMMARY_VALUE = 29;
    public static final Integer STEP_APPROVED_VALUE = 28;

    public static final String LOAD_FILE_ACTION = "LOAD_FILE_ACTION";
    public static final String EVALUATE_STEP_ACTION = "EVALUATE_STEP_ACTION";
    public static final String VIEW_OWN_FILE_ACTION = "VIEW_OWN_FILE_ACTION";
    public static final String CREATE_SUMMARY_ACTION = "CREATE_SUMMARY_ACTION";
    public static final String EVALUATE_SUMMARY_ACTION = "EVALUATE_SUMMARY_ACTION";
    public static final String SEND_STEP_EVALUATION_ACTION = "SEND_STEP_EVALUATION_ACTION";

    public static final String ATTACH_DC_TO_DIR = "ATTACH_DC_TO_DIR";
    public static final String ATTACH_DIR_TO_DC = "ATTACH_DIR_TO_DC";
    public static final String ATTACH_APPROVED = "ATTACH_APPROVED";
    public static final String ATTACH_DECLINED = "ATTACH_DECLINED";
    public static final String ATTACH_ON_PROCESS = "ATTACH_ON_PROCESS";
    public static final String ATTACH_DC_TO_VA = "ATTACH_DC_TO_VA";
    public static final String ATTACH_VA_TO_DC = "ATTACH_VA_TO_DC";

    public static final String SYLLABUS_PDF_TEMPLATE = "SYLLABUS_PDF_TEMPLATE";

    public static final Integer DC_ROLE = 5;
    public static final Integer AC_ROLE = 6;
    public static final Integer VR_ROLE = 2;
    public static final Integer DIR_ROLE = 1;
    public static final Integer COOR_ROLE = 7;
    public static final Integer TEACH_ROLE = 9;
    public static final Integer STU_ROLE = 4;
    public static final Integer STU_NF_ROLE = 10;

    public static final String CORE_FLAG = "--CORE--";
    public static final String SUBJECT_FLAG = "--SUBJECT--";
    public static final String SUBJECT_CODE_FLAG = "--SUBJECT_CODE--";
    public static final String SUBCORE_FLAG = "--SUBCORE--";
    public static final String CREDIT_FLAG = "--CREDIT--";
    public static final String CAMPUS_FLAG = "--CAMPUS--";
    public static final String MODALITY_FLAG = "--MODALITY--";
    public static final String SEMESTER_FLAG = "--SEMESTER--";
    public static final String SEMESTER_TABLE_FLAG = "--SEMESTER-TABLE--";
    public static final String LAST_SUBJECT_FLAG = "--LAST_SUBJECT--";
    public static final String SUBCORES_DATA_FLAG = "--SUBCORES_DATA--";
    public static final String LAST_SUBJECT_CSS = "last-right";
    public static final String CAT_FLAG = "--CAT--";
    public static final String CINE_FLAG = "--CINE--";
    public static final String NBC_FLAG = "--NBC--";
    public static final String IS_FACED_FLAG = "--IS_FACED--";
    public static final String MODALITY_OBSERVATION_FLAG = "--MODALITY_OBSERVATION--";
    public static final String IS_DISTANCE_FLAG = "--IS_DISTANCE--";
    public static final String IS_ONLINE_FLAG = "--IS_ONLINE--";
    public static final String LEVEL_FORMATION_FLAG = "--LEVEL_FORMATION--";
    public static final String PREREQUIREMENT_FLAG = "--PREREQUIREMENT--";
    public static final String CREDIT_NUMBERS_FLAG = "--CREDIT_NUMBERS--";
    public static final String IS_TEORIC_FLAG = "--IS_TEORIC--";
    public static final String SIGNATURE_TYPE_FLAG = "--SIGNATURE_TYPE--";
    public static final String IS_PRACTICAL_FLAG = "--IS_PRACTICAL--";
    public static final String IS_TEORIC_PRACTICAL_FLAG = "--IS_TEORIC_PRACTICAL--";
    public static final String ATTENDANCE_FLAG = "--ATTENDANCE--";
    public static final String CONFORMATION_FLAG = "--CONFORMATION--";
    public static final String CONTEXT_FLAG = "--CONTEXT--";
    public static final String DESCRIPTION_FLAG = "--DESCRIPTION--";
    public static final String GENERAL_FLAG = "--GENERAL--";
    public static final String SPECIFIC_FLAG = "--SPECIFIC--";
    public static final String CONTENT_FLAG = "--CONTENT--";
    public static final String PRACTICES_FLAG = "--PRACTICES--";
    public static final String BIBLIOGRAPHY_BASIC_FLAG = "--BIBLIOGRAPHY_BASIC--";
    public static final String BIBLIOGRAPHY_LANGUAGE_FLAG = "--BIBLIOGRAPHY_LANGUAGE--";
    public static final String BIBLIOGRAPHY_WEB_FLAG = "--BIBLIOGRAPHY_WEB--";
    public static final String CODE_FLAG = "--CODE--";
    public static final String APPROVED_DATE_FLAG = "--APPROVED_DATE--";
    public static final String HOUR_INTERACTION_TEACHER_FLAG = "--HOUR_INTERACTION_TEACHER--";
    public static final String HOUR_SELF_WORK_FLAG = "--HOUR_SELF_WORK--";
    public static final String RATING_FLAG = "--RATING--";
    public static final String ACADEMIC_PERIOD_FLAG = "--ACADEMIC_PERIOD--";
    public static final String COURSE_SCHEDULE_FLAG = "--COURSE_SCHEDULE--";
    public static final String MODALITY_SUBJECT_FLAG = "--MODALITY_SUBJECT--";
    public static final String STUDENT_ACADEMIC_WORK_FLAG = "--STUDENT_ACADEMIC_WORK--";
    public static final String HOUR_FREELANCE_WORK_FLAG = "--HOUR_FREELANCE_WORK--";
    public static final String HOUR_DIRECT_TEACHER_FLAG = "--HOUR_DIRECT_TEACHER--";
    public static final String SYNC_WORK_FLAG = "--SYNC_WORK--";
    public static final String TEACHER_NAME_FLAG = "--TEACHER_NAME--";
    public static final String TECHAER_EMAIL_FLAG = "--TECHAER_EMAIL--";
    public static final String TEACHER_SCHEDULE_FLAG = "--TEACHER_SCHEDULE--";
    public static final String TEACHER_PROFILE_FLAG = "--TEACHER_PROFILE--";
    public static final String MONITOR_NAME_FLAG = "--MONITOR_NAME--";
    public static final String MONITOR_EMAIL_FLAG = "--MONITOR_EMAIL--";
    public static final String MONITOR_SCHEDULE_FLAG = "--MONITOR_SCHEDULE--";
    public static final String STRATEGIES_FLAG = "--STRATEGIES--";
    public static final String STRATEGY_DESCRIPTION_FLAG = "--STRATEGY_DESCRIPTION--";
    public static final String STRATEGY_SYSTEM_FLAG = "--STRATEGY_SYSTEM--";
    public static final String ACTIVITIES_FLAG = "--ACTIVITIES--";

    public static final String DISTANCE_MODALITY = "DISTANCE_MODALITY";
    public static final String ONLINE_MODALITY = "ONLINE_MODALITY";
    public static final String FACED_MODALITY = "FACED_MODALITY";

    public static final String SUBJECT_TEORIC = "SUBJECT_TEORIC";
    public static final String SUBEJCT_PRACTIC = "SUBEJCT_PRACTIC";
    public static final String SUBJECT_TEORIC_PRACTIC = "SUBJECT_TEORIC_PRACTIC";

    public static final String EPISTEMOLOGIC_COMPONENT = "EPISTEMOLOGIC_COMPONENT";
    public static final String PEDAGOGIC_COMPONENT = "PEDAGOGIC_COMPONENT";
    public static final String FORMATIVE_COMPONENT = "FORMATIVE_COMPONENT";
    public static final String INTERACTION_COMPONENT = "INTERACTION_COMPONENT";
    public static final String EVALUATION_COMPONENT = "EVALUATION_COMPONENT";

    public static final int MODULE_BANK_OF_PROBLEMS = 12;
    public static final int MODULE_TECHNICAL_AND_TECHNOLOGICAL_PROGRAMS = 17;
    public static final int MODULE_UPDATE_AUTHORIZATIONS = 18;
    public static final int MODULE_CURRICULAR_PROCESS_PROFILES = 1;
    public static final int MODULE_CURRICULAR_COMPONENTS = 5;
    public static final int MODULE_ACADEMIC_CREDITS = 9;
    public static final int MODULE_RAE = 2;
    public static final int MODULE_EXTENSION_OR_SOCIAL_PROJECTION = 14;
    public static final int MODULE_PLAN_OF_STUDY_ORGANIZATION = 6;
    public static final int MODULE_ENGLISH_TRAINING = 10;
    public static final int MODULE_INTERNATIONALIZATION = 15;
    public static final int MODULE_TRAINING_OBJECTIVES = 3;
    public static final int MODULE_SYLLABUS = 7;
    public static final int MODULE_CORES_AND_SUBCORES = 11;
    public static final int MODULE_ACADEMIC_FIELD_PROGRAMS = 16;
    public static final int MODULE_COMPETENCIES = 4;
    public static final int MODULE_CURRICULAR_OUTCOMES = 8;
    public static final int MODULE_FORMATIVE_RESEARCH = 13;

    public static final int CURRICULUM_GRADUATE_PROFILE = 42;
    public static final int CURRICULUM_RAE = 43;
    public static final int CURRICULUM_COMPETENCIES = 44;
    public static final int CURRICULUM_PROGRAM_OBJECTIVES = 45;
    public static final int CURRICULUM_ENGLISH_TRAINING = 46;
    public static final int CURRICULUM_ACADEMIC_FIELD_PROGRAMS = 47;
    public static final int CURRICULUM_FORMATIVE_RESEARCH = 48;
    public static final int CURRICULUM_EXTENSION_OR_SOCIAL_PROJECT = 49;
    public static final int CURRICULUM_INTERNATIONALIZATION = 50;

    public static final int STEP_CURRICULAR_ASPECTS = 4;
    public static final int STEP_ACADEMIC_ACTIVITIES = 13;
    public static final int STEP_INVESTIGATION_INNOVATION = 14;
    public static final int STEP_EXTERNAL = 15;
    public static final int STEP_EPISTEMOLOGIC_COMPONENT = 7;
    public static final int STEP_FORMATIVE_COMPONENT = 9;
    public static final int STEP_INTERACTION_COMPONENT = 10;
    public static final int STEP_EVALUATION_COMPONENT = 11;
    public static final int STEP_PEDAGOGIC_COMPONENT = 8;

    public static final int CURRICULUM_TEMPLATE = 1;
    public static final int SEMESTER_TEMPLATE = 2;
    public static final int SUBJECT_TEMPLATE = 3;
    public static final int SILABO_TEMPLATE = 5;
    public static final int SUBJECT_GUIDE = 4;

    public static final int NIFS_DEFINITION = 106;
    public static final int NIFS_PURPOSE = 107;
    public static final int NIFS_RAGI = 108;

    public static final int NIFS_CORE = 316;

    public static final int SYLLABUS_PROGRAM = 111;
    public static final int SYLLABUS_CAMPUS = 110;
    public static final int SYLLABUS_TYPE_FORMATION = 109;
    public static final int SYLLABUS_FACULTY = 112;
    public static final int SYLLABUS_MODALITY = 113;

    public static final int PROBLEM_APPROVED = 114;
    public static final int PROBLEM_COMPLETNESS = 115;
    public static final int PROBLEM_SENT = 116;
    public static final int PROBLEM_SENT_DISABLE = 117;
    public static final int PROBLEM_SENT_UPDATE = 118;
    public static final int PROBLEM_DISABLED = 119;

    public static final String STATUS_APPROVED = "approved";
    public static final String STATUS_COMPLETENESS = "completeness";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_SENT_REVIEW = "review";
    public static final String STATUS_RQ = "rq";
    public static final String STATUS_RQ_DISABLE = "rq_disable";
    public static final String STATUS_RQ_UPDATE = "rq_update";
    public static final String STATUS_DISABLED = "disabled";
    public static final String STATUS_DECLINED = "declined";
    public static final String STATUS_CLEAN = "clean";

    public static final Integer STATUS_GUIDE_APPROVED = 125;
    public static final Integer STATUS_GUIDE_COMPLETENESS = 126;
    public static final Integer STATUS_GUIDE_ON_REVIEW = 127;
    public static final Integer STATUS_GUIDE_CLEAN = 133;

    public static final Integer CURRICULUM_CHAT_PROGRAM_SUBJECT_TYPE = 131;
    public static final Integer CURRICULUM_CHAT_ACTIVITY_PLAN_TYPE = 131;

    public static final Integer STATUS_RENOVATION_GUIDE_ON_REQUEST = 134;
    public static final Integer STATUS_RENOVATION_GUIDE_APPROVED = 135;
    public static final Integer STATUS_RENOVATION_GUIDE_DECLINED = 136;
    public static final Integer STATUS_RENOVATION_DONE = 150;

    public static final Integer CREATE_GUIDE_TYPE = 128;
    public static final Integer UPDATE_GUIDE_TYPE = 129;
    public static final Integer CREATE_ACTIVITY_TYPE = 130;

    public static final Integer STEP_DEFAULT_CONTROL = 1;

    public static final String[] EVALUATION_ROLES = new String[] {
            "vicerrector.atenea@gmail.com",
            "calidad.fs.atenea@gmail.com"
    };

    public static final String[] QUALITY_ROLES = new String[] {
            "calidad.fs.atenea@gmail.com"
    };

}
