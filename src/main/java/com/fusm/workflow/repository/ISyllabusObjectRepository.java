package com.fusm.workflow.repository;

import com.fusm.workflow.entity.SyllabusObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISyllabusObjectRepository extends JpaRepository<SyllabusObject, Integer> {

    List<SyllabusObject> findAllByObjectTypeAndEnabledAndSyllabusId_SyllabusId(
            Integer type, Boolean enabled, Integer syllabusId);

    List<SyllabusObject> findAllByObjectTypeAndSyllabusId_SyllabusId(
            Integer type, Integer syllabusId);

}
