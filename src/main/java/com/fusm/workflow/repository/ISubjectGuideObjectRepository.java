package com.fusm.workflow.repository;

import com.fusm.workflow.entity.SubjectGuideObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectGuideObjectRepository extends JpaRepository<SubjectGuideObject, Integer> {

    List<SubjectGuideObject> findAllByEnabledAndObjectTypeAndSubjectGuideId_SubjectGuideId(
            Boolean enabled, Integer objectType, Integer subjectGuideId);

    List<SubjectGuideObject> findAllByObjectTypeAndSubjectGuideId_SubjectGuideId(
            Integer objectType, Integer subjectGuideId);

}
