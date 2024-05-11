package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.*;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.IProblemBankService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProblemBankService implements IProblemBankService {

    @Autowired
    private IProblemBankRepository problemBankRepository;

    @Autowired
    private IProblemCompetenceRepository problemCompetenceRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private ICompetenceRepository competenceRepository;

    @Autowired
    private IProblemStatusRepository problemStatusRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;


    @Override
    public void createProblemBank(ProblemBankRequest problemBankRequest) {
        Optional<Curriculum> curriculumOptional = (problemBankRequest.getCurriculumId() != null) ?
                curriculumRepository.findById(problemBankRequest.getCurriculumId()) : Optional.empty();
        ProblemBank problemBank = problemBankRepository.save(
                ProblemBank.builder()
                        .tittle(problemBankRequest.getTittle())
                        .semester(problemBankRequest.getSemester())
                        .file(sharedMethods.saveFile(problemBankRequest.getFile(), problemBankRequest.getCreatedBy()))
                        .description(problemBankRequest.getDescription())
                        .moodleUrl(problemBankRequest.getLinkMoodle())
                        .createdBy(problemBankRequest.getCreatedBy())
                        .createdAt(new Date())
                        .enabled(true)
                        .isNif(problemBankRequest.getRoleId().equals(Constant.VR_ROLE))
                        .curriculumId(curriculumOptional.orElse(null))
                        .build()
        );
        createProblemCompetence(problemBank, problemBankRequest.getCompetences());
        if (problemBankRequest.getRoleId().equals(Constant.DIR_ROLE)) {
            createStatus(Constant.STATUS_RQ, problemBankRequest.getCreatedBy(), null, null, problemBank);
        } else {
            createStatus(Constant.STATUS_APPROVED, problemBankRequest.getCreatedBy(), null, null, problemBank);
        }
    }

    @Override
    public List<ProblemBankModel> getNifProblemBank(SearchModel searchModel) {
        return problemBankRepository.findAllNifsOrdered(searchModel.getProblem()).stream().map(
                problemBank -> ProblemBankModel.builder()
                        .problemBankId(problemBank.getIdProblemBank())
                        .semester(problemBank.getSemester())
                        .tittle(problemBank.getTittle())
                        .file(problemBank.getFile())
                        .description(problemBank.getDescription())
                        .competences(getCompetencesByProblemBank(problemBank.getIdProblemBank()))
                        .linkMoodle(problemBank.getMoodleUrl())
                        .enabled(problemBank.getEnabled())
                        .build()
        ).toList();
    }

    @Override
    public List<ProblemBankModel> getProblemBank(SearchModel searchModel) {
        return problemBankRepository
                .findAllProblemBank(searchModel.getRoleId(), searchModel.getSemester(), convertIdsToString(searchModel.getProgramId()), searchModel.getProblem())
                .stream().map(
                problemBank -> ProblemBankModel.builder()
                        .problemBankId(problemBank.getIdProblemBank())
                        .semester(problemBank.getSemester())
                        .tittle(problemBank.getTittle())
                        .file(problemBank.getFile())
                        .description(problemBank.getDescription())
                        .competences(getCompetencesByProblemBank(problemBank.getIdProblemBank()))
                        .linkMoodle(problemBank.getMoodleUrl())
                        .enabled(problemBank.getEnabled())
                        .subjectId(problemBank.getCurriculumId())
                        .program(getProgramName(problemBank.getProgramId()))
                        .statusId(problemBank.getIdStatus())
                        .subject(problemBank.getCurriculumName())
                        .build()
        ).toList();
    }

    @Override
    public void updateProblemBank(ProblemBankRequest problemBankRequest, Integer problemBankId) {
        Optional<ProblemBank> problemBankOptional = problemBankRepository.findById(problemBankId);
        if (problemBankOptional.isPresent()) {
            ProblemBank problemBank = problemBankOptional.get();
            problemBank.setTittle(problemBankRequest.getTittle());
            problemBank.setSemester(problemBankRequest.getSemester());
            if (problemBankRequest.getFile() != null) problemBank.setFile(sharedMethods.saveFile(problemBankRequest.getFile(), problemBankRequest.getCreatedBy()));
            problemBank.setDescription(problemBankRequest.getDescription());
            problemBank.setMoodleUrl(problemBankRequest.getLinkMoodle());
            problemBank.setUpdatedAt(new Date());
            problemBankRepository.save(problemBank);
            updateProblemCompetence(problemBank, problemBankRequest.getCompetences());
            if (problemBankRequest.getRoleId().equals(Constant.DIR_ROLE)) {
                createStatus(Constant.STATUS_RQ, problemBankRequest.getCreatedBy(), null, null, problemBank);
            }
        }
    }

    @Override
    public void evaluateProblemBank(EvaluateProposalModel evaluateProposalModel, Integer problemBankId) {
        Optional<ProblemBank> problemBankOptional = problemBankRepository.findById(problemBankId);
        List<ProblemStatus> problemStatusesList = problemStatusRepository.findLastStatus(problemBankId);
        String status = evaluateProposalModel.getEvaluation();

        if (!problemStatusesList.isEmpty() && problemBankOptional.isPresent()) {
            ProblemBank problemBank = problemBankOptional.get();
            if (problemStatusesList.get(0).getStatusId().equals(Constant.PROBLEM_SENT_DISABLE)) {
                if (evaluateProposalModel.getEvaluation().equals(Constant.STATUS_APPROVED)) {
                    problemBank.setEnabled(false);
                    problemBankRepository.save(problemBank);
                    status = Constant.STATUS_DISABLED;
                } else {
                    status = Constant.STATUS_APPROVED;
                }
            }
            createStatus(
                    status,
                    evaluateProposalModel.getCreatedBy(),
                    (evaluateProposalModel.getFileFeedback() != null) ?
                            sharedMethods.saveFile(evaluateProposalModel.getFileFeedback(), evaluateProposalModel.getCreatedBy()) : null,
                    evaluateProposalModel.getFeedback(),
                    problemBank);
        }
    }

    @Override
    public void enableDisableProblemBank(Integer problemBankId, Boolean enabled, UserData userData) {
        Optional<ProblemBank> problemBankOptional = problemBankRepository.findById(problemBankId);
        if (problemBankOptional.isPresent()) {
            ProblemBank problemBank = problemBankOptional.get();
            String status = Constant.STATUS_APPROVED;

            if (enabled && !userData.getRoleId().equals(Constant.DIR_ROLE)) {
                problemBank.setEnabled(true);
                problemBankRepository.save(problemBank);
            } else if (userData.getRoleId().equals(Constant.DIR_ROLE) && !enabled) {
                status = Constant.STATUS_RQ_DISABLE;
            } else if (!enabled) {
                problemBank.setEnabled(false);
                problemBankRepository.save(problemBank);
                status = Constant.STATUS_DISABLED;
            }

            createStatus(status, userData.getUserEmail(), null, null, problemBank);
        }
    }

    private void createProblemCompetence(ProblemBank problemBank, List<Integer> competences) {
        for (Integer competenceId : competences) {
            Optional<Competence> competenceOptional = competenceRepository.findById(competenceId);
            competenceOptional.ifPresent(competence -> problemCompetenceRepository.save(
                    ProblemCompetence.builder()
                            .problemCompetencePKId(
                                    ProblemCompetencePKId.builder()
                                            .problemBankId(problemBank)
                                            .competenceId(competence)
                                            .build()
                            )
                            .createdAt(new Date())
                            .createdBy(problemBank.getCreatedBy())
                            .enabled(true)
                            .build()
            ));
        }
    }

    private void updateProblemCompetence(ProblemBank problemBank, List<Integer> competences) {
        List<ProblemCompetence> problemCompetenceList = problemCompetenceRepository
                .findAllByProblemCompetencePKId_ProblemBankId_ProblemBankId(problemBank.getProblemBankId());
        for (ProblemCompetence problemCompetence : problemCompetenceList) {
            if (competences.contains(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId()) && problemCompetence.getEnabled().equals(true)) {
                competences.remove(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId());
            } else if (competences.contains(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId()) && problemCompetence.getEnabled().equals(false)) {
                problemCompetence.setEnabled(true);
                problemCompetenceRepository.save(problemCompetence);
                competences.remove(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId());
            } else if (!competences.contains(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId())) {
                problemCompetence.setEnabled(false);
                problemCompetenceRepository.save(problemCompetence);
                competences.remove(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId());
            }
        }
        createProblemCompetence(problemBank, competences);
    }

    private List<CompetenceRequest> getCompetencesByProblemBank(Integer problemBankId) {
        return problemCompetenceRepository.
                findAllByProblemCompetencePKId_ProblemBankId_ProblemBankIdAndEnabled(problemBankId, true).stream().map(
                        problemCompetence -> CompetenceRequest.builder()
                                .competenceId(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCompetenceId())
                                .categoryId(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCategoryId())
                                .code(problemCompetence.getProblemCompetencePKId().getCompetenceId().getCode())
                                .description(problemCompetence.getProblemCompetencePKId().getCompetenceId().getDescription())
                                .build()
                ).toList();
    }

    private void createStatus(String status, String createdBy, String file, String feedback, ProblemBank problemBank) {
        problemStatusRepository.save(
                ProblemStatus.builder()
                        .statusId(defineStatusId(status))
                        .createdAt(new Date())
                        .createdBy(createdBy)
                        .file(file)
                        .feedback(feedback)
                        .problemBankId(problemBank)
                        .build()
        );
    }

    private int defineStatusId(String status) {
        int statusId = 0;
        if (status.equals(Constant.STATUS_APPROVED)) statusId = Constant.PROBLEM_APPROVED;
        if (status.equals(Constant.STATUS_COMPLETENESS)) statusId = Constant.PROBLEM_COMPLETNESS;
        if (status.equals(Constant.STATUS_RQ)) statusId = Constant.PROBLEM_SENT;
        if (status.equals(Constant.STATUS_RQ_DISABLE)) statusId = Constant.PROBLEM_SENT_DISABLE;
        if (status.equals(Constant.STATUS_RQ_UPDATE)) statusId = Constant.PROBLEM_SENT_UPDATE;
        if (status.equals(Constant.STATUS_DISABLED)) statusId = Constant.PROBLEM_DISABLED;
        return statusId;
    }

    private String getProgramName(Integer programId) {
        String name = "";
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) name = programOptional.get().getName();
        return name;
    }

    private String convertIdsToString(List<Integer> ids) {
        StringBuilder idConverted = new StringBuilder();
        for (Integer id : ids) {
            idConverted.append(id).append(", ");
        }
        idConverted.delete(idConverted.length() - 2, idConverted.length());
        return idConverted.toString();
    }

}
