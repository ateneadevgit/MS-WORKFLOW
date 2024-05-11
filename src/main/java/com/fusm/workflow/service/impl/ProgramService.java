package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Program;
import com.fusm.workflow.entity.Status;
import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.INotificationService;
import com.fusm.workflow.external.ISettingsService;
import com.fusm.workflow.model.EvaluateProposalModel;
import com.fusm.workflow.model.FileModel;
import com.fusm.workflow.model.external.DocumentRequest;
import com.fusm.workflow.model.external.NotificationRequest;
import com.fusm.workflow.model.external.SettingRequest;
import com.fusm.workflow.model.external.Template;
import com.fusm.workflow.repository.IProgramRepository;
import com.fusm.workflow.repository.IStatusRepository;
import com.fusm.workflow.service.IProgramService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProgramService implements IProgramService {

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private ISettingsService settingsService;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private SharedMethods sharedMethods;


    @Override
    public void evaluateProposal(EvaluateProposalModel evaluation, Integer proposalId) {
        Optional<Program> programOptional = programRepository.findById(proposalId);
        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            String fileRoute = evaluation.getFileFeedback() == null ?
                    null : sharedMethods.saveFile(evaluation.getFileFeedback(), evaluation.getCreatedBy());
            createStatus(evaluateStatus(evaluation.getEvaluation()), Constant.STATUS_PROPOSAL, fileRoute, evaluation, program);

            if (!program.getIsEnlarge()){
                if (evaluation.getEvaluation().equals("approved")) {
                    approveProposal(program, fileRoute, evaluation);
                }

            } else {
                if (evaluation.getEvaluation().equals("approved")) {
                    evaluateEnlargeProposal(program.getProgramFather(), program, fileRoute, Constant.RENOVATION_ON_RENOVATION_STATUS, evaluation);
                } else if (evaluation.getEvaluation().equals("declined")) {
                    evaluateEnlargeProposal(program.getProgramFather(), program, fileRoute, Constant.RENOVATION_STATUS_DECLINED, evaluation);
                }
            }

            if (evaluation.getEvaluation().equals("completeness")) {
                sendNotification(
                        program.getFacultyId(),
                        program.getCreatedBy().split(","),
                        program.getName(),
                        Constant.PROPOSAL_REVIEW_TEMPLATE
                );
            }
            if (evaluation.getEvaluation().equals("declined")) {
                sendNotification(
                        program.getFacultyId(),
                        program.getCreatedBy().split(","),
                        program.getName(),
                        Constant.PROPOSAL_DECLINED_TEMPLATE
                );
            }
        }
    }

    @Override
    public void declineOrDisableProgram(EvaluateProposalModel evaluation, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            statusRepository.save(
                    Status.builder()
                            .statusId(evaluateStatusProgram(evaluation.getEvaluation()))
                            .statusType(getSettingValue(Constant.STATUS_PROGRAM))
                            .feedbackFileUrl(evaluation.getFileFeedback() == null ?
                                    null : sharedMethods.saveFile(evaluation.getFileFeedback(), evaluation.getCreatedBy()))
                            .feedback(evaluation.getFeedback())
                            .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                            .createdBy(evaluation.getCreatedBy())
                            .programId(program)
                            .roleId(evaluation.getRoleId())
                            .build()
            );
            if (evaluation.getEvaluation().equals("disabled")) {
                sendNotification(
                        program.getFacultyId(),
                        program.getCreatedBy().split(","),
                        program.getName(),
                        Constant.PROGRAM_DISABLED_TEMPLATE
                );
            }
            if (evaluation.getEvaluation().equals("declined")) {
                sendNotification(
                        program.getFacultyId(),
                        program.getCreatedBy().split(","),
                        program.getName(),
                        Constant.PROGRAM_DECLINED_TEMPLATE
                );
            }
        }
    }

    @Override
    public String getProgramName(List<Integer> programIds) {
        StringBuilder programs = new StringBuilder();
        for (Integer id : programIds) {
            Optional<Program> programOptional = programRepository.findById(id);
            programOptional.ifPresent(program -> programs.append(program.getName()).append(", "));
        }
        programs.deleteCharAt(programs.length() - 2);
        return programs.toString();
    }

    private Integer getSettingValue(String settingName) {
        return Integer.parseInt(
                settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                )
        );
    }

    private Integer evaluateStatus(String status) {
        String programStatus = Constant.PROPOSAL_REQUEST_SENT;
        if (status.equals("review")) programStatus = Constant.PROPOSAL_REQUEST_ON_REVIEW;
        if (status.equals("declined")) programStatus = Constant.PROPOSAL_DECLINED;
        if (status.equals("completeness")) programStatus = Constant.PROPOSAL_COMPLETENESS;
        if (status.equals("approved")) programStatus = Constant.PROPOSAL_APPROVED;
        return getSettingValue(programStatus);
    }

    private Integer evaluateStatusProgram(String status) {
        String programStatus = Constant.PROGRAM_ON_CONSTRUCTION;
        if (status.equals("disabled")) programStatus = Constant.PROGRAM_DISABLED;
        if (status.equals("declined")) programStatus = Constant.PROGRAM_DECLINED;
        return getSettingValue(programStatus);
    }

    private void sendNotification(Integer facultyId, String[] sendTo, String programName, String templateString) {
        Integer programCreationTemplateId = getSettingValue(templateString);
        Template programCreationTemplate = notificationService.getTemplate(programCreationTemplateId);
        String faculty = catalogService.getCatalogItemValue(facultyId);
        String template  = programCreationTemplate.getEmailBody();

        template = template.replace(Constant.PROGRAM_FLAG, programName);
        template = template.replace(Constant.FACULTY_FLAG, faculty);
        template = template.replace(Constant.URL_FLAG, getSettingValueToString(Constant.ATENEA_URL));

        notificationService.sendNotification(
                NotificationRequest.builder()
                        .sendTo(sendTo)
                        .subject(programCreationTemplate.getSubject().replace(Constant.PROGRAM_FLAG, programName))
                        .body(template)
                        .build()
        );
    }

    private String getSettingValueToString(String settingName) {
        return settingsService.getSetting(
                SettingRequest.builder()
                        .settingName(settingName)
                        .build()
        );
    }

    private void approveProposal(Program program, String fileRoute, EvaluateProposalModel evaluation) {
        createStatus(getSettingValue(Constant.PROGRAM_ON_CONSTRUCTION), Constant.STATUS_PROGRAM, fileRoute, evaluation, program);
        sendNotification(
                program.getFacultyId(),
                program.getCreatedBy().split(","),
                program.getName(),
                Constant.PROPOSAL_APPROVED_TEMPLATE
        );
    }

    private void evaluateEnlargeProposal(Program program, Program programChild, String fileRoute, String status, EvaluateProposalModel evaluation) {
        createStatus(getSettingValue(status), Constant.STATUS_RENOVATION, fileRoute, evaluation, programChild);
        createStatus(getSettingValue(status), Constant.STATUS_RENOVATION, fileRoute, evaluation, program);
        if (status.equals("approved")) {
            sendNotification(
                    program.getFacultyId(),
                    program.getCreatedBy().split(","),
                    program.getName(),
                    Constant.PROPOSAL_APPROVED_TEMPLATE
            );
        }
    }

    private void createStatus(Integer evaluation, String statusType, String fileRoute, EvaluateProposalModel evaluationRequest, Program program){
        statusRepository.save(
                Status.builder()
                        .statusId(evaluation)
                        .statusType(getSettingValue(statusType))
                        .feedbackFileUrl(fileRoute)
                        .feedback(evaluationRequest.getFeedback())
                        .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                        .createdBy(evaluationRequest.getCreatedBy())
                        .programId(program)
                        .roleId(evaluationRequest.getRoleId())
                        .build()
        );
    }

}
