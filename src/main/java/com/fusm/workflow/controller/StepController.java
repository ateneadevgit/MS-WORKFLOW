package com.fusm.workflow.controller;

import com.fusm.workflow.model.StepByIdModel;
import com.fusm.workflow.model.StepRequest;
import com.fusm.workflow.service.IStepService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con los pasos de la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/step")
public class StepController {

    @Autowired
    private IStepService stepService;


    /**
     * Crea un paso
     * @param stepRequest Modelo que contiene la información para crear un nuevo paso
     * @param workflowId ID del flujo de trabajo
     * @return OK
     */
    @PostMapping("/workflow-id/{id}")
    private ResponseEntity<String> createStep(
            @RequestBody StepRequest stepRequest,
            @PathVariable("id") Integer workflowId
    ) {
        stepService.createStep(stepRequest, workflowId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Habilitar o Deshabilitar un paso
     * @param stepId ID del paso
     * @param workflowId ID del flujo de trabajo
     * @param enabled TRUE o FALSE en caso que se quiera habilitar o deshabilitar
     * @return OK
     */
    @DeleteMapping("/{id}/workflow-id/{workflow-id}/enable/{enabled}")
    private ResponseEntity<String> enableDisableStep(
            @PathVariable("id") Integer stepId,
            @PathVariable("workflow-id") Integer workflowId,
            @PathVariable("enabled") Boolean enabled
    ) {
        stepService.enableDisableStep(stepId, workflowId, enabled);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtener paso por su ID
     * @param stepId ID del paso
     * @param workflowId ID del flujo de trabajo
     * @return paso
     */
    @GetMapping("/{id}/workflow-id/{workflow-id}")
    private ResponseEntity<StepByIdModel> getStepById(
            @PathVariable("id") Integer stepId,
            @PathVariable("workflow-id") Integer workflowId
    ) {
        return ResponseEntity.ok(stepService.getStepById(stepId, workflowId));
    }

    /**
     * Actualiza el paso
     * @param stepRequest Modelo que contiene la información a actualizar de un paso
     * @param stepId ID del paso
     * @param workflowId ID del flujo de trabajo
     * @return OK
     */
    @PutMapping("/{id}/workflow-id/{workflow-id}")
    private ResponseEntity<String> updateStep(
            @RequestBody StepRequest stepRequest,
            @PathVariable("id") Integer stepId,
            @PathVariable("workflow-id") Integer workflowId
    ) {
        stepService.updateStep(stepRequest, workflowId, stepId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}