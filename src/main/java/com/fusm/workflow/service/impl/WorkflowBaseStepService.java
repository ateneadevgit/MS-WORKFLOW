package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.UserAssignedToProgram;
import com.fusm.workflow.dto.WorkflowStepDto;
import com.fusm.workflow.entity.*;
import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.INotificationService;
import com.fusm.workflow.external.ISinuService;
import com.fusm.workflow.model.*;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.model.external.NotificationRequest;
import com.fusm.workflow.model.external.Template;
import com.fusm.workflow.model.external.UserByRoleModel;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.*;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class WorkflowBaseStepService implements IWorkflowBaseStepService {

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private IWorkflowBaseRepository workflowBaseRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IAttachRepository attachRepository;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private IWorkflowBaseStepFeedbackService workflowBaseStepFeedbackService;

    @Autowired
    private IActionService actionService;

    @Autowired
    private ITemplateService templateService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IWorkflowBaseLastActionService workflowBaseLastActionService;

    @Autowired
    private IWorkflowBaseLastActionRepository workflowBaseLastActionRepository;

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IWorkflowBaseStepReviewService workflowBaseStepReviewService;

    @Autowired
    private ISinuService sinuService;

    @Autowired
    private IStepRoleUserService stepRoleUserService;

    @Autowired
    private IAttachLastActionRepository attachLastActionRepository;


    @Override
    public WorkflowStepModel getStepsOfWorkflowByRole(Integer objectId, WorkflowStepRequest workflowStepRequest) {
        String workflow = defineWorkflowType(workflowStepRequest.getWorkflowType());
        Integer workflowId = sharedMethods.getSettingValue(workflow);

        List<WorkflowBase> workflowBase = workflowBaseRepository
                .findAllByWorkflowId_WorkflowIdAndWorkflowObjectId(workflowId, objectId);
        Integer workflowBaseId = workflowBase.get(0).getWorkflowBaseId();

        List<WorkflowStepDto> steps =  workflowBaseStepRepository
                .findStepsByWorkflow(objectId, workflowId, workflowStepRequest.getRoleId());
        List<StepModel> stepList = new ArrayList<>();

        int lastModified = getLastModifiedStep(workflowBaseId);
        Integer sendEvaluation = sharedMethods.getSettingValue(Constant.SEND_STEP_EVALUATION_ACTION);
        Integer stepEvaluated = sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION);

        for (WorkflowStepDto stepDto : steps) {
            List<ActionModel> actions = actionService.getActionsByRoleAndStep(workflowStepRequest.getRoleId(), stepDto.getStepId());
            stepList.add(
                    StepModel.builder()
                            .stepId(stepDto.getStepId())
                            .stepName(stepDto.getStepName())
                            .orderId(stepDto.getStepOrder())
                            .controlId(stepDto.getControlId())
                            .isEditable(stepDto.getIsEditable())
                            .isSent(getLasAction(workflowStepRequest.getRoleId(), stepDto.getStepId(), workflowBaseId, sendEvaluation))
                            .hasEvaluated(getLasAction(workflowStepRequest.getRoleId(), stepDto.getStepId(), workflowBaseId, stepEvaluated))
                            .isDone(stepDto.getIsDone())
                            .canCreateSummary(workflowBaseStepFeedbackService.hasSummaryStatus(stepDto.getStepId(), stepDto.getWorkflowStepId(), workflowStepRequest.getRoleId()))
                            .isLastTouched((stepDto.getStepId().equals(lastModified)))
                            .status(workflowBaseStepFeedbackService.getStatusOfStep(stepDto.getStepId(), stepDto.getWorkflowStepId(), workflowStepRequest.getRoleId()))
                            .template(templateService.getTemplatesByStep(stepDto.getStepId()))
                            .attachment(attachmentService.hasViewAttachment(actions, stepDto, workflowBaseId, workflowStepRequest.getRoleId()))
                            .hasAlreadySummary(getHasSummary(stepDto.getWorkflowStepId()))
                            .actions(actions)
                            .build()
            );

        }

        return WorkflowStepModel.builder()
                .workflowBaseId(workflowBaseId)
                .steps(stepList)
                .build();
    }

    @Override
    public void loadAttachment(StepAttachRequest stepAttachRequest) throws DocumentException, IOException {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(stepAttachRequest.getWorkflowId(), stepAttachRequest.getStepId());

        if (workflowBaseStepOptional.isPresent()) {
            WorkflowBaseStep workflowBaseStep = workflowBaseStepOptional.get();
            Integer status = getAttachStatus(stepAttachRequest.getRoleId());

            for (AttachRequest attach : stepAttachRequest.getAttachments()) {
                String fileRoute = fileRoute(attach, stepAttachRequest.getCreatedBy());
                Attach father = null;
                if (attach.getFatherId() != null) {
                    Optional<Attach> attachFatherOptional = attachRepository.findById(attach.getFatherId());
                    if (attachFatherOptional.isPresent()) father = attachFatherOptional.get();
                }
                attachRepository.save(
                        Attach.builder()
                                .name(attach.getName())
                                .url(fileRoute)
                                .enabled(true)
                                .version(attach.getVersion())
                                .status(status)
                                .roleId(stepAttachRequest.getRoleId())
                                .isSent(false)
                                .isOriginal(false)
                                .isDeclined(false)
                                .createdAt(new Date())
                                .createdBy(stepAttachRequest.getCreatedBy())
                                .attachFather(father)
                                .workflowBaseStepId(workflowBaseStepOptional.get())
                                .feedback(attach.getFeedback())
                                .build()
                );
            }

            setGeneralStatus(workflowBaseStep, stepAttachRequest.getStepId(), stepAttachRequest.getCreatedBy());
            //if (stepAttachRequest.getRoleId().equals(Constant.DC_ROLE)) setDefaultStatus(workflowBaseStep, stepAttachRequest);

            workflowBaseStep.setUpdatedAt(new Date());
            workflowBaseStepRepository.save(workflowBaseStep);
            //workflowBaseLastActionService.createLastAction(stepAttachRequest.getRoleId(), sharedMethods.getSettingValue(Constant.LOAD_FILE_ACTION), workflowBaseStep);
        }

    }

    private void setDefaultStatus(WorkflowBaseStep workflowBaseStep, SendEvaluationRequest sendEvaluationRequest) throws DocumentException, IOException {
        workflowBaseStepFeedbackService.evaluateStep(
                EvaluateStepRequest.builder()
                        .feedbackStatus("approved")
                        .feedback("-")
                        .workflowId(workflowBaseStep.getWorkflowBaseId().getWorkflowBaseId())
                        .stepId(workflowBaseStep.getStepId().getStepId())
                        .roleId(Constant.DC_ROLE)
                        .createdBy(sendEvaluationRequest.getCreatedBy())
                        .isSummary(false)
                        .build()
        );
    }

    private void setGeneralStatus(WorkflowBaseStep workflowBaseStep, Integer stepId, String createdBy){
        List<StepRoleAction> evaluators = stepRoleActionRepository
                .findAllByActionId_ActionIdAndStepId_StepId(sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION), stepId);

        for (StepRoleAction stepRoleAction : evaluators) {
            workflowBaseStepFeedbackRepository.save(
                    WorkflowBaseStepFeedback.builder()
                            .status(sharedMethods.getSettingValue(Constant.STEP_ON_PROJECTION))
                            .roleId(stepRoleAction.getRoleId())
                            .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                            .createdBy(createdBy)
                            .isSummary(false)
                            .workflowBaseStepId(workflowBaseStep)
                            .build()
            );
        }
    }

    private Integer getAttachStatus(Integer roleId){
        String status = Constant.ATTACH_DC_TO_VA;
        if (roleId.equals(Constant.DIR_ROLE)) status = Constant.ATTACH_DIR_TO_DC;
        return sharedMethods.getSettingValue(status);
    }

    @Override
    public void disableAttachment(Integer attachmentId) {
        Optional<Attach> attachOptional = attachRepository.findById(attachmentId);
        if (attachOptional.isPresent()) {
            Attach attach = attachOptional.get();
            if (attach.getAttachFather() == null) {
                List<Attach> attachChild = attachRepository.findAllByAttachFather_AttachId(attachmentId);
                for (Attach child : attachChild) {
                    child.setEnabled(false);
                    attachRepository.save(child);
                }
            }
            attach.setEnabled(false);
            attachRepository.save(attach);
        }
    }

    @Override
    public void sendStepToEvaluation(SendEvaluationRequest sendEvaluationRequest) throws DocumentException, IOException {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(sendEvaluationRequest.getWorkflowId(), sendEvaluationRequest.getStepId());
        Integer status = (sendEvaluationRequest.getRoleId().equals(Constant.DIR_ROLE)) ?
            sharedMethods.getSettingValue(Constant.ATTACH_DIR_TO_DC) : sharedMethods.getSettingValue(Constant.ATTACH_DC_TO_VA);

        if (workflowBaseStepOptional.isPresent()) {
            WorkflowBaseStep workflowBaseStep = workflowBaseStepOptional.get();
            if (sendEvaluationRequest.getRoleId().equals(Constant.DC_ROLE)) {
                setDefaultStatus(workflowBaseStep, sendEvaluationRequest);
            }
            Integer evaluationAction = sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION);

            for (Integer attachId : sendEvaluationRequest.getAttachment()) {
                sendAttachToEvaluation(attachId, status, evaluationAction, sendEvaluationRequest);
            }

            workflowBaseStep.setIsSent(true);
            workflowBaseStep.setUpdatedAt(new Date());
            workflowBaseStepRepository.save(workflowBaseStep);
            workflowBaseLastActionService
                    .createLastAction(sendEvaluationRequest.getRoleId(), sharedMethods.getSettingValue(Constant.SEND_STEP_EVALUATION_ACTION), workflowBaseStep);

            Optional<Program> program = programRepository.findById(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId());

            program.ifPresent(value -> sendNotification(
                    value.getFacultyId(),
                    getEvaluatorsUser(value.getProgramId()),
                    value,
                    Constant.DC_UPGRADE_STEP_TEMPLATE,
                    workflowBaseStep.getStepId()
            ));
        }

    }

    private void createEvaluationFeedback(SendEvaluationRequest sendEvaluationRequest, String feedback, Integer roleId) throws DocumentException, IOException {
        workflowBaseStepFeedbackService.evaluateStep(
                EvaluateStepRequest.builder()
                        .feedbackStatus("review")
                        .feedback(feedback)
                        .workflowId(sendEvaluationRequest.getWorkflowId())
                        .stepId(sendEvaluationRequest.getStepId())
                        .createdBy(sendEvaluationRequest.getCreatedBy())
                        .roleId(roleId)
                        .isSummary(false)
                        .build());
    }

    private String defineWorkflowType(String type) {
        String typeResult = "";
        if (type.equals("condition")) typeResult = Constant.CREATE_PROGRAM_BASE_CONDITION_WORKFLOW;
        if (type.equals("paper")) typeResult = Constant.CREATE_PROGRAM_BASE_PAPER_WORKFLOW;
        if (type.equals("no-formal")) typeResult = Constant.CREATE_PROGRAM_BASE_NO_FORMAL;
        return typeResult;
    }

    private void sendAttachToEvaluation(Integer attachId, Integer status, Integer evaluationAction, SendEvaluationRequest sendEvaluationRequest) throws DocumentException, IOException {
        Optional<Attach> attachOptional = attachRepository.findById(attachId);

        if (attachOptional.isPresent()) {
            Attach attach = attachOptional.get();
            attach.setIsSent(true);
            if (status != null) attach.setStatus(status);
            attachRepository.save(attach);

            List<StepRoleAction> actualRole = stepRoleActionRepository
                    .findAllByRoleIdAndActionId_ActionIdAndStepId_StepId(
                            sendEvaluationRequest.getRoleId(), evaluationAction, sendEvaluationRequest.getStepId());

            int orderStep = (actualRole.isEmpty()) ?
                    1 : actualRole.get(0).getStepOrder() + 1;

            List<StepRoleAction> toSendEvaluation = stepRoleActionRepository
                    .findAllByActionId_ActionIdAndStepId_StepIdAndStepOrder(
                            evaluationAction, sendEvaluationRequest.getStepId(), orderStep);
            List<StepRoleAction> shareWith = new ArrayList<>(toSendEvaluation);

            if (attach.getRoleId().equals(sendEvaluationRequest.getRoleId())) {
                List<StepRoleAction> onlyLoadFiles = stepRoleActionRepository
                        .findAllByActionId_ActionIdAndStepId_StepId(
                                sharedMethods.getSettingValue(Constant.LOAD_FILE_ACTION), sendEvaluationRequest.getStepId());
                shareWith.addAll(onlyLoadFiles);
            }

            String roleToSend = getRoleToReview(shareWith);

            createAttachLastAction(
                    sendEvaluationRequest.getCreatedBy(),
                    roleToSend,
                    null,
                    attach
                    );

            if (attach.getAttachFather() == null) {
                List<Attach> attachChild = attachRepository.findAllByAttachFather_AttachId(attach.getAttachId());
                for (Attach child : attachChild) {
                    if (child.getEnabled()) {
                        child.setIsSent(true);
                        if (status != null) child.setStatus(status);
                        attachRepository.save(child);
                        createAttachLastAction(
                                sendEvaluationRequest.getCreatedBy(),
                                roleToSend,
                                null,
                                child
                        );
                    }
                }

                for (StepRoleAction stepRoleAction : toSendEvaluation) {
                    createEvaluationFeedback(sendEvaluationRequest, attachOptional.get().getFeedback(), stepRoleAction.getRoleId());
                }
            }
            if (attach.getFeedback() != null) {
                workflowBaseStepReviewService.createReview(
                        ReviewRequest.builder()
                                .workflowId(sendEvaluationRequest.getWorkflowId())
                                .stepId(sendEvaluationRequest.getStepId())
                                .review(attach.getFeedback())
                                .createdBy(attach.getCreatedBy())
                                .roleId(attach.getRoleId())
                                .replyTo(null)
                                .build()
                );
            }
        }
    }

    private String fileRoute(AttachRequest fileModel, String createdBy) {
        return documentManagerService.saveFile(
                DocumentRequest.builder()
                        .documentBytes(fileModel.getBytes())
                        .documentExtension(fileModel.getExtension())
                        .documentVersion(fileModel.getVersion())
                        .idUser(createdBy)
                        .build()
        );
    }

    private String getRoleToReview(List<StepRoleAction> stepRoleAction) {
        StringBuilder roleReview = new StringBuilder();
        for (StepRoleAction roleAction : stepRoleAction) {
            roleReview.append(roleAction.getRoleId()).append(" , ");
        }
        return roleReview.toString();
    }

    private void createAttachLastAction(
            String createdBy, String sendTo, String status, Attach attach) {
        attachLastActionRepository.save(
                AttachLastAction.builder()
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .status(status)
                        .sendTo(sendTo)
                        .attachId(attach)
                        .build()
        );
    }

    private int getLastModifiedStep(Integer workflowBaseId) {
        Integer lastTouched = workflowBaseStepRepository.findLastTouched(workflowBaseId);
        return (lastTouched == null) ? 1 : lastTouched;
    }

    private boolean getLasAction(Integer roleId, Integer stepId, Integer workflowBaseId, Integer action) {
        boolean isSent = false;
        List<WorkflowBaseLastAction> lastActionList = workflowBaseLastActionRepository
                .findLastAction(workflowBaseId, stepId, roleId);
        if (!lastActionList.isEmpty()) {
            for (WorkflowBaseLastAction lastAction : lastActionList) {
                if (lastAction.getActionId().equals(action)) isSent = true;
            }
        }
        return isSent;
    }

    private boolean getHasSummary(Integer workflowBaseId) {
        boolean hasAlreadySummary = false;
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository.findById(workflowBaseId);
        if (workflowBaseStepOptional.isPresent())
            hasAlreadySummary = workflowBaseStepOptional.get().getSummaryId() != null;
        return hasAlreadySummary;
    }

    private void sendNotification (Integer facultyId, String[] sendTo, Program program, String templateString, Step step) {
        Integer programCreationTemplateId = sharedMethods.getSettingValue(templateString);
        Template programCreationTemplate = notificationService.getTemplate(programCreationTemplateId);
        String faculty = catalogService.getCatalogItemValue(facultyId);
        String template  = programCreationTemplate.getEmailBody();

        template = template.replace(Constant.PROGRAM_FLAG, program.getName());
        template = template.replace(Constant.FACULTY_FLAG, faculty);
        template = template.replace(Constant.URL_FLAG, sharedMethods.getSettingValueOnString(Constant.ATENEA_URL));
        template = template.replace(Constant.CAMPUS_FLAG, getCampusValue(campusRepository.findByProgramId_ProgramId(program.getProgramId())));
        template = template.replace(Constant.STEP_FLAG, step.getStepName());

        String subject = programCreationTemplate.getSubject();
        subject = subject.replace(Constant.STEP_FLAG, step.getStepName());

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

    private String[] getEvaluatorsUser(Integer programId) {
        List<UserByRoleModel> userRoles = sinuService.getUserByRole(Constant.VR_ROLE);
        List<String> evaluators = new ArrayList<>();
        for (UserByRoleModel user : userRoles) {
            evaluators.add(user.getUserEmail());
        }
        List<UserAssignedToProgram> userAssigned = stepRoleUserService
                .getUserRelatedWithProgram(Constant.AC_ROLE, programId);
        if (!userAssigned.isEmpty()) {
            evaluators.add(userAssigned.get(0).getUserEmail());
        }
        List<UserAssignedToProgram> directorAssigned = stepRoleUserService
                .getUserRelatedWithProgram(Constant.DIR_ROLE, programId);
        if (!directorAssigned.isEmpty()) {
            evaluators.add(directorAssigned.get(0).getUserEmail());
        }
        return evaluators.toArray(new String[0]);
    }

}
