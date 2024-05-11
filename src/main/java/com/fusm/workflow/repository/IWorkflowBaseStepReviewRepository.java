package com.fusm.workflow.repository;

import com.fusm.workflow.entity.WorkflowBaseStepReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowBaseStepReviewRepository extends JpaRepository<WorkflowBaseStepReview, Integer> {


    @Query(
            value = "select * " +
                    "from workflow_base_step_review " +
                    "where workflow_base_step_id = :workflowBaseStepId " +
                    "order by created_at asc",
            nativeQuery = true
    )
    List<WorkflowBaseStepReview> getReviewByStep(@Param("workflowBaseStepId") Integer workflowBaseStepId);


    @Query(
            value = "SELECT * " +
                    "FROM public.workflow_base_step_review " +
                    "where (role_id in (:rolesId)" +
                    "and workflow_base_step_id = :workflowBaseStepId) " +
                    "or replied_to in ( " +
                    "   select id_step_review " +
                    "   FROM public.workflow_base_step_review " +
                    "   where role_id in (:rolesId) " +
                    "   and workflow_base_step_id = :workflowBaseStepId) " +
                    "order by created_at asc",
            nativeQuery = true
    )
    List<WorkflowBaseStepReview> getOwnAndReplyReviewByStep(
            @Param("workflowBaseStepId") Integer workflowBaseStepId,
            @Param("rolesId") Integer[] rolesId
    );

    @Query(
            value = "SELECT * " +
                    "FROM public.workflow_base_step_review " +
                    "where (role_id not in (:rolesId)" +
                    "and workflow_base_step_id = :workflowBaseStepId) " +
                    "or replied_to not in ( " +
                    "   select id_step_review " +
                    "   FROM public.workflow_base_step_review " +
                    "   where role_id in (:rolesId) " +
                    "   and workflow_base_step_id = :workflowBaseStepId) " +
                    "order by created_at asc",
            nativeQuery = true
    )
    List<WorkflowBaseStepReview> getEvaluatorsReviewByStep(
            @Param("workflowBaseStepId") Integer workflowBaseStepId,
            @Param("rolesId") Integer[] rolesId
    );

}
