package com.fusm.workflow.repository;

import com.fusm.workflow.dto.AttachmentDto;
import com.fusm.workflow.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAttachRepository extends JpaRepository<Attach, Integer> {

    @Query(
            value = "select * from get_attachment_by_step(:workflowId, :stepId, :hasRestriction, :isOwn, :roleId)",
            nativeQuery = true
    )
    List<AttachmentDto> findAttachByWorkflowAndStep(
            @Param("workflowId") Integer workflowBaseId,
            @Param("stepId") Integer stepId,
            @Param("hasRestriction") Boolean hasRestriction,
            @Param("isOwn") Boolean isOwn,
            @Param("roleId") Integer roleId
    );

    List<Attach> findAllByAttachFather_AttachId(Integer attachId);

    List<Attach> findAllByWorkflowBaseStepId_WorkflowBaseStepIdAndIsDeclined(
            Integer workflowBaseStepId, Boolean isDeclined
    );

    @Query(
            value = "select * from " +
                    "attach where referenced_attach = :referencedId " +
                    "and attach_father is null",
            nativeQuery = true
    )
    List<Attach> findAllByReferencedWithoutFather(
        @Param("referencedId") Integer referenceId
    );

}
