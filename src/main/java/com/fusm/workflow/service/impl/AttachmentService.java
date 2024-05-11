package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.AttachmentDto;
import com.fusm.workflow.dto.WorkflowStepDto;
import com.fusm.workflow.entity.Attach;
import com.fusm.workflow.entity.WorkflowBaseStep;
import com.fusm.workflow.entity.WorkflowBaseStepFeedback;
import com.fusm.workflow.model.ActionModel;
import com.fusm.workflow.model.AttachmentModel;
import com.fusm.workflow.repository.IAttachRepository;
import com.fusm.workflow.repository.IWorkflowBaseStepFeedbackRepository;
import com.fusm.workflow.repository.IWorkflowBaseStepRepository;
import com.fusm.workflow.service.IAttachmentService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AttachmentService implements IAttachmentService {

    @Autowired
    private IWorkflowBaseStepFeedbackRepository workflowBaseStepFeedbackRepository;

    @Autowired
    private IAttachRepository attachRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;


    @Override
    public List<AttachmentModel> hasViewAttachment(List<ActionModel> actions, WorkflowStepDto stepDto, Integer workflowBaseId, Integer roleId) {
        List<AttachmentModel> attachmentList = new ArrayList<>();

        Boolean hasLoadFile = sharedMethods.hasAction(sharedMethods.getSettingValue(Constant.LOAD_FILE_ACTION), actions);
        Boolean evaluateStep = sharedMethods.hasAction(sharedMethods.getSettingValue(Constant.EVALUATE_STEP_ACTION), actions);

        if (hasLoadFile && evaluateStep ) {
            attachmentList = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, true, roleId);
            /*List<AttachmentModel> sendToEvaluation = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, true, roleId);
            List<AttachmentModel> ownedAttach =  getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, false, roleId);
            Set<AttachmentModel> setSentEvaluation = new HashSet<>(sendToEvaluation);
            Set<AttachmentModel> setOwned = new HashSet<>(ownedAttach);
            Set<AttachmentModel> collectedSet = new HashSet<>(setSentEvaluation);
            collectedSet.addAll(setOwned);
            attachmentList = new ArrayList<>(collectedSet);*/
        } else if (hasLoadFile) {
            attachmentList = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, true, roleId);
            /*List<AttachmentModel> ownAttach = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, false, roleId);
            List<AttachmentModel> approvedAttach = new ArrayList<>();
            List<WorkflowBaseStepFeedback> workflowBaseStepFeedbacks = workflowBaseStepFeedbackRepository.findlastStatus(roleId, stepDto.getWorkflowStepId());
            if (ownAttach.isEmpty() && !workflowBaseStepFeedbacks.isEmpty()) {
                if (workflowBaseStepFeedbacks.get(0).getStatus().equals(Constant.STEP_APPROVED_VALUE) ||
                        workflowBaseStepFeedbacks.get(0).getStatus().equals(Constant.STEP_ON_SUMMARY_VALUE)) {
                    approvedAttach = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), false, true, roleId);
                }
            }
            Set<AttachmentModel> setSentEvaluation = new HashSet<>(ownAttach);
            Set<AttachmentModel> setOwned = new HashSet<>(approvedAttach);
            Set<AttachmentModel> collectedSet = new HashSet<>(setSentEvaluation);
            collectedSet.addAll(setOwned);
            attachmentList = new ArrayList<>(collectedSet);*/
        } else if (evaluateStep) {
            //attachmentList = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), false, true, roleId);
            attachmentList = getAttachmentByStep(workflowBaseId, stepDto.getStepId(), true, false, roleId);
        }
        return attachmentList;
    }

    @Override
    public void changeAttachStatus(Integer status, Integer workflowId, Integer stepId, Integer roleId) {
        List<AttachmentDto> attachmentDtoList = attachRepository.findAttachByWorkflowAndStep(workflowId, stepId, true, false, roleId);

        for (AttachmentDto attachmentDto : attachmentDtoList) {
            Optional<Attach> attachOptional = attachRepository.findById(attachmentDto.getIdAttach());

            if (attachOptional.isPresent()) {
                Attach attach = attachOptional.get();
                attach.setStatus(status);
                attach.setIsDeclined(true);
                attachRepository.save(attach);

                if (attach.getAttachFather() == null) {
                    List<Attach> attachChild = attachRepository.findAllByAttachFather_AttachId(attach.getAttachId());
                    for (Attach child : attachChild) {
                        if (child.getEnabled()) {
                            child.setIsSent(true);
                            attach.setStatus(status);
                            attach.setIsDeclined(true);
                            attachRepository.save(child);
                        }
                    }
                }
            }
        }
    }

    private List<AttachmentModel> getAttachmentByStep(Integer workflowBaseId, Integer stepId, Boolean hasRestriction, Boolean isOwn, Integer roleId) {
        List<AttachmentDto> attachmentDtoList = attachRepository.findAttachByWorkflowAndStep(workflowBaseId, stepId, hasRestriction, isOwn, roleId);
        Map<Integer, AttachmentModel> attachmentFather = new HashMap<>();

        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(workflowBaseId, stepId);

        if (workflowBaseStepOptional.isPresent()) {
            for (AttachmentDto attachmentDto : attachmentDtoList) {
                if (attachmentDto.getAttachFather() == null) {
                    int firstStatus = 0;
                    if (attachmentDto.getRoleId().equals(roleId)) {
                        firstStatus = workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.DC_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId());
                    } else {
                        firstStatus = workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.DIR_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId());
                    }
                    Integer[] statusRole = {
                            firstStatus,
                            workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.AC_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId()),
                            workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.VR_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId())
                    };
                    attachmentFather.put(attachmentDto.getIdAttach(), buildAttachment(attachmentDto, statusRole));
                }
            }

            for (AttachmentDto attachmentDto : attachmentDtoList) {
                if (attachmentDto.getAttachFather() != null) {
                    int firstStatus = 0;
                    if (attachmentDto.getRoleId().equals(roleId)) {
                        firstStatus = workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.DC_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId());
                    } else {
                        firstStatus = workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.DIR_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId());
                    }
                    Integer[] statusRole = {
                            firstStatus,
                            workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.AC_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId()),
                            workflowBaseStepFeedbackRepository.findAttachStatusByRole(Constant.VR_ROLE, workflowBaseStepOptional.get().getWorkflowBaseStepId())
                    };
                    AttachmentModel father = attachmentFather.get(attachmentDto.getAttachFather());
                    if (father!= null) father.getAttachmentChild().add(buildAttachment(attachmentDto, statusRole));
                }
            }
        }

        return new ArrayList<>(attachmentFather.values());
    }

    private AttachmentModel buildAttachment(AttachmentDto attachmentDto, Integer[] status) {
        return AttachmentModel.builder()
                .attachId(attachmentDto.getIdAttach())
                .name(attachmentDto.getName())
                .version(attachmentDto.getVersion())
                .status(attachmentDto.getStatus())
                .createdDate(attachmentDto.getCreatedAt())
                .urlFile(attachmentDto.getUrl())
                .enabled(attachmentDto.getEnabled())
                .dcStatus(status[0])
                .acStatus(status[1])
                .vaStatus(status[2])
                .isOriginal(attachmentDto.getIsOriginal())
                .roleId(attachmentDto.getRoleId())
                .attachmentChild(new ArrayList<>())
                .createdBy(attachmentDto.getCreatedBy())
                .attachFatherId(attachmentDto.getAttachFather())
                .isDeclined(attachmentDto.getIsDeclined())
                .isSent(attachmentDto.getIsSent())
                .build();
    }

}
