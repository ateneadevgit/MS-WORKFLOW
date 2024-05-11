package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActionRepository extends JpaRepository<Action, Integer> {

    @Query(
            value = "select * from actions " +
                    "order by id_action asc ",
            nativeQuery = true
    )
    List<Action> getActionsOrdered();

}
