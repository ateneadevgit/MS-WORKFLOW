package com.fusm.workflow.repository;

import com.fusm.workflow.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITemplateRepository extends JpaRepository<Template, Integer> {

    @Query(
            value = "select * from template " +
                    "where step_id = :stepId " +
                    "and enabled = true " +
                    "order by template_name asc ",
            nativeQuery = true
    )
    List<Template> findByStepId(@Param("stepId") Integer stepId);
    List<Template> findAllByTemplateFatherId_TemplateId(Integer templateId);

}
