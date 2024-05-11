package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.UserData;
import com.fusm.workflow.model.WorkflowBaseRequest;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.IRoleStepActionService;
import com.fusm.workflow.service.IWorkflowBaseService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkflowBaseService implements IWorkflowBaseService {

    @Autowired
    private IWorkflowBaseRepository workflowBaseRepository;

    @Autowired
    private IWorkflowRepository workflowRepository;

    @Autowired
    private IWorkFlowStepRepository workFlowStepRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private IStepRoleUserRepository stepRoleUserRepository;

    @Autowired
    private IStepRoleActionRepository stepRoleActionRepository;

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private IRoleStepActionService roleStepActionService;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IAttachRepository attachRepository;


    @Override
    public void createWorkflowBase(WorkflowBaseRequest workflowBaseRequest) {
        if (isEnlarge(workflowBaseRequest.getWorkflowObjectId()) && workflowBaseRequest.getWorkflowType().equalsIgnoreCase("program")) {
            Optional<Program> programOptional = programRepository.findById(workflowBaseRequest.getWorkflowObjectId());
            if (programOptional.isPresent()) {
                Program program = programOptional.get();
                cloneWorkflow(program.getProgramFather(), program, workflowBaseRequest);
            }
        } else {
            List<Integer> workflowReferenced = getWorkflowBase(workflowBaseRequest.getWorkflowType(), isFormal(workflowBaseRequest.getWorkflowObjectId()));
            for (Integer workflowId : workflowReferenced) {
                Optional<Workflow> originalWorkflowOptional = workflowRepository.findById(workflowId);

                if (originalWorkflowOptional.isPresent()) {
                    WorkflowBase workflowBase = workflowBaseRepository.save(
                            WorkflowBase
                                    .builder()
                                    .status(Constant.STATUS_CREATED)
                                    .workflowObjectId(workflowBaseRequest.getWorkflowObjectId())
                                    .workflowObjectType(defineProgramType(workflowBaseRequest.getWorkflowType()))
                                    .createdAt(new Date())
                                    .createdBy(workflowBaseRequest.getCreatedBy())
                                    .workflowId(originalWorkflowOptional.get())
                                    .build()
                    );
                    createSteps(workflowId, workflowBase, workflowBaseRequest);
                    if (workflowBaseRequest.getUserData() != null) createUserActions(workflowBase, workflowBaseRequest.getUserData());
                }
            }
        }
    }

    @Override
    public Boolean hasFlowStarted(Integer programId) {
        List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectIdAndStatus(programId, Constant.STATUS_CREATED);
        return !workflowBases.isEmpty();
    }

    @Override
    public void relateUserToWorkflow(UserData userData, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            if (programOptional.get().getProgramFather() != null) {
                int programIdFather = programOptional.get().getProgramFather().getProgramId();
                List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectId(programIdFather);
                for (WorkflowBase workflowBase : workflowBases) {
                    createUserActions(workflowBase, userData);
                }
            }
            List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectId(programId);
            for (WorkflowBase workflowBase : workflowBases) {
                createUserActions(workflowBase, userData);
            }
        }
    }

    @Override
    public void relateUserToWorkflowFather(UserData userData, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            if (programOptional.get().getProgramFather() != null) {
                int programIdFather = programOptional.get().getProgramFather().getProgramId();
                List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectId(programIdFather);
                for (WorkflowBase workflowBase : workflowBases) {
                    createUserActions(workflowBase, userData);
                }
            }
            List<WorkflowBase> workflowBases = workflowBaseRepository.findAllByWorkflowObjectId(programId);
            for (WorkflowBase workflowBase : workflowBases) {
                createUserActions(workflowBase, userData);
            }
        }
    }

    public void cloneWorkflow(Program programFather, Program programChild, WorkflowBaseRequest workflowBaseRequest) {
        List<WorkflowBase> workflowBases = workflowBaseRepository
                .findAllByWorkflowObjectIdAndStatus(programFather.getProgramId(), Constant.STATUS_FINISHED);
        for (WorkflowBase base : workflowBases) {

            WorkflowBase workflowBaseClone = workflowBaseRepository.save(
                    WorkflowBase
                            .builder()
                            .status(Constant.STATUS_CREATED)
                            .workflowObjectId(programChild.getProgramId())
                            .workflowObjectType("program")
                            .createdAt(new Date())
                            .createdBy(workflowBaseRequest.getCreatedBy())
                            .workflowId(base.getWorkflowId())
                            .build()
            );
            cloneSteps(base.getWorkflowBaseId(), workflowBaseClone, workflowBaseRequest);
            if (workflowBaseRequest.getUserData() != null) {
                createUserActions(base, workflowBaseRequest.getUserData());
                createUserActions(workflowBaseClone, workflowBaseRequest.getUserData());
            }
        }
    }

    private boolean isEnlarge(Integer objectId) {
        boolean isEnlarge = false;
        Optional<Program> programOptional = programRepository.findById(objectId);
        if (programOptional.isPresent()) isEnlarge = programOptional.get().getIsEnlarge();
        return isEnlarge;
    }

    private boolean isFormal(Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        boolean isType = false;

        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            isType = program.getTypeFormationId().equals(sharedMethods.getSettingValue(Constant.FORMAL_PROGRAM));
        }
        return isType;
    }

    private String defineProgramType(String programType) {
        String result = " - ";
        if (programType.equalsIgnoreCase("program")) result = Constant.WORKFLOW_PROGRAM_TYPE;
        return result;
    }

    List<Integer> getWorkflowBase(String programType, boolean isFormal) {
        List<Integer> workflowBaseResult = new ArrayList<>();

        if (programType.equalsIgnoreCase("program")) {
            if (isFormal) {
                String[] referencedWorkflow
                        = { Constant.CREATE_PROGRAM_BASE_CONDITION_WORKFLOW, Constant.CREATE_PROGRAM_BASE_PAPER_WORKFLOW };
                for (String reference : referencedWorkflow) {
                    workflowBaseResult.add(sharedMethods.getSettingValue(reference));
                }
            } else {
                String[] referencedWorkflow
                        = { Constant.CREATE_PROGRAM_BASE_NO_FORMAL };
                for (String reference : referencedWorkflow) {
                    workflowBaseResult.add(sharedMethods.getSettingValue(reference));
                }
            }
        }
        return workflowBaseResult;
    }

    private void createSteps(Integer workflowId, WorkflowBase workflowBase, WorkflowBaseRequest workflowBaseRequest) {
        List<WorkflowStep> stepsByWorkflowList =
                workFlowStepRepository.findAllByWorkflowStepPKId_WorkflowId_WorkflowIdAndEnabled(workflowId, true);

        for (WorkflowStep step : stepsByWorkflowList) {
            WorkflowBaseStep newWorkflowBaseStep = workflowBaseStepRepository.save(
                    WorkflowBaseStep.builder()
                            .isDone(false)
                            .isSent(false)
                            .isEditable(step.getWorkflowStepPKId().getStepId().getIsMandatory())
                            .roleId(workflowBaseRequest.getRoleId())
                            .createdAt(new Date())
                            .createdBy(workflowBaseRequest.getCreatedBy())
                            .workflowBaseId(workflowBase)
                            .stepId(step.getWorkflowStepPKId().getStepId())
                            .build()
            );
            createDefaultStepFeedback(newWorkflowBaseStep, workflowBaseRequest);
        }
    }

    private void cloneSteps(Integer workflowBaseFatherId, WorkflowBase workflowBaseChild, WorkflowBaseRequest workflowBaseRequest) {
        List<WorkflowBaseStep> workflowStepsFather = workflowBaseStepRepository
                .findAllByWorkflowBaseId_WorkflowBaseId(workflowBaseFatherId);

        for (WorkflowBaseStep workflowBaseStep : workflowStepsFather) {
            WorkflowBaseStep newWorkflowBaseStep = workflowBaseStepRepository.save(
                    WorkflowBaseStep.builder()
                            .isDone(false)
                            .isSent(false)
                            .isEditable(workflowBaseStep.getStepId().getIsMandatory())
                            .roleId(workflowBaseRequest.getRoleId())
                            .createdAt(new Date())
                            .createdBy(workflowBaseRequest.getCreatedBy())
                            .workflowBaseId(workflowBaseChild)
                            .stepId(workflowBaseStep.getStepId())
                            .build()
            );
            createDefaultStepFeedback(newWorkflowBaseStep, workflowBaseRequest);
            getApprovedAttach(newWorkflowBaseStep, workflowBaseStep.getWorkflowBaseStepId());
        }
    }

    private void createDefaultStepFeedback(WorkflowBaseStep workflowBaseStep, WorkflowBaseRequest workflowBaseRequest) {
        List<Integer> relatedRoles = roleStepActionService.getRolesRelatedWithStep(workflowBaseStep.getStepId().getStepId());
        for (Integer role : relatedRoles) {
            workflowBaseStepFeedbackRepository.save(
                    WorkflowBaseStepFeedback.builder()
                            .status(sharedMethods.getSettingValue(Constant.STEP_ON_PROJECTION))
                            .roleId(role)
                            .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                            .createdBy(workflowBaseRequest.getCreatedBy())
                            .isSummary(false)
                            .workflowBaseStepId(workflowBaseStep)
                            .build()
            );
        }
    }

    private void createUserActions(WorkflowBase workflowBase, UserData userData) {
        /*List<StepRoleAction> stepRoleActionList =
                stepRoleActionRepository.findAllByRoleId(workflowBaseRequest.getUserData().getRoleId());
        for (StepRoleAction action : stepRoleActionList) {

        }*/
        stepRoleUserRepository.save(
                StepRoleUsers.builder()
                        .userId(userData.getUserId())
                        .userEmail(userData.getUserEmail())
                        .stepRoleActionId(null)
                        .workflowBaseId(workflowBase)
                        .enabled(true)
                        .createdAt(new Date())
                        .roleUser(userData.getRoleId())
                        .build()
        );

    }

    private void getApprovedAttach(WorkflowBaseStep workflowBaseStep, Integer workflowBaseStepFatherId) {
        List<Attach> attachOrigin = attachRepository
                .findAllByWorkflowBaseStepId_WorkflowBaseStepIdAndIsDeclined(workflowBaseStepFatherId, false);

        for (Attach attach : attachOrigin) {
            attachRepository.save(
                    Attach.builder()
                            .name(attach.getName())
                            .url(attach.getUrl())
                            .enabled(true)
                            .isSent(true)
                            .version(attach.getVersion())
                            .status(attach.getStatus())
                            .roleId(attach.getRoleId())
                            .isOriginal(true)
                            .isDeclined(false)
                            .createdAt(attach.getCreatedAt())
                            .createdBy(attach.getCreatedBy())
                            .attachFather(findAttachReferenced(attach.getAttachFather()))
                            .workflowBaseStepId(workflowBaseStep)
                            .referencedAttach(attach)
                            .build()
            );
        }
    }

    private Attach findAttachReferenced(Attach attachFather) {
        Attach attachReference = null;
        if (attachFather != null) {
            List<Attach> references = attachRepository.findAllByReferencedWithoutFather(attachFather.getAttachId());
            if (!references.isEmpty()) attachReference = references.get(0);
        }
        return  attachReference;
    }

}
