package com.fusm.workflow.service.impl;

import com.fusm.workflow.dto.CurriculumFatherDto;
import com.fusm.workflow.dto.ProgramSubjectDto;
import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.entity.*;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.external.ISinuService;
import com.fusm.workflow.model.*;
import com.fusm.workflow.model.external.CodeModel;
import com.fusm.workflow.model.external.SubjectData;
import com.fusm.workflow.model.external.UserSinuData;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.*;
import com.fusm.workflow.service.specific.CurriculumMapper;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.google.gson.Gson;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CurriculumService implements ICurriculumService {

    @Autowired
    private ICurriculumRepository curriculumRepository;

    @Autowired
    private IComplementaryCoreRepository complementaryCoreRepository;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private IModalityRepository modalityRepository;

    @Autowired
    private IComplementarySubjectRepository complementarySubjectRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private HistoryExtendedService historyExtendedService;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IComplementaryNifsRepository complementaryNifsRepository;

    @Autowired
    private ISyllabusService syllabusService;

    @Autowired
    private IIntegrativeActivityRepository integrativeActivityRepository;

    @Autowired
    private IIntegrativeActivityService integrativeActivityService;

    @Autowired
    private ISubjectGuideService subjectGuideService;

    @Autowired
    private IComplementaryEvaluationRepository complementaryEvaluationRepository;

    @Autowired
    private ISinuService sinuService;

    @Autowired
    private IPdfService pdfService;

    Gson gson = new Gson();


    @Override
    public void createCurriculum(CurriculumListRequest curriculumListRequest) {
        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(curriculumListRequest.getWorkflowId(), curriculumListRequest.getStepId());

        if (workflowBaseStepOptional.isPresent()) {
            for (CurriculumRequest request : curriculumListRequest.getCurriculumRequests()) {
                Optional<Curriculum> fatherOptional = Optional.empty();
                if (request.getFatherId() != null) fatherOptional = curriculumRepository.findById(request.getFatherId());
                Curriculum father = fatherOptional.orElse(null);
                ComplementaryCore complementaryCore = (request.getRaeg() != null) ?
                        saveComplementaryCore(request.getRaeg()) : null;
                ComplementarySubject complementarySubject = (request.getSubjectRequest() != null) ?
                        saveComplementarySubject(request.getSubjectRequest()) : null;
                curriculumRepository.save(
                        Curriculum.builder()
                                .name(request.getName())
                                .curriculumType(request.getType())
                                .numberCredits(request.getNumberCredits())
                                .description(request.getDescription())
                                .enabled(true)
                                .createdAt(new Date())
                                .createdBy(curriculumListRequest.getCreatedBy())
                                .currirriculumFatherId(father)
                                .complementarySubject(complementarySubject)
                                .complementaryCore(complementaryCore)
                                .isNif(curriculumListRequest.getIsNif())
                                .workflowBaseStepId(workflowBaseStepOptional.get())
                                .build()
                );
            }
            calculateParticipationPercentage(workflowBaseStepOptional.get().getWorkflowBaseId().getWorkflowObjectId());
        }
    }

    @Override
    public List<CurriculumModel> getCurriculum(Integer objectId) {
        List<Curriculum> curriculumList = curriculumRepository.findCurriculumByProgram(objectId);
        CurriculumMapper mapper = new CurriculumMapper();
        return mapper.mapToCurriculumModel(curriculumList);
    }

    @Override
    public void updateCurriculum(UpdateCurriculumRequest curriculumRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);

        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();

            curriculum.setName(curriculumRequest.getName());
            curriculum.setNumberCredits(curriculumRequest.getNumberCredits());
            curriculum.setDescription(curriculumRequest.getDescription());
            curriculum.setUpdatedAt(new Date());

            if (curriculumRequest.getRaeg() != null) {
                ComplementaryCore core = curriculum.getComplementaryCore();
                if (core != null) {
                    core.setRaeg(curriculumRequest.getRaeg());
                    complementaryCoreRepository.save(core);
                } else {
                    curriculum.setComplementaryCore(saveComplementaryCore(curriculumRequest.getRaeg()));
                }
            }

            if (curriculumRequest.getSubjectRequest() != null) {
                ComplementarySubject subject = curriculum.getComplementarySubject();
                if (subject != null) {
                    subject.setSemester(curriculumRequest.getSubjectRequest().getSemester());
                    if (curriculumRequest.getSubjectRequest().getCode() != null)
                        subject.setCode(curriculumRequest.getSubjectRequest().getCode());
                    subject.setHoursInteractionTeacher(curriculumRequest.getSubjectRequest().getHoursInteractionTeacher());
                    subject.setHourSelfWork(curriculumRequest.getSubjectRequest().getHourSelfWork());
                    complementarySubjectRepository.save(subject);
                } else {
                    curriculum.setComplementarySubject(saveComplementarySubject(curriculumRequest.getSubjectRequest()));
                }
            }

            if (curriculumRequest.getRae() != null || curriculumRequest.getCompetences() != null) {
                ComplementaryNifs complementaryNifs = curriculum.getComplementaryNifs();
                if (complementaryNifs != null) {
                    if (curriculumRequest.getRae() != null) complementaryNifs.setRae(curriculumRequest.getRae());
                    if (curriculumRequest.getCompetences() != null) complementaryNifs.setCompetences(curriculumRequest.getCompetences());
                } else {
                    complementaryNifs = ComplementaryNifs.builder()
                            .rae(curriculumRequest.getRae())
                            .competences(curriculumRequest.getCompetences())
                            .build();
                }
                complementaryNifs = complementaryNifsRepository.save(complementaryNifs);
                curriculum.setComplementaryNifs(complementaryNifs);
            }

            curriculumRepository.save(curriculum);
        }
    }

    @Override
    public void updateNameAndDescriptionOfCurriculum(UpdateNameCurriculumRequest curriculumUpdateRequest) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumUpdateRequest.getCurriculumId());
        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();
            curriculum.setName(curriculumUpdateRequest.getName());
            curriculum.setDescription(curriculumUpdateRequest.getDescription());
            if (curriculum.getComplementarySubject() != null) {
                ComplementarySubject complementarySubject = curriculum.getComplementarySubject();
                complementarySubject.setHourSelfWork(curriculumUpdateRequest.getHourSelfWork());
                complementarySubject.setHoursInteractionTeacher(curriculumUpdateRequest.getHoursInteractionTeacher());
                complementarySubjectRepository.save(complementarySubject);
            }
            curriculumRepository.save(curriculum);
        }
    }

    @Override
    public void disableCurriculum(Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);

        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();
            curriculum.setUpdatedAt(new Date());
            curriculum.setEnabled(false);
            curriculumRepository.save(curriculum);
        }
    }

    @Override
    public List<CurriculumListModel> getCurriculumByType(Integer programId, Integer type) {
        return curriculumRepository.findCurriculumByType(
                programId, type
        ).stream().map(
                curriculum -> CurriculumListModel.builder()
                        .curriculumId(curriculum.getCurriculumId())
                        .name(curriculum.getName())
                        .code((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getCode() : null)
                        .build()
        ).toList();
    }

    @Override
    public List<CurriculumFatherDto> getCurriculumByFather(Integer programId, Integer fatherId) {
        return curriculumRepository.findCurriculumByFatherCurriculumId(programId, fatherId);
    }

    @Override
    public void calculateParticipationPercentage(Integer objectId) {
        int totalCredits = curriculumRepository.sumTotalCurriculums(objectId);
        List<Curriculum> curriculumList = curriculumRepository.findCurriculumByProgram(objectId);

        for (Curriculum curriculum : curriculumList) {
            curriculum.setPercentageParticipation(calculatePercentage(totalCredits, curriculum.getNumberCredits()));
            curriculum.setUpdatedAt(new Date());
            curriculumRepository.save(curriculum);
        }
    }

    @Override
    public void updateSubjectCredit(List<SubjectListModel> subjectListModelList) {
        for (SubjectListModel subjectListModel : subjectListModelList) {
            Optional<Curriculum> curriculumOptional = curriculumRepository.findById(subjectListModel.getSubjectId());
            if (subjectListModel.getIsUpdated()) {
                if (curriculumOptional.isPresent()) {Curriculum curriculum = curriculumOptional.get();
                    curriculum.setNumberCredits(subjectListModel.getCreditNumber());
                    curriculum.setUpdatedAt(new Date());
                    curriculumRepository.save(curriculum);
                    historyExtendedService.createHistoric(
                            gson.toJson(subjectListModel),
                            Constant.MODULE_ACADEMIC_CREDITS,
                            curriculum.getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId(),
                            subjectListModel.getCreatedBy(),
                            curriculum.getCurriculumId());
                }
            }
        }
    }

    @Override
    public List<SubjectListModel> getSubjects(Integer programId) {
        return curriculumRepository.findAllSubjects(programId).stream().map(
                curriculum -> SubjectListModel.builder()
                        .subjectId(curriculum.getCurriculumId())
                        .name(curriculum.getName())
                        .semester((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getSemester() : null)
                        .creditNumber(curriculum.getNumberCredits())
                        .isUpdated(false)
                        .build()
        ).toList();
    }

    @Override
    public void updateCurriculumMassive(List<CurriculumModel> curriculumModelList) {
        WorkflowBaseStep workflowBaseStep = null;
        for (CurriculumModel curriculumModel : curriculumModelList) {
            if (curriculumModel.getIsUpdated()) {
                Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumModel.getCurriculumId());
                if (curriculumOptional.isPresent()) {
                    Curriculum curriculum = curriculumOptional.get();
                    curriculum.setUpdatedAt(new Date());
                    curriculum.setName(curriculumModel.getName());
                    curriculum.setDescription(curriculumModel.getDescription());
                    curriculumRepository.save(curriculum);
                    workflowBaseStep = curriculum.getWorkflowBaseStepId();
                }
            }
        }
        String createdBy = (curriculumModelList.isEmpty()) ? " - " : curriculumModelList.get(0).getCreatedBy();
        if (workflowBaseStep != null) createCurriculumHistory(workflowBaseStep, createdBy);
    }

    @Override
    public Integer getCurriculumCredits(Integer objectId) {
        int credits = 0;
        List<Curriculum> curriculumList = curriculumRepository.findAllSubjects(objectId);
        for (Curriculum curriculum : curriculumList) credits += curriculum.getNumberCredits();
        return credits;
    }

    @Override
    public List<CurriculumModel> getCurriculumWithoutCore(Integer objectId) {
        List<Curriculum> curriculumList = curriculumRepository.findAllWithoutCore(objectId);
        CurriculumMapper mapper = new CurriculumMapper();
        return mapper.mapToCurriculumModel(curriculumList);
    }

    @Override
    public CurriculumModel getCurriculumById(Integer curriculumId) {
        CurriculumModel curriculumModel = new CurriculumModel();
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);

        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();
            curriculumModel = CurriculumModel.builder()
                    .curriculumId(curriculum.getCurriculumId())
                    .name(curriculum.getName())
                    .type(curriculum.getCurriculumType())
                    .numberCredits(curriculum.getNumberCredits())
                    .percentageParticipation(curriculum.getPercentageParticipation())
                    .description(curriculum.getDescription())
                    .createdAt(curriculum.getCreatedAt().getTime())
                    .fatherId((curriculum.getCurrirriculumFatherId() != null) ? curriculum.getCurrirriculumFatherId().getCurriculumId() : null)
                    .subjectRequest(getComplementarySubject(curriculum.getComplementarySubject()))
                    .coreModel(getComplementaryCore(curriculum.getComplementaryCore()))
                    .isNif(curriculum.getIsNif())
                    .childs(new ArrayList<>())
                    .build();
        }
        return curriculumModel;
    }

    @Override
    public List<CoreAndSubcoreModel> getCoreAndSubcore(Integer programId) {
        return curriculumRepository.findAllCoreAndCubcore(programId).stream().map(
                curriculum -> CoreAndSubcoreModel.builder()
                        .curriculumId(curriculum.getCurriculumId())
                        .name(curriculum.getName())
                        .type(curriculum.getCurriculumType())
                        .creditNumber(curriculum.getNumberCredits())
                        .participation(curriculum.getPercentageParticipation())
                        .code((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getCode() : null)
                        .description(curriculum.getDescription())
                        .isUpdated(false)
                        .fatherId((curriculum.getCurrirriculumFatherId() != null) ?
                                curriculum.getCurrirriculumFatherId().getCurriculumId() : null)
                        .hoursInteractionTeacher((curriculum.getComplementarySubject() != null) ?
                                curriculum.getComplementarySubject().getHoursInteractionTeacher() : null)
                        .hourSelfWork((curriculum.getComplementarySubject() != null) ?
                                curriculum.getComplementarySubject().getHourSelfWork() : null)
                        .raeg((curriculum.getComplementaryCore() != null) ? curriculum.getComplementaryCore().getRaeg() : null)
                        .semester((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getSemester() : null)
                        .build()
        ).toList();
    }

    @Override
    public void updateCoreAndSubcoreMassive(List<CoreAndSubcoreModel> coreAndSubcoreModelList) {
        for (CoreAndSubcoreModel coreAndSubcoreModel : coreAndSubcoreModelList) {
            if (coreAndSubcoreModel.getIsUpdated()) {
                Optional<Curriculum> curriculumOptional = curriculumRepository.findById(coreAndSubcoreModel.getCurriculumId());
                if (curriculumOptional.isPresent()) {
                    Curriculum curriculum = curriculumOptional.get();
                    curriculum.setName(coreAndSubcoreModel.getName());
                    curriculum.setDescription(coreAndSubcoreModel.getDescription());
                    ComplementarySubject complementarySubject = null;
                    ComplementaryCore complementaryCore = null;

                    if (curriculum.getComplementarySubject() != null) {
                        complementarySubject = curriculum.getComplementarySubject();
                        complementarySubject.setCode(coreAndSubcoreModel.getCode());
                        complementarySubject.setHourSelfWork(coreAndSubcoreModel.getHourSelfWork());
                        complementarySubject.setHoursInteractionTeacher(coreAndSubcoreModel.getHoursInteractionTeacher());
                        complementarySubjectRepository.save(complementarySubject);
                    }
                    if (curriculum.getComplementaryCore() != null) {
                        complementaryCore = curriculum.getComplementaryCore();
                        complementaryCore.setRaeg(coreAndSubcoreModel.getRaeg());
                        complementaryCoreRepository.save(complementaryCore);
                    }
                    curriculum.setComplementaryCore(complementaryCore);
                    curriculum.setComplementarySubject(complementarySubject);
                    curriculumRepository.save(curriculum);
                    historyExtendedService.createHistoric(
                            gson.toJson(coreAndSubcoreModel),
                            Constant.MODULE_CORES_AND_SUBCORES,
                            curriculum.getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId(),
                            coreAndSubcoreModel.getCreatedBy(),
                            curriculum.getCurriculumId());
                }
             }
        }
    }

    @Override
    public List<SubjectSemesterModel> getSubjectBySemester(Integer programId) {
        List<SubjectSemesterModel> subjectBySemester = new ArrayList<>();
        List<SubjectListModel> subjectList = getSubjects(programId);
        Map<Integer, List<SubjectListModel>> subjectsBySemesterMap = subjectList.stream()
                .collect(Collectors.groupingBy(SubjectListModel::getSemester, Collectors.toList()));
        for (Integer key : subjectsBySemesterMap.keySet()) {
            subjectBySemester.add(new SubjectSemesterModel(key, subjectsBySemesterMap.get(key)));
        }
        return subjectBySemester;
    }

    @Override
    public CurriculumSemester getCurriculumSemesterByProgram(Integer programId) {
        return CurriculumSemester.builder()
                .curriculumList(getCurriculumWithNif(programId))
                .semesterList(getSubjectBySemesterWithNif(programId))
                .build();
    }

    @Override
    public CurriculumDetail getCurriculumDetailById(Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        CurriculumDetail curriculumDetail = new CurriculumDetail();
        if (curriculumOptional.isPresent()) {
            Curriculum curriculum = curriculumOptional.get();
            List<CurriculumDetailChild> childs = getCurriculumChildDetail(curriculumId);
            curriculumDetail = CurriculumDetail.builder()
                    .curriculumId(curriculum.getCurriculumId())
                    .name(curriculum.getName())
                    .description(curriculum.getDescription())
                    .type(curriculum.getCurriculumType())
                    .childs(childs)
                    .code((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getCode() : null)
                    .raeg((curriculum.getComplementaryCore() != null) ? curriculum.getComplementaryCore().getRaeg() : null)
                    .semester((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getSemester() : null)
                    .hoursInteractionTeacher((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getHoursInteractionTeacher() : null)
                    .hoursSelfWork((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getHourSelfWork() : null)
                    .credits(curriculum.getNumberCredits())
                    .totalCredits(getTotalCredits(childs))
                    .totalParticipation(getTotalParticipation(childs))
                    .rae((curriculum.getComplementaryNifs() != null) ? curriculum.getComplementaryNifs().getRae() : null)
                    .competences((curriculum.getComplementaryNifs() != null) ? curriculum.getComplementaryNifs().getCompetences() : null)
                    .activities(getActivitiesByCurriculum(curriculumId))
                    .build();
        }
        return curriculumDetail;
    }

    @Override
    public String curriculumPdf(Integer programId) throws DocumentException {
        String template = documentManagerService.getTemplate(Constant.CURRICULUM_TEMPLATE);
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            template = buildPdf(template, programOptional.get());
        }
        return pdfService.savePdf(template);
    }

    @Override
    public void createNifsCurriculum(SubcoreNifsRequest subcoreNifsRequest) {
        Curriculum curriculum = saveSubCore(subcoreNifsRequest);
        subcoreNifsRequest.getSyllabus().setCreatedBy(subcoreNifsRequest.getCreatedBy());
        subcoreNifsRequest.getSyllabus().setCreatedBy(subcoreNifsRequest.getCreatedBy());
        syllabusService.createSyllabusNifs(subcoreNifsRequest.getSyllabus(), curriculum);
        for (IntegrativeActivityRequest request : subcoreNifsRequest.getActivities()) request.setCreatedBy(subcoreNifsRequest.getCreatedBy());
        integrativeActivityService.createActivity(subcoreNifsRequest.getActivities(), curriculum.getCurriculumId());
        subcoreNifsRequest.setCreatedBy(subcoreNifsRequest.getCreatedBy());
        for (SubjectActivityRequest subjectActivityRequest : subcoreNifsRequest.getSubjectGuide().getActivityRequestList()) {
            subjectActivityRequest.setCreatedBy(subcoreNifsRequest.getCreatedBy());
            subjectActivityRequest.setRoleId(subcoreNifsRequest.getRoleId());
        }
        subcoreNifsRequest.getSubjectGuide().setRoleId(subcoreNifsRequest.getRoleId());
        subjectGuideService.createSubjectGuide(subcoreNifsRequest.getSubjectGuide(), curriculum.getCurriculumId());
    }

    @Override
    public SubcoreNifsModel getNifsCurriculum() {
        List<Curriculum> curriculumList = curriculumRepository
                .findAllByCurrirriculumFatherId_CurriculumIdAndEnabled(Constant.NIFS_CORE, true);
        List<CurriculumRequest> subjectRequests = new ArrayList<>();
        int totalCredits = 0;
        for (Curriculum curriculum : curriculumList) {
            subjectRequests.add(modelSubcore(curriculum));
            totalCredits += curriculum.getNumberCredits();
        }
        return SubcoreNifsModel.builder()
                .totalSubject(subjectRequests.size())
                .totalCredits(totalCredits)
                .subjects(subjectRequests)
                .build();
    }

    @Override
    public List<SemesterModel> getSemestersByProgram(Integer programId) {
        List<Integer> semesters = getSubjects(programId).stream()
                .map(SubjectListModel::getSemester)
                .distinct()
                .sorted()
                .toList();
        List<SemesterModel> semesterModels = new ArrayList<>();
        for (Integer number : semesters) {
            semesterModels.add(
                    SemesterModel.builder()
                            .semester(number)
                            .name("Semestre " + convertToRoman(number))
                            .build()
            );
        }
        return semesterModels;
    }

    @Override
    public ResponsePage<ProgramSubjectDto> getProgramSubject(SearchSubject searchSubject, PageModel pageModel) {
        Pageable pageable = PageRequest.of(pageModel.getPage(), pageModel.getSize());
        Page<ProgramSubjectDto> programSubjectDtos = curriculumRepository
                .findAllProgramSubject(
                        pageable,
                        searchSubject.getProgramId(),
                        searchSubject.getIsActivity(),
                        searchSubject.getRoleId(),
                        searchSubject.getUserId(),
                        searchSubject.getStatusId(),
                        (searchSubject.getRoleId().equals(Constant.COOR_ROLE) || searchSubject.getRoleId().equals(Constant.TEACH_ROLE)) ?
                                searchSubject.getCreatedBy() : null,
                        searchSubject.getSemester(),
                        (searchSubject.getRoleId().equals(Constant.TEACH_ROLE)) ? getCurriculumIds(searchSubject.getCreatedBy()) : null);
        return buildPageableSubject(programSubjectDtos, pageModel);
    }

    @Override
    public List<SubjectTeacherDto> getTeachersBySubject(Integer subjectId, String userId, Integer roleId) {
        List<SubjectTeacherDto> subjectTeacherDtos = new ArrayList<>();
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(subjectId);
        if (curriculumOptional.isPresent()) {
            if (curriculumOptional.get().getComplementarySubject() != null) {
                subjectTeacherDtos = curriculumRepository.findAllTeacherBySubject(
                        getUserIds(curriculumOptional.get().getComplementarySubject().getCode()), subjectId, roleId, userId);
            }
        }
        return subjectTeacherDtos;
    }

    @Override
    public void updateComplementaryNifs(UpdateComplementaryNifs complementaryNifsRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        if (curriculumOptional.isPresent()) {
            if (curriculumOptional.get().getComplementaryNifs() != null) {
                ComplementaryNifs complementaryNifs = curriculumOptional.get().getComplementaryNifs();
                if (complementaryNifsRequest.getCompetences() != null) complementaryNifs.setCompetences(complementaryNifsRequest.getCompetences());
                if (complementaryNifsRequest.getRae() != null) complementaryNifs.setRae(complementaryNifsRequest.getRae());
                complementaryNifsRepository.save(complementaryNifs);
            }
        }
    }

    @Override
    public List<SelectModel> getLevelsByProgram(Integer programId) {
        return curriculumRepository.findLevelsByProgram(programId).stream().map(
            type -> SelectModel.builder()
                    .type(type)
                    .name(sharedMethods.getCampusValue(List.of(type)).replace(" ", ""))
                    .build()
        ).toList();
    }

    @Override
    public void createComplementaryEvaluation(ComplementaryEvaluationRequest complementaryEvaluationRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        if (curriculumOptional.isPresent()) {
            ComplementaryEvaluation complementaryEvaluation = complementaryEvaluationRepository.save(
                    ComplementaryEvaluation.builder()
                            .decription(complementaryEvaluationRequest.getDescription())
                            .build()
            );
            Curriculum curriculum = curriculumOptional.get();
            curriculum.setComplementaryEvaluation(complementaryEvaluation);
            curriculumRepository.save(curriculum);
        }
    }

    @Override
    public void updateComplementaryEvaluation(ComplementaryEvaluationRequest complementaryEvaluationRequest, Integer curriculumId) {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);
        if (curriculumOptional.isPresent()) {
            if (curriculumOptional.get().getComplementaryEvaluation() != null) {
                ComplementaryEvaluation complementaryEvaluation = curriculumOptional.get().getComplementaryEvaluation();
                complementaryEvaluation.setDecription(complementaryEvaluationRequest.getDescription());
                complementaryEvaluationRepository.save(complementaryEvaluation);
            }
        }
    }

    @Override
    public List<CurriculumTypeEvaluation> getCurriculumEvaluationByType(Integer programId, Integer type) {
        return curriculumRepository.findCurriculumByType(
                programId, type
        ).stream().map(
                curriculum -> CurriculumTypeEvaluation.builder()
                        .curriculumId(curriculum.getCurriculumId())
                        .tittle(curriculum.getName())
                        .description((curriculum.getComplementaryEvaluation() != null) ? curriculum.getComplementaryEvaluation().getDecription() : null)
                        .build()
        ).toList();
    }

    @Override
    public ProgramUserData getProgramProgress(Integer programId, String userId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        ProgramUserData programUserData = new ProgramUserData();
        if (programOptional.isPresent()) {
            UserSinuData userData = sinuService.getUserData(userId);
            int totalCredits = curriculumRepository.sumTotalCurriculums(programId);
            int doneCredits =  sumCurrentCredits(programId, userId);
            programUserData = ProgramUserData.builder()
                    .programName(programOptional.get().getName())
                    .code(userData.getCode())
                    .email(userData.getEmail())
                    .totalCredits(totalCredits)
                    .doneCredits(doneCredits)
                    .totalProgress((double) (doneCredits * 100) / totalCredits)
                    .build();
        }
        return programUserData;
    }

    @Override
    public List<UserSubjectData> getHistorySubject(Integer programId, String userId) {
        List<SubjectData> subjectData = sinuService.getHistorySubject(userId);
        return mapUserSubjectData(subjectData);
    }

    @Override
    public List<UserSubjectData> getCurrentSubject(Integer programId, String userId) {
        List<SubjectData> subjectData = sinuService.getCurrentSubject(userId);
        return mapUserSubjectData(subjectData);
    }

    private List<UserSubjectData> mapUserSubjectData(List<SubjectData> subjectData) {
        List<UserSubjectData> subjects = new ArrayList<>();
        for (SubjectData data : subjectData) {
            List<Curriculum> curriculumList = curriculumRepository.findAllByCode(data.getCode());
            if (!curriculumList.isEmpty()) {
                subjects.add(
                        UserSubjectData.builder()
                                .subjectId(curriculumList.get(0).getCurriculumId())
                                .name(curriculumList.get(0).getName())
                                .coreId(curriculumList.get(0).getCurrirriculumFatherId().getCurriculumId())
                                .coreName(curriculumList.get(0).getCurrirriculumFatherId().getName())
                                .credits(curriculumList.get(0).getNumberCredits())
                                .qalification(data.getQalification())
                                .period(data.getPeriod())
                                .build()
                );
            }
        }
        return subjects;
    }

    private int sumCurrentCredits(Integer programId, String userId) {
        int currentCredits = 0;
        List<UserSubjectData> subjectData = getHistorySubject(programId, userId);
        for (UserSubjectData data : subjectData) {
            currentCredits += data.getCredits();
        }
        return currentCredits;
    }

    private CurriculumRequest modelSubcore(Curriculum curriculum) {
        return CurriculumRequest.builder()
                .curriculumId(curriculum.getCurriculumId())
                .name(curriculum.getName())
                .type(curriculum.getCurriculumType())
                .numberCredits(curriculum.getNumberCredits())
                .description(curriculum.getDescription())
                .fatherId(Constant.NIFS_CORE)
                .subjectRequest(
                        (curriculum.getComplementarySubject() != null) ?
                                SubjectRequest.builder()
                                        .semester(curriculum.getComplementarySubject().getSemester())
                                        .code(curriculum.getComplementarySubject().getCode())
                                        .hoursInteractionTeacher(curriculum.getComplementarySubject().getHoursInteractionTeacher())
                                        .hourSelfWork(curriculum.getComplementarySubject().getHourSelfWork())
                                        .build()
                                : null
                )
                .build();
    }

    private List<IntegrativeActivityRequest> getActivitiesByCurriculum(Integer curriculumId) {
        return integrativeActivityRepository.findAllByEnabledAndCurriculumId_CurriculumId(true, curriculumId).stream().map(
                integrativeActivity -> IntegrativeActivityRequest.builder()
                        .activityId(integrativeActivity.getIntegrativeActivityId())
                        .tittle(integrativeActivity.getName())
                        .description(integrativeActivity.getDescription())
                        .activity(integrativeActivity.getActivityValue())
                        .build()
        ).toList();
    }

    private Curriculum saveSubCore(SubcoreNifsRequest subcoreNifsRequest) {
        ComplementarySubject complementarySubject = (subcoreNifsRequest.getSubjectRequest().getSubjectRequest() != null) ?
                saveComplementarySubject(subcoreNifsRequest.getSubjectRequest().getSubjectRequest()) : null;
        ComplementaryNifs complementaryNifs = saveComplementaryNifs(subcoreNifsRequest);
        Curriculum savedCurriculum = null;
        Optional<Curriculum> curriculumNif = curriculumRepository.findById(Constant.NIFS_CORE);
        if (curriculumNif.isPresent()) {
            savedCurriculum = curriculumRepository.save(
                    Curriculum.builder()
                            .name(subcoreNifsRequest.getSubjectRequest().getName())
                            .curriculumType(subcoreNifsRequest.getSubjectRequest().getType())
                            .numberCredits(subcoreNifsRequest.getSubjectRequest().getNumberCredits())
                            .description(subcoreNifsRequest.getSubjectRequest().getDescription())
                            .enabled(true)
                            .createdAt(new Date())
                            .createdBy(subcoreNifsRequest.getCreatedBy())
                            .currirriculumFatherId(curriculumNif.get())
                            .complementarySubject(complementarySubject)
                            .complementaryNifs(complementaryNifs)
                            .build()
            );
        }
        return savedCurriculum;
    }

    private List<CurriculumDetailChild> getCurriculumChildDetail(Integer curriculumId) {
        return curriculumRepository.findAllByCurrirriculumFatherId_CurriculumIdAndEnabled(curriculumId, true).stream().map(
                curriculum -> CurriculumDetailChild.builder()
                        .curriculumId(curriculum.getCurriculumId())
                        .name(curriculum.getName())
                        .type(curriculum.getCurriculumType())
                        .creditNumber(curriculum.getNumberCredits())
                        .participation(curriculum.getPercentageParticipation())
                        .build()
        ).toList();
    }

    private int getTotalCredits(List<CurriculumDetailChild> childs) {
        return childs.stream()
                .mapToInt(CurriculumDetailChild::getCreditNumber)
                .sum();
    }

    private double getTotalParticipation(List<CurriculumDetailChild> childs) {
        double participation = childs.stream()
                .mapToDouble(CurriculumDetailChild::getParticipation)
                .sum();
        DecimalFormat df = new DecimalFormat("##.##");
        return Double.parseDouble(df.format(participation));
    }

    private Double calculatePercentage(int total, Integer credit) {
        double result = (credit.doubleValue() * 100) / total;
        DecimalFormat df = new DecimalFormat("##.##");
        return Double.valueOf(df.format(result));
    }

    private ComplementaryCore saveComplementaryCore(String raeg) {
        return complementaryCoreRepository.save(
                ComplementaryCore.builder()
                        .raeg(raeg)
                        .build()
        );
    }

    private ComplementarySubject saveComplementarySubject(SubjectRequest subjectRequest) {
        return complementarySubjectRepository.save(
                ComplementarySubject.builder()
                        .semester(subjectRequest.getSemester())
                        .code(subjectRequest.getCode())
                        .hoursInteractionTeacher(subjectRequest.getHoursInteractionTeacher())
                        .hourSelfWork(subjectRequest.getHourSelfWork())
                        .build()
        );
    }

    private ComplementaryNifs saveComplementaryNifs(SubcoreNifsRequest subcoreNifsRequest) {
        return complementaryNifsRepository.save(
                ComplementaryNifs.builder()
                        .rae(subcoreNifsRequest.getRae())
                        .competences(subcoreNifsRequest.getCompetences())
                        .build()
        );
    }

    private SubjectRequest getComplementarySubject(ComplementarySubject complementarySubject) {
        SubjectRequest subject = null;
        if (complementarySubject != null) {
            subject = SubjectRequest.builder()
                    .semester(complementarySubject.getSemester())
                    .code(complementarySubject.getCode())
                    .hoursInteractionTeacher(complementarySubject.getHoursInteractionTeacher())
                    .hourSelfWork(complementarySubject.getHourSelfWork())
                    .build();
        }
        return subject;
    }

    private CoreModel getComplementaryCore(ComplementaryCore complementaryCore) {
        CoreModel raeg = null;
        if (complementaryCore != null) raeg = CoreModel.builder()
                .raeg(complementaryCore.getRaeg()).build();
        return raeg;
    }

    public void createCurriculumHistory(WorkflowBaseStep workflowBaseStep, String createdBY) {
        CurriculumSemester curriculumSemester = CurriculumSemester.builder()
                .curriculumList(getCurriculum(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId()))
                .semesterList(getSubjectBySemester(workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId()))
                .build();
        historyExtendedService.createHistoric(
                gson.toJson(curriculumSemester),
                Constant.MODULE_PLAN_OF_STUDY_ORGANIZATION,
                workflowBaseStep.getWorkflowBaseId().getWorkflowObjectId(),
                createdBY,
                null);
    }

    private String buildPdf(String template, Program program) {
        List<SubjectSemesterModel> semesters = getSubjectBySemesterWithNif(program.getProgramId());
        template = template.replace(Constant.PROGRAM_FLAG, program.getName().toUpperCase());
        template = template.replace(Constant.SNIES_FLAG, (program.getTraceabilityId() != null) ?
                program.getTraceabilityId().getSinies() : "");
        template = template.replace(Constant.CAMPUS_FLAG, sharedMethods.getCampusValue(campusRepository.findByProgramId_ProgramId(program.getProgramId())));
        template = template.replace(Constant.MODALITY_FLAG, sharedMethods.getModalityValue(modalityRepository.findByProgramId_ProgramId(program.getProgramId())));
        template = template.replace(Constant.TOTAL_SEMESTER_FLAG, String.valueOf(semesters.size()));
        template = template.replace(Constant.CREDIT_NUMBERS_FLAG, String.valueOf(curriculumRepository.sumTotalCurriculums(program.getProgramId())));
        return buildSemester(template, semesters);
    }

    private String buildSemester(String templateOrigin, List<SubjectSemesterModel> semesters) {
        String templateSemester = documentManagerService.getTemplate(Constant.SEMESTER_TEMPLATE);
        String templateSubject = documentManagerService.getTemplate(Constant.SUBJECT_TEMPLATE);
        StringBuilder templateResponse = new StringBuilder();
        int i = 1;
        for (SubjectSemesterModel semesterModel : semesters) {
            if (i % 2 != 0) templateResponse.append("<div class=\"row\">");
            templateResponse.append(replaceSemesterTemplate(templateSemester, templateSubject, semesterModel));
            if (i % 2 == 0) templateResponse.append("</div>");
            i++;
        }
        templateResponse.append("</div>");
        templateOrigin = templateOrigin.replace(Constant.SEMESTER_TABLE_FLAG, templateResponse);

        return templateOrigin;
    }

    private String replaceSemesterTemplate(String templateSemester, String templateSubject, SubjectSemesterModel semesterModel) {
        templateSemester = templateSemester.replace(Constant.SEMESTER_FLAG, convertToRoman(semesterModel.getSemesterNumber()));
        int i = 1;
        StringBuilder subjects = new StringBuilder();
        for (SubjectListModel subject : semesterModel.getSubjectListModel()) {
            subjects.append(replaceSubject(templateSubject, subject, semesterModel.getSubjectListModel().size(), i));
            i++;
        }
        templateSemester = templateSemester.replace(Constant.SUBCORES_DATA_FLAG, subjects);
        return templateSemester;
    }

    private String replaceSubject(String templateSubject, SubjectListModel subject, int size, int i) {
        templateSubject = templateSubject.replace(Constant.SUBCORE_FLAG, subject.getName());
        templateSubject = templateSubject.replace(Constant.CREDIT_FLAG, subject.getCreditNumber().toString());
        templateSubject = (i == size) ?
                templateSubject.replace(Constant.LAST_SUBJECT_FLAG, Constant.LAST_SUBJECT_CSS) :
                templateSubject.replace(Constant.LAST_SUBJECT_FLAG, "");
        return templateSubject;
    }

    private String convertToRoman(int semester) {
        Map<Integer, String> roman = new HashMap<>();
        roman.put(1, "I");
        roman.put(2, "II");
        roman.put(3, "III");
        roman.put(4, "IV");
        roman.put(5, "V");
        roman.put(6, "VI");
        roman.put(7, "VII");
        roman.put(8, "VIII");
        roman.put(9, "IX");
        roman.put(10, "XI");
        roman.put(11, "XII");
        roman.put(12, "XIII");
        roman.put(13, "XIV");
        roman.put(14, "XV");
        roman.put(15, "XVI");
        return roman.get(semester);
    }

    private List<CurriculumModel> getCurriculumWithNif(Integer objectId) {
        List<Curriculum> curriculumList = curriculumRepository.findCurriculumByProgram(objectId);
        Optional<Curriculum> foundCore = curriculumList.stream()
                .filter(curriculum -> curriculum.getIsNif() != null && curriculum.getIsNif().equals(true))
                .findFirst();

        if (foundCore.isPresent()) {
            List<Curriculum> curriculumNifList = curriculumRepository
                    .findAllByCurrirriculumFatherId_CurriculumIdAndEnabled(Constant.NIFS_CORE, true);
            for (Curriculum nif : curriculumNifList) nif.setCurrirriculumFatherId(foundCore.get());
            curriculumList.addAll(curriculumNifList);
        }

        CurriculumMapper mapper = new CurriculumMapper();
        return mapper.mapToCurriculumModel(curriculumList);
    }

    public List<SubjectSemesterModel> getSubjectBySemesterWithNif(Integer programId) {
        List<SubjectSemesterModel> subjectBySemester = new ArrayList<>();
        List<SubjectListModel> subjectList = getSubjectsWithNif(programId);
        Map<Integer, List<SubjectListModel>> subjectsBySemesterMap = subjectList.stream()
                .collect(Collectors.groupingBy(SubjectListModel::getSemester, Collectors.toList()));
        for (Integer key : subjectsBySemesterMap.keySet()) {
            subjectBySemester.add(new SubjectSemesterModel(key, subjectsBySemesterMap.get(key)));
        }
        return subjectBySemester;
    }

    public List<SubjectListModel> getSubjectsWithNif(Integer programId) {
        List<Curriculum> subjects = curriculumRepository.findAllSubjects(programId);
        subjects.addAll(curriculumRepository.findAllByCurrirriculumFatherId_CurriculumIdAndEnabled(Constant.NIFS_CORE, true));
        return subjects.stream().map(
                curriculum -> SubjectListModel.builder()
                        .subjectId(curriculum.getCurriculumId())
                        .name(curriculum.getName())
                        .semester((curriculum.getComplementarySubject() != null) ? curriculum.getComplementarySubject().getSemester() : null)
                        .creditNumber(curriculum.getNumberCredits())
                        .isUpdated(false)
                        .build()
        ).toList();
    }

    private ResponsePage<ProgramSubjectDto> buildPageableSubject(Page<ProgramSubjectDto> programSubjectDtos, PageModel pageModel) {
        return ResponsePage.<ProgramSubjectDto>builder()
                .content(programSubjectDtos.getContent())
                .numberOfPage(pageModel.getPage() + 1)
                .itemsPerPage(pageModel.getSize())
                .itemsOnThisPage(programSubjectDtos.getContent().size())
                .totalNumberPages(programSubjectDtos.getTotalPages())
                .totalNumberItems(programSubjectDtos.getTotalElements())
                .hasNextPage(programSubjectDtos.hasNext())
                .hasPreviousPage(programSubjectDtos.hasPrevious())
                .build();
    }

    private String getCurriculumIds(String userId) {
        StringBuilder ids = new StringBuilder();
        List<String> subjectsCode = sinuService.getSubjectsCodeByUserId(userId).getCode();
        for (Integer id : curriculumRepository.findCurriculumByCode(subjectsCode.toArray(new String[0]))) {
            ids.append(id).append(", ");
        }
        if (!ids.isEmpty()) ids.delete(ids.length() - 2, ids.length());
        return ids.toString();
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

}
