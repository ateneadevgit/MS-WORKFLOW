package com.fusm.workflow.repository;

import com.fusm.workflow.entity.RenovationSubjectGuideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRenovationSubjectGuideStatusRepository extends JpaRepository<RenovationSubjectGuideStatus, Integer> {
}
