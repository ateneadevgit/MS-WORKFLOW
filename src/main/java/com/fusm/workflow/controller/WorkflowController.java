package com.fusm.workflow.controller;

import com.fusm.workflow.entity.Workflow;
import com.fusm.workflow.model.WorkflowRequest;
import com.fusm.workflow.service.IWorkflowService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios reklacionados con los flujos de trabajos dentro de la aplicación
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/workflow")
public class WorkflowController {

    @Autowired
    private IWorkflowService workflowService;


    /**
     * Obtiene los flujos de trabajo
     * @return lista de flujos de trabajo
     */
    @GetMapping
    private ResponseEntity<List<Workflow>> getWorkflows() {
        return ResponseEntity.ok(workflowService.getWorkflows());
    }

    /**
     * Actualiza un flujo de trabajo
     * @param workflowId ID del flujo de trabajo
     * @param workflowRequest Modelo que contiene la información necesaria para ctualizar un flujo de trabajo
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateWorkflow(
            @PathVariable("id") Integer workflowId,
            @RequestBody WorkflowRequest workflowRequest
            ) {
        workflowService.updateWorkflow(workflowRequest, workflowId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
