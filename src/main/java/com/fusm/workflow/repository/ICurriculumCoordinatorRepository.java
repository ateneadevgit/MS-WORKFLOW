package com.fusm.workflow.repository;

import com.fusm.workflow.entity.CurriculumCoordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumCoordinatorRepository extends JpaRepository<CurriculumCoordinator, Integer> {

    @Query(
            value = "select * " +
                    "from curriculum_coordinator " +
                    "where curriculum_id = :curriculumId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<CurriculumCoordinator> findAssignedCoordinator(
            @Param("curriculumId") Integer curriculumId
    );

}
