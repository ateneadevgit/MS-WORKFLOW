package com.fusm.workflow.repository;

import com.fusm.workflow.entity.CurriculumChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumChatRepository extends JpaRepository<CurriculumChat, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM curriculum_chat " +
                    "where object_id = :objectId " +
                    "and object_type = :objectType " +
                    "and ( " +
                    "   sender = :userId " +
                    "   or receptor = :userId " +
                    ") " +
                    "order by created_at asc",
            nativeQuery = true
    )
    List<CurriculumChat> findAllChatsByUser(
            @Param("objectId") Integer objectId,
            @Param("objectType") Integer objectType,
            @Param("userId") String userId
    );

}
