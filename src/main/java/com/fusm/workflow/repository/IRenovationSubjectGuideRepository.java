package com.fusm.workflow.repository;

import com.fusm.workflow.dto.RenovationSubjectGuideDto;
import com.fusm.workflow.entity.RenovationSubjectGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRenovationSubjectGuideRepository extends JpaRepository<RenovationSubjectGuide, Integer> {

    @Query(
            value = "select * from get_renovation_subject_guide(:roleId, :subjectGuideId)",
            nativeQuery = true
    )
    List<RenovationSubjectGuideDto> findAllRenovation(
            @Param("roleId") Integer roleId,
            @Param("subjectGuideId") Integer subjectGuideId
    );

}
