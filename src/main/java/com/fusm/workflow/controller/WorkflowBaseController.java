package com.fusm.workflow.controller;

import com.fusm.workflow.model.UserData;
import com.fusm.workflow.model.WorkflowBaseRequest;
import com.fusm.workflow.service.IWorkflowBaseService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con el flujo de trabajo para la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/base")
public class WorkflowBaseController {

    @Autowired
    private IWorkflowBaseService workflowBaseService;


    /**
     * Crea un flujo de trabajo
     * @param workflowBaseRequest Modelo que contiene la información necesaria para crear un flujo de trabajo
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createWorkflowBase(
            @RequestBody WorkflowBaseRequest workflowBaseRequest
    ) {
        workflowBaseService.createWorkflowBase(workflowBaseRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene si el flujo ha iniciado o no
     * @param programId ID del programa
     * @return TRUE o FALSE
     */
    @GetMapping("/started/{id}")
    public ResponseEntity<Boolean> hasFlowStarted(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(workflowBaseService.hasFlowStarted(programId));
    }

    /**
     * Relaciona un usuario a un flujo de trabajo
     * @param userData Modelo que contiene la información de un usuario
     * @param objectId ID del programa
     * @return OK
     */
    @PostMapping("/relate-user/object-id/{id}")
    public ResponseEntity<String> relateUserToWorkflow(
            @RequestBody UserData userData,
            @PathVariable("id") Integer objectId
            ) {
        workflowBaseService.relateUserToWorkflow(userData, objectId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Relaciona un usuario a un flujo de trabajo padre
     * @param userData Modelo que contiene la información de un usuario
     * @param objectId ID del programa
     * @return OK
     */
    @PostMapping("/relate-user/father/object-id/{id}")
    public ResponseEntity<String> relateUserToWorkflowFather(
            @RequestBody UserData userData,
            @PathVariable("id") Integer objectId
    ) {
        workflowBaseService.relateUserToWorkflowFather(userData, objectId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
