package com.fusm.workflow.repository;

import com.fusm.workflow.entity.AttachLastAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAttachLastActionRepository extends JpaRepository<AttachLastAction, Integer> {
}
