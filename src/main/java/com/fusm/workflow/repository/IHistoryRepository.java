package com.fusm.workflow.repository;

import com.fusm.workflow.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHistoryRepository extends JpaRepository<History, Integer> {

    @Query(
            value = "select * from history " +
                    "where program_id = :programId " +
                    "and program_module_id = :moduleId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<History> findLastVersion(@Param("programId") Integer programId, @Param("moduleId") Integer moduleId);

    @Query(
            value = "select * from history " +
                    "where program_id = :programId " +
                    "and program_module_id = :moduleId " +
                    "and object_type = :type " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<History> findLastVersionWithType(
            @Param("programId") Integer programId,
            @Param("moduleId") Integer moduleId,
            @Param("type") Integer type);

}
