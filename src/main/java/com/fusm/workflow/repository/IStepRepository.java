package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStepRepository extends JpaRepository<Step, Integer> {

    List<Step> findAllByIsMandatory(Boolean isMandatory);

}
