package com.fusm.workflow.repository;

import com.fusm.workflow.entity.LearningAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILearningAssessmentRepository extends JpaRepository<LearningAssessment, Integer> {

    @Query(
            value = "select * from " +
                    "learning_assessment " +
                    "where curriculum_id = :curriculumId " +
                    "and enabled = true " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<LearningAssessment> findAllByCurriculum(
            @Param("curriculumId") Integer curriculumId
    );

}
