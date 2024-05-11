package com.fusm.workflow.repository;

import com.fusm.workflow.entity.IntegrativeActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIntegrativeActivityRepository extends JpaRepository<IntegrativeActivity, Integer> {

    List<IntegrativeActivity> findAllByEnabledAndCurriculumId_CurriculumId(Boolean enabled, Integer curriculumId);

}
