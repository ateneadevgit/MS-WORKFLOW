package com.fusm.workflow.repository;

import com.fusm.workflow.dto.UserAssignedToProgram;
import com.fusm.workflow.entity.StepRoleUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStepRoleUserRepository extends JpaRepository<StepRoleUsers, Integer> {

    @Query(
            value = "select " +
                    "distinct " +
                    "workflow_base.workflow_object_id " +
                    "from workflow_base " +
                    "JOIN ( " +
                    " select * , " +
                    " ROW_NUMBER() OVER (PARTITION BY step_role_users.workflow_base_id ORDER BY step_role_users.created_at DESC) AS rn " +
                    " FROM step_role_users " +
                    " where step_role_users.role_user_id = :roleId " +
                    ") step_role_users ON step_role_users.workflow_base_id = workflow_base.id_workflow_base " +
                    "where " +
                    "step_role_users.rn = 1 " +
                    "and step_role_users.user_email = :email ",
            nativeQuery = true
    )
    List<Integer> findObjectByUserPermission(
            @Param("email") String userEmail,
            @Param("roleId") Integer roleId);

    List<StepRoleUsers> findAllByWorkflowBaseId_WorkflowBaseIdAndUserEmailAndEnabled(
            Integer workflowBaseId, String userEmail, Boolean enabled
    );

    @Query(
            value = "select " +
                    "distinct " +
                    "step_role_users.user_email, " +
                    "step_role_users.created_at " +
                    "from workflow_base " +
                    "JOIN ( " +
                    "   select * , " +
                    "   ROW_NUMBER() OVER (PARTITION BY step_role_users.workflow_base_id ORDER BY step_role_users.created_at DESC) AS rn " +
                    "   FROM step_role_users " +
                    "   where step_role_users.role_user_id = :roleId " +
                    ") step_role_users ON step_role_users.workflow_base_id = workflow_base.id_workflow_base " +
                    "where " +
                    " workflow_base.workflow_object_id = :programId " +
                    "ORDER BY step_role_users.created_at desc ",
            nativeQuery = true
    )
    List<UserAssignedToProgram> findUserAssignedToProgram(
            @Param("roleId") Integer roleId,
            @Param("programId") Integer programId
    );

}
