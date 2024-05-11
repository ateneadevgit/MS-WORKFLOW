package com.fusm.workflow.repository;

import com.fusm.workflow.dto.CurriculumFatherDto;
import com.fusm.workflow.dto.ProgramSubjectDto;
import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.entity.WorkflowBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ICurriculumRepository extends JpaRepository<Curriculum, Integer> {

    List<Curriculum> findAllByWorkflowBaseStepId_WorkflowBaseStepIdAndEnabled(Integer workflowBaseStepId, Boolean enabled);

    @Query(
            value = "SELECT *  " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "and curriculum.type = :type ",
            nativeQuery = true
    )
    List<Curriculum> findCurriculumByType(
            @Param("objectId") Integer objectId,
            @Param("type") Integer curriculumType
    );

    @Query(
            value = "SELECT  " +
                    "curriculum.id_curriculum, " +
                    "curriculum.name, " +
                    "complementary_subject.code, " +
                    "curriculum.number_credits, " +
                    "exists (select * from syllabus where syllabus.curriculum_id = curriculum.id_curriculum) as has_syllabus " +
                    "FROM workflow_base " +
                    "join workflow_base_step  " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum  " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id  " +
                    "join complementary_subject " +
                    "on complementary_subject.id_complementary_subject = curriculum.complementary_subject_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "and curriculum.curriculum_father_id = :fatherId",
            nativeQuery = true
    )
    List<CurriculumFatherDto> findCurriculumByFatherCurriculumId(
            @Param("objectId") Integer objectId,
            @Param("fatherId") Integer fatherCurriculumId
    );

    @Query(
            value = "SELECT " +
                    "COALESCE(SUM(curriculum.number_credits), 0) " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true ",
            nativeQuery = true
    )
    int sumTotalCurriculums(@Param("objectId") Integer objectId);

    @Query(
            value = "SELECT *  " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "order by curriculum.id_curriculum asc ",
            nativeQuery = true
    )
    List<Curriculum> findCurriculumByProgram(
            @Param("objectId") Integer objectId
    );

    @Query(
            value = "SELECT *  " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "and curriculum.type = 55 " +
                    "order by curriculum.id_curriculum asc ",
            nativeQuery = true
    )
    List<Curriculum> findAllSubjects(@Param("objectId") Integer objectId);

    @Query(
            value = "SELECT *  " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "and curriculum.type not in (53, 55) " +
                    "order by curriculum.id_curriculum asc ",
            nativeQuery = true
    )
    List<Curriculum> findAllWithoutCore(@Param("objectId") Integer objectId);

    @Query(
            value = "SELECT *  " +
                    "FROM workflow_base " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_id = workflow_base.id_workflow_base " +
                    "join curriculum " +
                    "on curriculum.workflow_base_step_id = workflow_base_step.workflow_base_step_id " +
                    "where workflow_object_id = :objectId " +
                    "and curriculum.enabled = true " +
                    "and curriculum.type in (53, 55) " +
                    "order by curriculum.id_curriculum asc ",
            nativeQuery = true
    )
    List<Curriculum> findAllCoreAndCubcore(@Param("objectId") Integer objectId);

    List<Curriculum> findAllByCurrirriculumFatherId_CurriculumIdAndEnabled(Integer curriculumFather, Boolean enabled);

    @Query(
            value = "select * from get_program_subject(:programId, :isActivity, :roleId, :userId, :statusId, :createdBy, :semester, :subject)",
            nativeQuery = true,
            countQuery = "select count(*) from get_program_subject(:programId, :isActivity, :roleId, :userId, :statusId, :createdBy, :semester, :subject)"
    )
    Page<ProgramSubjectDto> findAllProgramSubject(
            Pageable pageable,
            @Param("programId") Integer programId,
            @Param("isActivity") Boolean isActivity,
            @Param("roleId") Integer roleId,
            @Param("userId") String userId,
            @Param("statusId") Integer statusId,
            @Param("createdBy") String createdBy,
            @Param("semester") Integer semester,
            @Param("subject") String subject);


    @Query(
            value = "SELECT * " +
                    "FROM curriculum " +
                    "left join complementary_subject on complementary_subject.id_complementary_subject = curriculum.complementary_subject_id " +
                    "where complementary_subject.code in (:codes)",
            nativeQuery = true
    )
    List<Integer> findCurriculumByCode(
            @Param("codes") String[] codes
    );

    @Query(
            value = "select * from get_teacher_by_subject(:userIds, :subjectId, :roleId, :userId)",
            nativeQuery = true
    )
    List<SubjectTeacherDto> findAllTeacherBySubject(
            @Param("userIds") String userIds,
            @Param("subjectId") Integer subjectId,
            @Param("roleId") Integer roleId,
            @Param("userId") String userId
    );

    @Query(
            value = "SELECT  " +
                    "DISTINCT " +
                    "curriculum.type " +
                    "FROM curriculum " +
                    "left join workflow_base_step on workflow_base_step.workflow_base_step_id = curriculum.workflow_base_step_id " +
                    "left join workflow_base on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :objectId " +
                    "and curriculum.type not in (55, 56) ",
            nativeQuery = true
    )
    List<Integer> findLevelsByProgram(
            @Param("objectId") Integer objectId
    );

    @Query(
            value = "SELECT * " +
                    "FROM curriculum " +
                    "left join workflow_base_step on workflow_base_step.workflow_base_step_id = curriculum.workflow_base_step_id " +
                    "left join workflow_base on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :objectId " +
                    "and curriculum.type = :type ",
            nativeQuery = true
    )
    List<Curriculum> findCurriculumOnLevel(
            @Param("objectId") Integer objectId,
            @Param("type") Integer type
    );

    @Query(
            value = "select * from curriculum " +
                    "left join complementary_subject on complementary_subject.id_complementary_subject = curriculum.complementary_subject_id " +
                    "where complementary_subject.code = :code " +
                    "and curriculum.enabled = true ",
            nativeQuery = true
    )
    List<Curriculum> findAllByCode(
            @Param("code") String code
    );

}
