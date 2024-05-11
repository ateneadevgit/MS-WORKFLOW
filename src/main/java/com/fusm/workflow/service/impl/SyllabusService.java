package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.entity.Program;
import com.fusm.workflow.entity.Syllabus;
import com.fusm.workflow.external.ICatalogService;
import com.fusm.workflow.external.IDocumentManagerService;
import com.fusm.workflow.model.SyllabusInformationModel;
import com.fusm.workflow.model.SyllabusRequest;
import com.fusm.workflow.repository.ICampusRepository;
import com.fusm.workflow.repository.ICurriculumRepository;
import com.fusm.workflow.repository.IModalityRepository;
import com.fusm.workflow.repository.IProgramRepository;
import com.fusm.workflow.entity.*;
import com.fusm.workflow.model.SyllabusModel;
import com.fusm.workflow.repository.*;
import com.fusm.workflow.service.IPdfService;
import com.fusm.workflow.service.IProgramService;
import com.fusm.workflow.service.ISyllabusService;
import com.fusm.workflow.util.Constant;
import com.fusm.workflow.util.SharedMethods;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import java.util.Optional;

@Service
public class SyllabusService implements ISyllabusService {

    @Autowired
    private ISyllabusRepository syllabusRepository;

    @Autowired
    private ISyllabusComplementaryInformationRepository syllabusCompInfoRepository;

    @Autowired
    private ICurriculumRepository curriculumRepository;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private IModalityRepository modalityRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private SharedMethods sharedMethods;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private IPdfService pdfService;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private HistoryExtendedService historyService;

    @Autowired
    private ISyllabusObjectRepository syllabusObjectRepository;

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private IProgramService programService;


    public SyllabusInformationModel getPreloadInformation(Integer curriculumId) {

        SyllabusInformationModel syllabusInformationModel = new SyllabusInformationModel();
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);

