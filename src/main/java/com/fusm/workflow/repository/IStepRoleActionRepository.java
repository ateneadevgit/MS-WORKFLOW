package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.entity.StepRoleAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStepRoleActionRepository extends JpaRepository<StepRoleAction, Integer> {

    List<StepRoleAction> findAllByRoleId(Integer roleId);

    @Query(
            value = "SELECT * from step_role_action " +
                    "join actions " +
                    "on actions.id_action = step_role_action.action_id " +
                    "where step_role_action.role_id = :roleId " +
                    "and step_role_action.step_id = :stepId ",
            nativeQuery = true
    )
    List<StepRoleAction> findActionByRoleAndStepId_StepId(
            @Param("roleId") Integer roleId,
            @Param("stepId") Integer stepId
    );

    @Query(
            value = "SELECT DISTINCT role_id " +
                    "FROM step_role_action WHERE step_id = :stepId ",
            nativeQuery = true
    )
    List<Integer> getRolesRelatedWithStep(
            @Param("stepId") Integer stepId
    );

    List<StepRoleAction> findAllByStepId_StepId(Integer stepId);

    List<StepRoleAction> findAllByRoleIdAndActionId_ActionIdAndStepId_StepId(
            Integer roleId, Integer actionId, Integer stepId);

    List<StepRoleAction> findAllByActionId_ActionIdAndStepId_StepIdAndStepOrder(
            Integer actionId, Integer stepId, Integer stepOrder);

    List<StepRoleAction> findAllByActionId_ActionIdAndStepId_StepId(Integer actionId, Integer stepId);

    @Query(
            value = "SELECT role_id " +
                    "FROM step_role_action " +
                    "join actions " +
                    "on actions.id_action = step_role_action.action_id " +
                    "where actions.id_action = 1 " +
                    "and step_role_action.step_id = :stepId " +
                    "and step_role_action.step_order = ( " +
                    "select max(step_role_action.step_order) " +
                    "from step_role_action " +
                    "where step_role_action.action_id = 1)",
            nativeQuery = true
    )
    List<Integer> findLastEvaluatorsOfStep(
            @Param("stepId") Integer stepId
    );

    @Query(
            value = "SELECT DISTINCT role_id " +
                    "FROM public.step_role_action AS sra1 " +
                    "WHERE action_id = 2 " +
                    "  AND NOT EXISTS ( " +
                    "    SELECT * " +
                    "    FROM public.step_role_action AS sra2 " +
                    "    WHERE sra1.role_id = sra2.role_id " +
                    "      AND sra2.action_id = 1 " +
                    "  ) " +
                    "  and step_id = :stepId",
            nativeQuery = true
    )
    List<Integer> getRolesNotEvaluators(@Param("stepId") Integer stepId);

    @Query(
            value = "SELECT distinct role_id " +
                    "FROM step_role_action " +
                    "WHERE action_id = 1 AND role_id NOT IN ( " +
                    "    SELECT role_id " +
                    "    FROM step_role_action " +
                    "    WHERE action_id = 2 " +
                    ") " +
                    "and step_id = :stepId",
            nativeQuery = true
    )
    List<Integer> getOnlyEvaluators(@Param("stepId") Integer stepId);

    @Query(
            value = "SELECT distinct role_id " +
                    "FROM step_role_action " +
                    "WHERE action_id = 2 AND role_id NOT IN ( " +
                    "    SELECT role_id " +
                    "    FROM step_role_action " +
                    "    WHERE action_id = 1 " +
                    ") " +
                    "and step_id = :stepId",
            nativeQuery = true
    )
    List<Integer> getOnlyLoadFile(@Param("stepId") Integer stepId);

    @Query(
            value = "select distinct role_id from step_role_action " +
                    "where action_id in (34, 7) " +
                    "and step_id = :stepId",
            nativeQuery = true
    )
    List<Integer> getRolesRelatedWithSummary(@Param("stepId") Integer stepId);

    @Query(
            value = "SELECT role_id " +
                    "FROM public.step_role_action " +
                    "where action_id = 34 " +
                    "and step_id = :stepId",
            nativeQuery = true
    )
    List<Integer> getSummaryEvaluators(@Param("stepId") Integer stepId);

}
