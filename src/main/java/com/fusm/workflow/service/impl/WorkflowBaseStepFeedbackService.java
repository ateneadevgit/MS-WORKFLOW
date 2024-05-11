package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.UserAssignedToProgram;
import com.fusm.workflow.entity.*;
import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.INotificationService;
import com.fusm.workflow.model.*;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.model.external.NotificationRequest;
import com.fusm.workflow.model.external.Template;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.*;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Service
public class WorkflowBaseStepFeedbackService implements IWorkflowBaseStepFeedbackService {

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IStepService stepService;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private ICampusService campusService;

    @Autowired
    private IWorkflowBaseRepository workflowBaseRepository;

    @Autowired
    private IProgramService programService;

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private IWorkflowBaseLastActionService workflowBaseLastActionService;

    @Autowired
    private IWorkflowBaseLastActionRepository workflowBaseLastActionRepository;

    @Autowired
    private IStepRoleUserRepository stepRoleUserRepository;

    @Autowired
    private IHistoryService historyService;

    @Autowired
    private ICatalogService catalogService;
    
    @Autowired
    private INotificationService notificationService;
    
    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private ISummaryService summaryService;

    @Autowired
    private IStepRoleUserService stepRoleUserService;


    @Override
    public void evaluateStep(EvaluateStepRequest evaluateStepRequest) throws DocumentException, IOException {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(evaluateStepRequest.getWorkflowId(), evaluateStepRequest.getStepId());
        boolean isSummary = (evaluateStepRequest.getIsSummary() != null) ? evaluateStepRequest.getIsSummary() : false;
        evaluateStepRequest.setIsSummary(isSummary);

        if (workflowBaseStepOptional.isPresent()) {
            WorkflowBaseStep workflowBaseStep = workflowBaseStepOptional.get();
            workflowBaseStepFeedbackRepository.save(
                    createBaseStepFeedback(evaluateStepRequest, workflowBaseStep, evaluateStepRequest.getRoleId())
            );

            if (evaluateStepRequest.getCampusId() != null)  evaluateCampus(evaluateStepRequest, workflowBaseStep);
            if (evaluateStepRequest.getFeedbackStatus().equals("completeness")) disapproveStep(evaluateStepRequest, workflowBaseStep, isSummary);
            if (evaluateStepRequest.getFeedbackStatus().equals("approved")) approveStep(evaluateStepRequest, workflowBaseStep, isSummary);

            if (stepService.allStepsFinished(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId())){
                stepService.updateEditionOfStepsWithoutControl(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());
            }

            if (!evaluateStepRequest.getFeedbackStatus().equals("completeness") && !evaluateStepRequest.getFeedbackStatus().equals("review")) {
                workflowBaseLastActionService
                        .createLastAction(evaluateStepRequest.getRoleId(), sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION), workflowBaseStepOptional.get());

            }
        }
    }

    @Override
    public Integer getStatusOfStep(Integer stepId, Integer workflowBaseStepId, Integer roleId) {
        Integer stepStatus = sharedMethods.getSettingValue(Constant.STEP_ON_PROJECTION);

        List<Integer> evaluators = stepRoleActionRepository.getOnlyEvaluators(stepId);

        if (evaluators.contains(roleId)) {
            stepStatus = workflowBaseStepFeedbackRepository.findAttachStatusByRole(roleId, workflowBaseStepId);
        } else if ((stepId == 19 && roleId.equals(Constant.DC_ROLE)) || (stepId == 19 && roleId.equals(Constant.AC_ROLE))) {
            stepStatus = sharedMethods.getSettingValue(Constant.STEP_ON_PROJECTION);
        } else {
            Integer approvedStatus = sharedMethods.getSettingValue(Constant.STEP_APPROVED);
            Integer onReviewStatus = sharedMethods.getSettingValue(Constant.STEP_ON_REVIEW);
            Integer declinedStatus = sharedMethods.getSettingValue(Constant.STEP_ON_UPDATE);
            Integer onSummaryStatus = sharedMethods.getSettingValue(Constant.STEP_ON_SUMMARY);

            List<Integer> lastEvaluators = stepRoleActionRepository.findLastEvaluatorsOfStep(stepId);
            List<Integer> statusByEvaluator = new ArrayList<>();

            for (Integer evaluator : lastEvaluators) {
                statusByEvaluator.add(workflowBaseStepFeedbackRepository.findAttachStatusByRole(evaluator, workflowBaseStepId));
            }

            if (Collections.frequency(statusByEvaluator, approvedStatus) == lastEvaluators.size()) stepStatus = approvedStatus;
            if (statusByEvaluator.contains(onReviewStatus)) stepStatus  = onReviewStatus;
            if (statusByEvaluator.contains(declinedStatus)) stepStatus = declinedStatus;
            if (statusByEvaluator.contains(onSummaryStatus)) stepStatus = onSummaryStatus;
        }

        return stepStatus;
    }

    @Override
    public void evaluateTraceability(EvaluateTraceability evaluateTraceability, Integer programId) {
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(programId, Constant.STATUS_CREATED);

        for (WorkflowBase workflowBase : workflowBases) {
            workflowBase.setStatus(Constant.STATUS_FINISHED);
            workflowBase.setUpdatedAt(new Date());
            workflowBaseRepository.save(workflowBase);
            disableUserActions(workflowBase, evaluateTraceability.getCreatedBy());
        }

        if (evaluateTraceability.getFeedbackStatus().equals("approved")) approveProgramTraceability(evaluateTraceability, programId);
        if (evaluateTraceability.getFeedbackStatus().equals("declined")) declineProgram(evaluateTraceability, programId);
    }

    @Override
    public boolean hasSummaryStatus(Integer stepId, Integer workflowBaseStepId, Integer roleId) {
        boolean isLastSummary = false;
        List<WorkflowBaseStepFeedback> workflowBaseStepFeedbacks = workflowBaseStepFeedbackRepository.findlastStatus(roleId, workflowBaseStepId);
        if (!workflowBaseStepFeedbacks.isEmpty()) isLastSummary = workflowBaseStepFeedbacks.get(0).getIsSummary();
        return isLastSummary;
    }

    private void disableUserActions(WorkflowBase workflowBase, String createdBy) {
        List<StepRoleUsers> stepRoleUsers = stepRoleUserRepository
                .findAllByWorkflowBaseId_WorkflowBaseIdAndUserEmailAndEnabled(workflowBase.getWorkflowBaseId(), createdBy, true);
        for (StepRoleUsers stepRole : stepRoleUsers) {
            stepRole.setEnabled(false);
            stepRoleUserRepository.save(stepRole);
        }

    }

    private Integer getEvaluationValue(String status) {
        String stepStatus = Constant.STEP_ON_PROJECTION;
        if (status.equals("review")) stepStatus = Constant.STEP_ON_REVIEW;
        if (status.equals("completeness")) stepStatus = Constant.STEP_ON_UPDATE;
        if (status.equals("summary")) stepStatus = Constant.STEP_ON_SUMMARY;
        if (status.equals("approved")) stepStatus = Constant.STEP_APPROVED;
        return sharedMethods.getSettingValue(stepStatus);
    }

    private WorkflowBaseStepFeedback createBaseStepFeedback(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep, Integer roleId) {
       return WorkflowBaseStepFeedback.builder()
                .status(getEvaluationValue(evaluateStepRequest.getFeedbackStatus()))
                .feedback(evaluateStepRequest.getFeedback())
                .roleId(roleId)
                .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                .createdBy(evaluateStepRequest.getCreatedBy())
                .isSummary(evaluateStepRequest.getIsSummary())
                .workflowBaseStepId(workflowBaseStep)
                .build();
    }

    private void evaluateCampus(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep) {
        if (!evaluateStepRequest.getCampusId().isEmpty()) {
            Integer objectId = workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId();
            campusService.disableCampus(evaluateStepRequest.getCampusId(), objectId);
        }
    }

    private void disapproveStep(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep, Boolean isSummary) {
        if (!isSummary) {
            String status = (evaluateStepRequest.getRoleId().equals(Constant.DC_ROLE)) ?
                    Constant.ATTACH_DC_TO_DIR : Constant.ATTACH_VA_TO_DC;
            attachmentService.changeAttachStatus(
                    sharedMethods.getSettingValue(status), evaluateStepRequest.getWorkflowId(), evaluateStepRequest.getStepId(), evaluateStepRequest.getRoleId());
            stepService.updateStepIsSent(workflowBaseStep);
            disableLastAction(evaluateStepRequest);
            Optional<Program> programOptional = programRepository.findById(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());
            programOptional.ifPresent(program -> sendNotification(
                    program.getCreatedBy().split(","),
                    program,
                    Constant.DISAPPROVE_STEP_TEMPLATE,
                    workflowBaseStep.getStepId()));
        } else {
            summaryService.editIsSentOfSummary(workflowBaseStep.getSummaryId());
        }
    }

    private void changeStepToSummary(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep) {
        List<Integer> rolesRelatedWithSummary = stepRoleActionRepository.getRolesRelatedWithSummary(workflowBaseStep.getStepId().getStepId());
        evaluateStepRequest.setIsSummary(true);

        for (Integer roleId : rolesRelatedWithSummary) {
            EvaluateStepRequest initSummary = EvaluateStepRequest.builder()
                    .feedbackStatus("summary")
                    .stepId(workflowBaseStep.getStepId().getStepId())
                    .isSummary(true)
                    .createdBy(evaluateStepRequest.getCreatedBy())
                    .build();
            workflowBaseStepFeedbackRepository.save(
                    createBaseStepFeedback(initSummary, workflowBaseStep, roleId)
            );
        }
    }

    private void approveStep(EvaluateStepRequest evaluateStepRequest, WorkflowBaseStep workflowBaseStep, boolean isSummary) throws DocumentException, IOException {
        if (!isSummary) {
            if (stepService.isStepMandatory(evaluateStepRequest.getStepId())) {
                if (stepService.allMandatoryDone(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(), workflowBaseStep)) {
                    stepService.updateEditionOfSteps(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());
                    if (!workflowBaseStep.getStepId().getHasSummary()) stepService.updateStepToDone(workflowBaseStep);
                }
            } else if (stepService.stepHasAllEvaluation(workflowBaseStep.getStepId().getStepId(), workflowBaseStep.getWorkflowBaseStepId())
                        && workflowBaseStep.getStepId().getHasSummary()) {
                List<Integer> loadFileList = stepRoleActionRepository.getOnlyLoadFile(workflowBaseStep.getStepId().getStepId());
                for (Integer roleId : loadFileList) {
                    workflowBaseStepFeedbackRepository.save(
                            createBaseStepFeedback(evaluateStepRequest, workflowBaseStep, roleId)
                    );
                }
                changeStepToSummary(evaluateStepRequest, workflowBaseStep);
            } else if (stepService.stepHasAllEvaluation(workflowBaseStep.getStepId().getStepId(), workflowBaseStep.getWorkflowBaseStepId())
                    && !workflowBaseStep.getStepId().getHasSummary()) {
                List<Integer> loadFileList = stepRoleActionRepository.getOnlyLoadFile(workflowBaseStep.getStepId().getStepId());
                for (Integer roleId : loadFileList) {
                    workflowBaseStepFeedbackRepository.save(
                            createBaseStepFeedback(evaluateStepRequest, workflowBaseStep, roleId)
                    );
                }
                stepService.updateStepToDone(workflowBaseStep);
            }
            Optional<Program> programOptional = programRepository.findById(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());
            if (programOptional.isPresent()) {
                if (evaluateStepRequest.getRoleId().equals(Constant.AC_ROLE)) {
                    sendNotification(programOptional.get().getCreatedBy().split(","), programOptional.get(), Constant.CA_APPROVE_STEP_TEMPLATE, workflowBaseStep.getStepId());
                }
                if (evaluateStepRequest.getRoleId().equals(Constant.VR_ROLE)) {
                    sendNotification(programOptional.get().getCreatedBy().split(","), programOptional.get(), Constant.VA_APPROVE_STEP_TEMPLATE, workflowBaseStep.getStepId());
                }
            }
        } else if (stepService.hasAllSummaryEvaluation(evaluateStepRequest.getStepId(), evaluateStepRequest.getWorkflowId())) {
            List<StepRoleAction> stepRoleActionList = stepRoleActionRepository
                    .findAllByActionId_ActionIdAndStepId_StepId(sharedMethods.getSettingValue(Constant.CREATE_SUMMARY_ACTION), evaluateStepRequest.getStepId());
            for (StepRoleAction stepRoleAction : stepRoleActionList) {
                createBaseStepFeedback(evaluateStepRequest, workflowBaseStep, stepRoleAction.getRoleId());
            }
            stepService.updateStepToDone(workflowBaseStep);
            createSummaryHistory(workflowBaseStep);
        }
    }

    private void approveProgramTraceability(EvaluateTraceability evaluateTraceability, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            if (program.getIsEnlarge()) {
                Program programFather = program.getProgramFather();
                programFather.setEnabled(false);
                programFather.setUpdatedAt(new Date());
                programRepository.save(programFather);
                createProgramStatus(evaluateTraceability, program, Constant.PROGRAM_APPROVED, Constant.STATUS_PROGRAM);
                createProgramStatus(evaluateTraceability, program, Constant.RENOVATION_APPROVED_STATUS, Constant.STATUS_RENOVATION);
            } else {
                createProgramStatus(evaluateTraceability, program, Constant.PROGRAM_APPROVED, Constant.STATUS_PROGRAM);
            }
            historyService.createCreditAcademicHistory(program);
            historyService.createSubCoreHistory(program);
            historyService.createCoreAndSubCoreHistory(program);
            sendNotification(
                    Stream.concat(Arrays.stream(program.getCreatedBy().split(",")), getQualityUserAssigned(programId).stream())
                            .toArray(String[]::new),
                    program,
                    Constant.APPROVE_PROGRAM_TEMPLATE,
                    null);
        }

    }

    private void declineProgram(EvaluateTraceability evaluateTraceability, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isPresent()) {
            Program program = programOptional.get();

            if (program.getIsEnlarge()) {
                createProgramStatus(evaluateTraceability, program.getProgramFather(), Constant.RENOVATION_STATUS_DECLINED, Constant.STATUS_RENOVATION);
            } else {
                EvaluateProposalModel evaluation = EvaluateProposalModel.builder()
                        .evaluation("declined")
                        .createdBy(evaluateTraceability.getCreatedBy())
                        .roleId(evaluateTraceability.getRoleId())
                        .fileFeedback(evaluateTraceability.getFileFeedback())
                        .build();
                programService.declineOrDisableProgram(evaluation, programId);
            }
        }
    }

    private String fileRoute(FileModel fileModel, String createdBy) {
        return documentManagerService.saveFile(
                DocumentRequest.builder()
                        .documentBytes(fileModel.getFileContent())
                        .documentExtension(fileModel.getFileExtension())
                        .documentVersion("1")
                        .idUser(createdBy)
                        .build()
        );
    }

    private void createProgramStatus(EvaluateTraceability evaluateTraceability, Program program, String status, String StatusType) {
        statusRepository.save(
                Status.builder()
                        .statusId(sharedMethods.getSettingValue(status))
                        .statusType(sharedMethods.getSettingValue(StatusType))
                        .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                        .createdBy(evaluateTraceability.getCreatedBy())
                        .programId(program)
                        .feedbackFileUrl(evaluateTraceability.getFileFeedback() == null ?
                                null : fileRoute(evaluateTraceability.getFileFeedback(), evaluateTraceability.getCreatedBy()))
                        .roleId(evaluateTraceability.getRoleId())
                        .build()
        );
    }

    private void disableLastAction(EvaluateStepRequest evaluateStepRequest) {
        List<WorkflowBaseLastAction> lastActionList = workflowBaseLastActionRepository
                .findLastActionActive(evaluateStepRequest.getWorkflowId(), evaluateStepRequest.getStepId());
        for (WorkflowBaseLastAction lastAction : lastActionList) {
            lastAction.setEnabled(false);
            workflowBaseLastActionRepository.save(lastAction);
        }
    }

    private void createSummaryHistory(WorkflowBaseStep workflowBaseStep) throws DocumentException, IOException {
        if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_CURRICULAR_ASPECTS)) {
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_GRADUATE_PROFILE);
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_RAE);
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_COMPETENCIES);
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_PROGRAM_OBJECTIVES);
            historyService.createCurriculumHistory(workflowBaseStep);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_ACADEMIC_ACTIVITIES)) {
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_ENGLISH_TRAINING);
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_ACADEMIC_FIELD_PROGRAMS);
            historyService.createSyllabusHistory(workflowBaseStep);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_INVESTIGATION_INNOVATION)) {
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_FORMATIVE_RESEARCH);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_EXTERNAL)) {
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_EXTENSION_OR_SOCIAL_PROJECT);
            historyService.createSpecificHistory(workflowBaseStep, Constant.CURRICULUM_INTERNATIONALIZATION);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_EPISTEMOLOGIC_COMPONENT)) {
            historyService.createCurricularComponentHistory(workflowBaseStep, Constant.STEP_EPISTEMOLOGIC_COMPONENT);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_PEDAGOGIC_COMPONENT)) {
            historyService.createCurricularComponentHistory(workflowBaseStep, Constant.STEP_PEDAGOGIC_COMPONENT);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_FORMATIVE_COMPONENT)) {
            historyService.createCurricularComponentHistory(workflowBaseStep, Constant.STEP_FORMATIVE_COMPONENT);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_INTERACTION_COMPONENT)) {
            historyService.createCurricularComponentHistory(workflowBaseStep, Constant.STEP_INTERACTION_COMPONENT);
        } else if (workflowBaseStep.getStepId().getStepId().equals(Constant.STEP_EVALUATION_COMPONENT)) {
            historyService.createCurricularComponentHistory(workflowBaseStep, Constant.STEP_EVALUATION_COMPONENT);
        }
    }

    private void sendNotification(String[] sendTo, Program program, String templateString, Step step) {
        Integer programCreationTemplateId = sharedMethods.getSettingValue(templateString);
        Template programCreationTemplate = notificationService.getTemplate(programCreationTemplateId);
        String faculty = catalogService.getCatalogItemValue(program.getFacultyId());
        String template  = programCreationTemplate.getEmailBody();
        String campus = getCampusValue(campusRepository.findByProgramId_ProgramId(program.getProgramId()));

        template = template.replace(Constant.PROGRAM_FLAG, program.getName());
        template = template.replace(Constant.FACULTY_FLAG, faculty);
        template = template.replace(Constant.URL_FLAG, sharedMethods.getSettingValueOnString(Constant.ATENEA_URL));
        template = template.replace(Constant.CAMPUS_FLAG, campus);
        template = template.replace(Constant.STEP_FLAG, (step != null) ? step.getStepName() : "");

        String subject = programCreationTemplate.getSubject();
        subject = subject.replace(Constant.STEP_FLAG, (step != null) ? step.getStepName() : "");
        subject = subject.replace(Constant.PROGRAM_FLAG, program.getName());
        subject = subject.replace(Constant.FACULTY_FLAG, faculty);
        subject = subject.replace(Constant.CAMPUS_FLAG, campus);

        notificationService.sendNotification(
                NotificationRequest.builder()
                        .sendTo(sendTo)
                        .subject(subject)
                        .body(template)
                        .build()
        );
    }

    private String getCampusValue(List<Integer> campusIdList) {
        StringBuilder campus = new StringBuilder();
        for (Integer id : campusIdList) {
            campus.append(catalogService.getCatalogItemValue(id)).append(", ");
        }
        campus.deleteCharAt(campus.length() - 2);
        return campus.toString();
    }

    private List<String> getQualityUserAssigned(Integer programId) {
        List<UserAssignedToProgram> userAssignedToPrograms = stepRoleUserService
                .getUserRelatedWithProgram(programId, Constant.AC_ROLE);
        List<String> users = new ArrayList<>();
        if (!userAssignedToPrograms.isEmpty()) {
            users.add(userAssignedToPrograms.get(0).getUserEmail());
        }
        return users;
    }

}
