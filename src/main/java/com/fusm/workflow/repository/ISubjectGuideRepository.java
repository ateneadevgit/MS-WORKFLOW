package com.fusm.workflow.repository;

import com.fusm.workflow.entity.SubjectGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectGuideRepository extends JpaRepository<SubjectGuide, Integer> {

    @Query(
            value = "select * from " +
                    "subject_guide where " +
                    "curriculum_id = :id " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<SubjectGuide> findAllByCurriculumId(@Param("id") Integer curriculumId);

}
