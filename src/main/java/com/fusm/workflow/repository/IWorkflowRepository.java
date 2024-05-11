package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Workflow;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowRepository extends JpaRepository<Workflow, Integer> {

    @Query(
            value = "select * from Workflow " +
                    "order by id_workflow asc ",
            nativeQuery = true
    )
    List<Workflow> finAllOrdered();

}
