package com.fusm.workflow.repository;

import com.fusm.workflow.entity.SubjectGuideActivity;
import com.fusm.workflow.entity.SubjectGuideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISubjectGuideStatusRepository extends JpaRepository<SubjectGuideStatus, Integer> {

    @Query(
            value = "select * from subject_guide_status " +
                    "where status_type = 128 " +
                    "and subject_guide_id = :guideId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<SubjectGuideStatus> findLastStatus(
            @Param("guideId") Integer guideId
    );

    @Query(
            value = "select * from ( " +
                    "   SELECT * , " +
                    "        ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY subject_guide_status.created_at DESC) AS rn " +
                    "        FROM subject_guide_status " +
                    "   where status_type = 130 " +
                    ") subject_guide_status " +
                    "where subject_guide_status.rn = 1 ",
            nativeQuery = true
    )
    List<SubjectGuideStatus> findUsersToReset();

}