        if(curriculumOptional.isPresent()){
            Integer workflowObjectId =  (curriculumOptional.get().getWorkflowBaseStepId() != null ) ?
                    curriculumOptional.get().getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId() : 0;
            Optional<Program> programOptional = programRepository.findById(workflowObjectId);
            if (programOptional.isPresent()){
                syllabusInformationModel.setProgramName(programOptional.get().getName());
                syllabusInformationModel.setLevelFormationId(programOptional.get().getLevelFormationId());
                syllabusInformationModel.setFacultyId(programOptional.get().getFacultyId());
                syllabusInformationModel.setCampus(campusRepository.findByProgramId_ProgramId(workflowObjectId));
                syllabusInformationModel.setModalities(modalityRepository.findByProgramId_ProgramId(workflowObjectId));
                syllabusInformationModel.setSubjectCode((curriculumOptional.get().getComplementarySubject() != null) ?
                        curriculumOptional.get().getComplementarySubject().getCode() : "");
                syllabusInformationModel.setCredits(curriculumOptional.get().getNumberCredits());
            }
        }
        return  syllabusInformationModel;
    }

    @Override
    public String syllabusPdf(Integer curriculumId) throws DocumentException {
        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(curriculumId);

        String pdfTemplate = documentManagerService.getTemplate(Constant.SILABO_TEMPLATE);
        SyllabusInformationModel syllabusData = getPreloadInformation(curriculumId);
        if (syllabusData.getProgramName() == null) syllabusData = null;
        List<Syllabus> syllabusList = syllabusRepository.findAllByCurriculumId(curriculumId);
        String pdfRoute = "";

        if (curriculumOptional.isPresent() && !syllabusList.isEmpty()) {
            Curriculum curriculum = curriculumOptional.get();
            Syllabus syllabus = syllabusList.get(0);
            pdfTemplate = buildTemplate(curriculum, syllabus, syllabusData, pdfTemplate);
            pdfRoute = pdfService.savePdf(pdfTemplate);
        }
        return pdfRoute;
    }

    @Override
    public void createSyllabus(SyllabusModel syllabusModel) {

        Optional<WorkflowBaseStep> workflowBaseStepOptional = workflowBaseStepRepository
                .findByWorkflowBaseId_WorkflowBaseIdAndStepId_StepId(syllabusModel.getWorkflowBaseId(), syllabusModel.getStepId());

        Optional<Curriculum> curriculumOptional = curriculumRepository.findById(syllabusModel.getCurriculumId());

        if(workflowBaseStepOptional.isPresent() && curriculumOptional.isPresent()){

            SyllabusComplementaryInformation syllabusCompInfo = saveComplementaryInformation(syllabusModel);

            syllabusRepository.save(
                    Syllabus.builder()
                            .code(syllabusModel.getCode())
                            .approvedDate(syllabusModel.getApprovedDate())
                            .cat(syllabusModel.getCat())
                            .cine(syllabusModel.getCine())
                            .nbc(syllabusModel.getNbc())
                            .attendance(syllabusModel.getAttendance())
                            .modalityObservation(syllabusModel.getModalityObservation())
                            .levelFormationCredits(syllabusModel.getLevelFormationCredits())
                            .levelFormationPrerequisites(syllabusModel.getLevelFormationPrerequisites())
                            .signatureType(syllabusModel.getSignatureType())
                            .signatureTypeObservation(syllabusModel.getSignatureTypeObservation())
                            .createdBy(syllabusModel.getCreatedBy())
                            .createdAt(new Date())
                            .subjectCode(syllabusModel.getSubjectCode())
                            .enabled(true)
                            .complementaryInformationId(syllabusCompInfo)
                            .curriculumId(curriculumOptional.get())
                            .workflowBaseStepId(workflowBaseStepOptional.get())
                            .build()
            );
        }
    }

    @Override
    public void createSyllabusNifs(SyllabusModel syllabusModel, Curriculum curriculum) {
        SyllabusComplementaryInformation syllabusCompInfo = saveComplementaryInformation(syllabusModel);

        Syllabus syllabus = syllabusRepository.save(
                Syllabus.builder()
                        .code(syllabusModel.getCode())
                        .approvedDate(syllabusModel.getApprovedDate())
                        .cat(syllabusModel.getCat())
                        .cine(syllabusModel.getCine())
                        .nbc(syllabusModel.getNbc())
                        .attendance(syllabusModel.getAttendance())
                        .modalityObservation(syllabusModel.getModalityObservation())
                        .levelFormationCredits(syllabusModel.getLevelFormationCredits())
                        .levelFormationPrerequisites(syllabusModel.getLevelFormationPrerequisites())
                        .signatureType(syllabusModel.getSignatureType())
                        .signatureTypeObservation(syllabusModel.getSignatureTypeObservation())
                        .createdBy(syllabusModel.getCreatedBy())
                        .subjectCode(syllabusModel.getSubjectCode())
                        .createdAt(new Date())
                        .enabled(true)
                        .complementaryInformationId(syllabusCompInfo)
                        .curriculumId(curriculum)
                        .build()
        );
        saveSyllabusObject(syllabusModel.getProgramIds(), syllabus, Constant.SYLLABUS_PROGRAM);
        saveSyllabusObject(syllabusModel.getCampusIds(), syllabus, Constant.SYLLABUS_CAMPUS);
        saveSyllabusObject(syllabusModel.getLevelFormationIds(), syllabus, Constant.SYLLABUS_TYPE_FORMATION);
        saveSyllabusObject(syllabusModel.getFacultyIds(), syllabus, Constant.SYLLABUS_FACULTY);
        saveSyllabusObject(syllabusModel.getModalities(), syllabus, Constant.SYLLABUS_MODALITY);
    }

    @Override
    public Boolean getSyllabusExist(Integer curriculumId) {
        boolean exist = false;
        List<Syllabus> syllabusList = syllabusRepository.findAllByCurriculumId(curriculumId);
        if(!syllabusList.isEmpty()){
            exist = true;
        }
        return  exist;
    }

    @Override
    public SyllabusModel getSyllabusByCurriculum(Integer curriculumId) {
        SyllabusModel syllabusModel = new SyllabusModel();
        List<Syllabus> syllabusOptional = syllabusRepository.findAllByCurriculumId(curriculumId);
        if (!syllabusOptional.isEmpty()) {
            Syllabus syllabus = syllabusOptional.get(0);
            syllabusModel = SyllabusModel.builder()
                    .syllabusId(syllabus.getSyllabusId())
                    .code(syllabus.getCode())
                    .approvedDate(syllabus.getApprovedDate())
                    .cat(syllabus.getCat())
                    .cine(syllabus.getCine())
                    .nbc(syllabus.getNbc())
                    .attendance(syllabus.getAttendance())
                    .modalityObservation(syllabus.getModalityObservation())
                    .levelFormationCredits(syllabus.getLevelFormationCredits())
                    .levelFormationPrerequisites(syllabus.getLevelFormationPrerequisites())
                    .signatureType(syllabus.getSignatureType())
                    .signatureTypeObservation(syllabus.getSignatureTypeObservation())
                    .createdBy(syllabus.getCreatedBy())
                    .enabled(syllabus.getEnabled())
                    .curriculumId(curriculumId)
                    .subjectCode(syllabus.getSubjectCode())
                    .subjectConformation(syllabus.getComplementaryInformationId().getSubjectConformation())
                    .subjectContext(syllabus.getComplementaryInformationId().getSubjectContext())
                    .subjectDescription(syllabus.getComplementaryInformationId().getSubjectDescription())
                    .learningGeneral(syllabus.getComplementaryInformationId().getLearningGeneral())
                    .learningSpecific(syllabus.getComplementaryInformationId().getLearningSpecific())
                    .content(syllabus.getComplementaryInformationId().getContent())
                    .pedagogicalPractices(syllabus.getComplementaryInformationId().getPedagogicalPractices())
                    .bibliographyBasic(syllabus.getComplementaryInformationId().getBibliographyBasic())
                    .bibliographyLenguaje(syllabus.getComplementaryInformationId().getBibliographyLenguaje())
                    .bibliographyWeb(syllabus.getComplementaryInformationId().getBibliographyWeb())
                    .programIds(getSyllabusObject(Constant.SYLLABUS_PROGRAM, syllabus.getSyllabusId()))
                    .campusIds(getSyllabusObject(Constant.SYLLABUS_CAMPUS, syllabus.getSyllabusId()))
                    .levelFormationIds(getSyllabusObject(Constant.SYLLABUS_TYPE_FORMATION, syllabus.getSyllabusId()))
                    .facultyIds(getSyllabusObject(Constant.SYLLABUS_FACULTY, syllabus.getSyllabusId()))
                    .modalities(getSyllabusObject(Constant.SYLLABUS_MODALITY, syllabus.getSyllabusId()))
                    .build();
        }
        return syllabusModel;
    }

    @Override
    public void updateSyllabus(Integer syllabusId, SyllabusModel syllabusModel) {
        Optional<Syllabus> syllabusOptional = syllabusRepository.findBySyllabusId(syllabusId);

        if(syllabusOptional.isPresent()){

            SyllabusComplementaryInformation syllabusCompInfo =
                    saveComplementaryInformationExist(syllabusOptional.get().getComplementaryInformationId(), syllabusModel);

            syllabusOptional.get().setComplementaryInformationId(syllabusCompInfo);
            syllabusOptional.get().setUpdatedAt(new Date());
            saveSyllabus(syllabusOptional.get(), syllabusModel);
        }
    }

    @Override
    public void updateSyllabusMassive(List<SyllabusRequest> syllabusRequestList) throws DocumentException {
        for (SyllabusRequest syllabusRequest : syllabusRequestList) {
            List<Syllabus> syllabus = syllabusRepository.findAllByCurriculumId(syllabusRequest.getCurriculumId());

            if (!syllabus.isEmpty()) {
                SyllabusModel syllabusModel = SyllabusModel.builder()
                        .code(syllabusRequest.getCode())
                        .approvedDate(new Date(syllabusRequest.getApprovedDate()))
                        .cat(syllabusRequest.getCat())
                        .cine(syllabusRequest.getCine())
                        .nbc(syllabusRequest.getNbc())
                        .attendance(syllabusRequest.getAttendance())
                        .modalityObservation(syllabusRequest.getModalityObservation())
                        .levelFormationCredits(syllabusRequest.getLevelFormationCredits())
                        .levelFormationPrerequisites(syllabusRequest.getLevelFormationPrerequisites())
                        .signatureType(syllabusRequest.getSignatureType())
                        .signatureTypeObservation(syllabusRequest.getSignatureTypeObservation())
                        .workflowBaseId(syllabusRequest.getWorkflowBaseId())
                        .stepId(syllabusRequest.getStepId())
                        .curriculumId(syllabusRequest.getCurriculumId())
                        .subjectConformation(syllabusRequest.getSubjectConformation())
                        .subjectContext(syllabusRequest.getSubjectContext())
                        .subjectDescription(syllabusRequest.getSubjectDescription())
                        .learningGeneral(syllabusRequest.getLearningGeneral())
                        .learningSpecific(syllabusRequest.getLearningSpecific())
                        .content(syllabusRequest.getContent())
                        .pedagogicalPractices(syllabusRequest.getPedagogicalPractices())
                        .bibliographyBasic(syllabusRequest.getBibliographyBasic())
                        .bibliographyLenguaje(syllabusRequest.getBibliographyLenguaje())
                        .bibliographyWeb(syllabusRequest.getBibliographyWeb())
                                .build();
                updateSyllabus(syllabus.get(0).getSyllabusId(), syllabusModel);
                historyService.createHistoric(
                        syllabusPdf(syllabusModel.getCurriculumId()),
                        Constant.MODULE_SYLLABUS,
                        syllabus.get(0).getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowObjectId(),
                        syllabusRequest.getCreatedBy(),
                        syllabusModel.getCurriculumId());
            }
        }
    }

    private void saveSyllabusObject(List<Integer> objectsIds, Syllabus syllabus, Integer type) {
        if (objectsIds != null) {
            for (Integer id : objectsIds) saveObject(id, type, syllabus);
        }
    }

    private void updateSyllabusObject(List<Integer> objectIds, Integer type, Syllabus syllabus) {
        List<SyllabusObject> syllabusObjectList = syllabusObjectRepository
                .findAllByObjectTypeAndSyllabusId_SyllabusId(type, syllabus.getSyllabusId());

        for (SyllabusObject syllabusObject : syllabusObjectList) {
            if (objectIds.contains(syllabusObject.getObjectId()) && syllabusObject.getEnabled().equals(true)) {
                objectIds.remove(syllabusObject.getObjectId());
            } else if (syllabusObject.getEnabled().equals(false) && objectIds.contains(syllabusObject.getObjectId())) {
                syllabusObject.setEnabled(true);
                syllabusObjectRepository.save(syllabusObject);
                objectIds.remove(syllabusObject.getObjectId());
            } else if (!objectIds.contains(syllabusObject.getObjectId())) {
                syllabusObject.setEnabled(false);
                syllabusObjectRepository.save(syllabusObject);
                objectIds.remove(syllabusObject.getObjectId());
            }
        }
        saveSyllabusObject(objectIds, syllabus, type);
    }

    private List<Integer> getSyllabusObject(Integer type, Integer syllabusId) {
        return syllabusObjectRepository.findAllByObjectTypeAndEnabledAndSyllabusId_SyllabusId
                (type, true, syllabusId).stream().map(
                SyllabusObject::getObjectId
        ).toList();
    }

    private void saveObject(Integer id, Integer type, Syllabus syllabus) {
        syllabusObjectRepository.save(
                SyllabusObject.builder()
                        .objectId(id)
                        .objectType(type)
                        .syllabusId(syllabus)
                        .enabled(true)
                        .build()
        );
    }

    private void saveSyllabus(Syllabus syllabus, SyllabusModel syllabusModel) {

        syllabus.setCode(syllabusModel.getCode());
        syllabus.setApprovedDate(syllabusModel.getApprovedDate());
        syllabus.setCat(syllabusModel.getCat());
        syllabus.setCine(syllabusModel.getCine());
        syllabus.setNbc(syllabusModel.getNbc());
        syllabus.setSubjectCode(syllabusModel.getSubjectCode());
        syllabus.setAttendance(syllabusModel.getAttendance());
        syllabus.setModalityObservation(syllabusModel.getModalityObservation());
        syllabus.setLevelFormationCredits(syllabusModel.getLevelFormationCredits());
        syllabus.setLevelFormationPrerequisites(syllabusModel.getLevelFormationPrerequisites());
        syllabus.setSignatureType(syllabusModel.getSignatureType());

        syllabusRepository.save(syllabus);
        updateSyllabusObject(syllabusModel.getProgramIds(), Constant.SYLLABUS_PROGRAM, syllabus);
        updateSyllabusObject(syllabusModel.getCampusIds(), Constant.SYLLABUS_CAMPUS, syllabus);
        updateSyllabusObject(syllabusModel.getLevelFormationIds(), Constant.SYLLABUS_TYPE_FORMATION, syllabus);
        updateSyllabusObject(syllabusModel.getFacultyIds(), Constant.SYLLABUS_FACULTY, syllabus);
        updateSyllabusObject(syllabusModel.getModalities(), Constant.SYLLABUS_MODALITY, syllabus);
    }

    private SyllabusComplementaryInformation saveComplementaryInformation(SyllabusModel syllabusModel) {
        return syllabusCompInfoRepository.save(
                SyllabusComplementaryInformation.builder()
                        .subjectConformation(syllabusModel.getSubjectConformation())
                        .subjectContext(syllabusModel.getSubjectContext())
                        .subjectDescription(syllabusModel.getSubjectDescription())
                        .learningGeneral(syllabusModel.getLearningGeneral())
                        .learningSpecific(syllabusModel.getLearningSpecific())
                        .content(syllabusModel.getContent())
                        .pedagogicalPractices(syllabusModel.getPedagogicalPractices())
                        .bibliographyBasic(syllabusModel.getBibliographyBasic())
                        .bibliographyLenguaje(syllabusModel.getBibliographyLenguaje())
                        .bibliographyWeb(syllabusModel.getBibliographyWeb())
                        .build()
        );
    }

    private SyllabusComplementaryInformation saveComplementaryInformationExist(
            SyllabusComplementaryInformation syllabusCompInformation, SyllabusModel syllabusModel) {

        syllabusCompInformation.setSubjectConformation(syllabusModel.getSubjectConformation());
        syllabusCompInformation.setSubjectContext(syllabusModel.getSubjectContext());
        syllabusCompInformation.setSubjectDescription(syllabusModel.getSubjectDescription());
        syllabusCompInformation.setLearningGeneral(syllabusModel.getLearningGeneral());
        syllabusCompInformation.setLearningSpecific(syllabusModel.getLearningSpecific());
        syllabusCompInformation.setContent(syllabusModel.getContent());
        syllabusCompInformation.setPedagogicalPractices(syllabusModel.getPedagogicalPractices());
        syllabusCompInformation.setBibliographyBasic(syllabusModel.getBibliographyBasic());
        syllabusCompInformation.setBibliographyLenguaje(syllabusModel.getBibliographyLenguaje());
        syllabusCompInformation.setBibliographyWeb(syllabusModel.getBibliographyWeb());

        return syllabusCompInfoRepository.save(syllabusCompInformation);
    }

    private SyllabusModel mapToSyllabus(Syllabus syllabus) {

        return SyllabusModel.builder()
                .syllabusId(syllabus.getSyllabusId())
                .code(syllabus.getCode())
                .approvedDate(syllabus.getApprovedDate())
                .cat(syllabus.getCat())
                .cine(syllabus.getCine())
                .nbc(syllabus.getNbc())
                .attendance(syllabus.getAttendance())
                .modalityObservation(syllabus.getModalityObservation())
                .levelFormationCredits(syllabus.getLevelFormationCredits())
                .levelFormationPrerequisites(syllabus.getLevelFormationPrerequisites())
                .signatureType(syllabus.getSignatureType())
                .signatureTypeObservation(syllabus.getSignatureTypeObservation())
                .createdBy(syllabus.getCreatedBy())
                .enabled(syllabus.getEnabled())
                .createdAt(syllabus.getCreatedAt())
                .updatedAt(syllabus.getUpdatedAt())
                .subjectConformation(syllabus.getComplementaryInformationId().getSubjectConformation())
                .subjectContext(syllabus.getComplementaryInformationId().getSubjectContext())
                .subjectDescription(syllabus.getComplementaryInformationId().getSubjectDescription())
                .learningGeneral(syllabus.getComplementaryInformationId().getLearningGeneral())
                .learningSpecific(syllabus.getComplementaryInformationId().getLearningSpecific())
                .content(syllabus.getComplementaryInformationId().getContent())
                .pedagogicalPractices(syllabus.getComplementaryInformationId().getPedagogicalPractices())
                .bibliographyBasic(syllabus.getComplementaryInformationId().getBibliographyBasic())
                .bibliographyLenguaje(syllabus.getComplementaryInformationId().getBibliographyLenguaje())
                .bibliographyWeb(syllabus.getComplementaryInformationId().getBibliographyWeb())
                .workflowBaseId(syllabus.getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowBaseId())
                .stepId(syllabus.getWorkflowBaseStepId().getStepId().getStepId())
                .curriculumId(syllabus.getCurriculumId().getCurriculumId())
                .build();
    }

    private String replaceModality(List<Integer> modalities, String pdfTemplate, String modalityType, String flag) {
        boolean hasModality = modalities.contains(sharedMethods.getSettingValue(modalityType));
        pdfTemplate = (hasModality) ? pdfTemplate.replace(flag, "X")
                : pdfTemplate.replace(flag, " ");
        return pdfTemplate;
    }

    private String replaceSubjectType(Integer subjectType, String pdfTemplate, String type, String flag) {
        boolean hasType = subjectType.equals(sharedMethods.getSettingValue(type));
        pdfTemplate = (hasType) ? pdfTemplate.replace(flag, "X")
                : pdfTemplate.replace(flag, " ");
        return pdfTemplate;
    }

    private String buildTemplate(Curriculum curriculum, Syllabus syllabus, SyllabusInformationModel syllabusData, String pdfTemplate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");

        List<Integer> modalities = (syllabusData != null) ? syllabusData.getModalities() :
                getSyllabusObject(Constant.SYLLABUS_MODALITY, syllabus.getSyllabusId());

        pdfTemplate = pdfTemplate.replace(Constant.CORE_FLAG, curriculum.getCurrirriculumFatherId().getName());
        pdfTemplate = pdfTemplate.replace(Constant.SUBJECT_FLAG, curriculum.getName());
        pdfTemplate = pdfTemplate.replace(Constant.SUBJECT_CODE_FLAG, curriculum.getComplementarySubject().getCode());
        pdfTemplate = pdfTemplate.replace(Constant.PROGRAM_FLAG, (syllabusData != null) ? syllabusData.getProgramName()
                : programService.getProgramName(getSyllabusObject(Constant.SYLLABUS_PROGRAM, syllabus.getSyllabusId())));
        pdfTemplate = pdfTemplate.replace(Constant.FACULTY_FLAG, (syllabusData != null) ? catalogService.getCatalogItemValue(syllabusData.getFacultyId())
                : getObjectValue(getSyllabusObject(Constant.SYLLABUS_FACULTY, syllabus.getSyllabusId())));
        pdfTemplate = pdfTemplate.replace(Constant.CAMPUS_FLAG, (syllabusData != null) ? sharedMethods.getCampusValue(syllabusData.getCampus())
                : getObjectValue(getSyllabusObject(Constant.SYLLABUS_CAMPUS, syllabus.getSyllabusId())));
        pdfTemplate = replaceModality(modalities, pdfTemplate, Constant.FACED_MODALITY, Constant.IS_FACED_FLAG);
        pdfTemplate = replaceModality(modalities, pdfTemplate, Constant.DISTANCE_MODALITY, Constant.IS_DISTANCE_FLAG);
        pdfTemplate = replaceModality(modalities, pdfTemplate, Constant.ONLINE_MODALITY, Constant.IS_ONLINE_FLAG);
        pdfTemplate = pdfTemplate.replace(Constant.LEVEL_FORMATION_FLAG, (syllabusData != null) ? catalogService.getCatalogItemValue(syllabusData.getLevelFormationId())
                : getObjectValue(getSyllabusObject(Constant.SYLLABUS_TYPE_FORMATION, syllabus.getSyllabusId())));
        pdfTemplate = pdfTemplate.replace(Constant.CAT_FLAG, syllabus.getCat());
        pdfTemplate = pdfTemplate.replace(Constant.CINE_FLAG, syllabus.getCine());
        pdfTemplate = pdfTemplate.replace(Constant.NBC_FLAG, syllabus.getNbc());
        pdfTemplate = pdfTemplate.replace(Constant.MODALITY_OBSERVATION_FLAG, syllabus.getModalityObservation());
        pdfTemplate = pdfTemplate.replace(Constant.PREREQUIREMENT_FLAG, syllabus.getLevelFormationPrerequisites());
        pdfTemplate = pdfTemplate.replace(Constant.CREDIT_NUMBERS_FLAG, syllabus.getLevelFormationCredits().toString());
        pdfTemplate = replaceSubjectType(syllabus.getSignatureType(), pdfTemplate, Constant.SUBJECT_TEORIC, Constant.IS_TEORIC_FLAG);
        pdfTemplate = replaceSubjectType(syllabus.getSignatureType(), pdfTemplate, Constant.SUBEJCT_PRACTIC, Constant.IS_PRACTICAL_FLAG);
        pdfTemplate = replaceSubjectType(syllabus.getSignatureType(), pdfTemplate, Constant.SUBJECT_TEORIC_PRACTIC, Constant.IS_TEORIC_PRACTICAL_FLAG);
        pdfTemplate = pdfTemplate.replace(Constant.SIGNATURE_TYPE_FLAG, syllabus.getSignatureTypeObservation());
        pdfTemplate = pdfTemplate.replace(Constant.ATTENDANCE_FLAG, syllabus.getAttendance());
        pdfTemplate = pdfTemplate.replace(Constant.CONFORMATION_FLAG, syllabus.getComplementaryInformationId().getSubjectConformation());
        pdfTemplate = pdfTemplate.replace(Constant.CONTEXT_FLAG, syllabus.getComplementaryInformationId().getSubjectContext());
        pdfTemplate = pdfTemplate.replace(Constant.DESCRIPTION_FLAG, syllabus.getComplementaryInformationId().getSubjectDescription());
        pdfTemplate = pdfTemplate.replace(Constant.GENERAL_FLAG, syllabus.getComplementaryInformationId().getLearningGeneral());
        pdfTemplate = pdfTemplate.replace(Constant.SPECIFIC_FLAG, syllabus.getComplementaryInformationId().getLearningSpecific());
        pdfTemplate = pdfTemplate.replace(Constant.CONTENT_FLAG, syllabus.getComplementaryInformationId().getContent());
        pdfTemplate = pdfTemplate.replace(Constant.PRACTICES_FLAG, syllabus.getComplementaryInformationId().getPedagogicalPractices());
        pdfTemplate = pdfTemplate.replace(Constant.BIBLIOGRAPHY_BASIC_FLAG, syllabus.getComplementaryInformationId().getBibliographyBasic());
        pdfTemplate = pdfTemplate.replace(Constant.BIBLIOGRAPHY_LANGUAGE_FLAG, syllabus.getComplementaryInformationId().getBibliographyLenguaje());
        pdfTemplate = pdfTemplate.replace(Constant.BIBLIOGRAPHY_WEB_FLAG, syllabus.getComplementaryInformationId().getBibliographyWeb());
        pdfTemplate = pdfTemplate.replace(Constant.CODE_FLAG, syllabus.getCode());
        pdfTemplate = pdfTemplate.replace(Constant.APPROVED_DATE_FLAG, format.format(syllabus.getApprovedDate()));
        return pdfTemplate;
    }

    public String getObjectValue(List<Integer> campusIdList) {
        StringBuilder campus = new StringBuilder();
        for (Integer id : campusIdList) {
            campus.append(catalogService.getCatalogItemValue(id)).append(", ");
        }
        campus.deleteCharAt(campus.length() - 2);
        return campus.toString();
    }

}
