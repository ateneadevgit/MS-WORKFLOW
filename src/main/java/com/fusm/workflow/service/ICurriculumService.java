package com.fusm.workflow.service;

import com.fusm.workflow.dto.CurriculumFatherDto;
import com.fusm.workflow.dto.ProgramSubjectDto;
import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.model.*;
import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICurriculumService {

    void createCurriculum(CurriculumListRequest curriculumListRequest);
    List<CurriculumModel> getCurriculum(Integer objectId);
    void updateCurriculum(UpdateCurriculumRequest curriculumRequest, Integer curriculumId);
    void updateNameAndDescriptionOfCurriculum(UpdateNameCurriculumRequest curriculumUpdateRequest);
    void disableCurriculum(Integer curriculumId);
    List<CurriculumListModel> getCurriculumByType(Integer programId, Integer type);
    List<CurriculumFatherDto> getCurriculumByFather(Integer programId, Integer fatherId);
    void calculateParticipationPercentage(Integer programId);
    void updateSubjectCredit(List<SubjectListModel> subjectListModels);
    List<SubjectListModel> getSubjects(Integer programId);
    void updateCurriculumMassive(List<CurriculumModel> curriculumModelList);
    Integer getCurriculumCredits(Integer objectId);
    List<CurriculumModel> getCurriculumWithoutCore(Integer objectId);
    CurriculumModel getCurriculumById(Integer curriculumId);
    List<CoreAndSubcoreModel> getCoreAndSubcore(Integer programId);
    void updateCoreAndSubcoreMassive(List<CoreAndSubcoreModel> coreAndSubcoreModels);
    List<SubjectSemesterModel> getSubjectBySemester(Integer programId);
    CurriculumSemester getCurriculumSemesterByProgram(Integer programId);
    CurriculumDetail getCurriculumDetailById(Integer curriculumId);
    String curriculumPdf(Integer programId) throws DocumentException;
    void createNifsCurriculum(SubcoreNifsRequest subcoreNifsRequest);
    SubcoreNifsModel getNifsCurriculum();
    List<SemesterModel> getSemestersByProgram(Integer programId);
    ResponsePage<ProgramSubjectDto> getProgramSubject(SearchSubject searchSubject, PageModel pageModel);
    List<SubjectTeacherDto> getTeachersBySubject(Integer subjectId, String userId, Integer roleId);
    void updateComplementaryNifs(UpdateComplementaryNifs complementaryNifs, Integer curriculumId);
    List<SelectModel> getLevelsByProgram(Integer programId);
    void createComplementaryEvaluation(ComplementaryEvaluationRequest complementaryEvaluationRequest, Integer curriculumId);
    void updateComplementaryEvaluation(ComplementaryEvaluationRequest complementaryEvaluationRequest, Integer curriculumId);
    List<CurriculumTypeEvaluation> getCurriculumEvaluationByType(Integer programId, Integer type);
    ProgramUserData getProgramProgress(Integer programId, String userId);
    List<UserSubjectData> getHistorySubject(Integer programId, String userId);
    List<UserSubjectData> getCurrentSubject(Integer programId, String userId);

}
