package com.fusm.workflow.controller;

import com.fusm.workflow.model.StepByWorkflowModel;
import com.fusm.workflow.service.IWorkflowStepService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con los pasos de los flujos de trabajo de la aplicación
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/workflow-step")
public class WorkFlowStepController {

    @Autowired
    private IWorkflowStepService workflowStepService;


    /**
     * Obtiene los pasos de un flujo de trabajo
     * @param workflowId ID del flujo de trabajo
     * @return lista de flujos de trabajo
     */
    @GetMapping("/{id}")
    private ResponseEntity<List<StepByWorkflowModel>> getStepsByWorkflow(
            @PathVariable("id") Integer workflowId
    ) {
        return ResponseEntity.ok(workflowStepService.getStepsByWorkflow(workflowId));
    }

}
