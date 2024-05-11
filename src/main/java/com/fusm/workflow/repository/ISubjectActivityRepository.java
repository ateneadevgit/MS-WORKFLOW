package com.fusm.workflow.repository;

import com.fusm.workflow.entity.SubjectGuideActivity;
import com.fusm.workflow.model.SubjectActivityRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectActivityRepository extends JpaRepository<SubjectGuideActivity, Integer> {

    @Query(
            value = "select * from subject_guide_activity " +
                    "where subject_guide_id = :guideId " +
                    "and enabled = true " +
                    "and period = :period " +
                    "order by session asc ",
            nativeQuery = true
    )
    List<SubjectGuideActivity> findAllBySubjectGuideId(
            @Param("guideId") Integer guideId,
            @Param("period") String period);

    @Query(
            value = "select * from subject_guide_activity " +
                    "where teacher in (:teachers) " +
                    "and subject_guide_id = :guideId " +
                    "and enabled = true " +
                    "and period = :period " +
                    "order by session asc ",
            nativeQuery = true
    )
    List<SubjectGuideActivity> findAllActivitiesApproved(
            @Param("guideId") Integer guideId,
            @Param("teachers") String[] teacher,
            @Param("period") String period
    );

    @Query(
            value = "select * from subject_guide_activity " +
                    "where subject_guide_id = :subjectGuideId " +
                    "and teacher = :teacher " +
                    "and period not in (:period) ",
            nativeQuery = true
    )
    List<SubjectGuideActivity> findAllByGuideAndTeacherAndPeriod(
            @Param("subjectGuideId") Integer subjectGuideId,
            @Param("teacher") String teacher,
            @Param("period") String period
    );

}
