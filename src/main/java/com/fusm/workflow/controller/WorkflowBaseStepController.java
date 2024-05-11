package com.fusm.workflow.controller;

import com.fusm.workflow.model.SendEvaluationRequest;
import com.fusm.workflow.model.StepAttachRequest;
import com.fusm.workflow.model.WorkflowStepModel;
import com.fusm.workflow.model.WorkflowStepRequest;
import com.fusm.workflow.service.IWorkflowBaseStepService;
import com.fusm.workflow.util.AppRoutes;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Clase que expone los servicios relacionados con los pasos y flujo de trabajo para la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/step")
public class WorkflowBaseStepController {

    @Autowired
    private IWorkflowBaseStepService workflowBaseStepService;


    /**
     * Obtiene los pasos de un flujo de trabajo
     * @param objectId ID del programa
     * @param workflowStepRequest Modelo que contiene la información para realizar la consulta
     * @return flujo de trabajo
     */
    @PostMapping("/by-object/{id}")
    public ResponseEntity<WorkflowStepModel> getSteps(
            @PathVariable("id") Integer objectId,
            @RequestBody WorkflowStepRequest workflowStepRequest
            ) {
        return ResponseEntity.ok(workflowBaseStepService.getStepsOfWorkflowByRole(objectId, workflowStepRequest));
    }

    /**
     * Carga un anexo al flujo
     * @param stepAttachRequest Modelo que contiene la información para cargar un anexo
     * @return OK
     * @throws DocumentException
     * @throws IOException
     */
    @PostMapping("/attach")
    public ResponseEntity<String> loadAttachment(
            @RequestBody StepAttachRequest stepAttachRequest
            ) throws DocumentException, IOException {
        workflowBaseStepService.loadAttachment(stepAttachRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Elimina un anexo al flujo de trabajo
     * @param attachId ID del anexo
     * @return OK
     */
    @DeleteMapping("/attach/{id}")
    public ResponseEntity<String> disableAttachment(
            @PathVariable("id") Integer attachId
    ) {
        workflowBaseStepService.disableAttachment(attachId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Envia paso a evaluación
     * @param sendEvaluationRequest Modelo que contiene la información para enviar un paso a evaluaicón
     * @return OK
     * @throws DocumentException
     * @throws IOException
     */
    @PostMapping("/send/evaluation")
    public ResponseEntity<String> sendStepToEvaluation(
            @RequestBody SendEvaluationRequest sendEvaluationRequest
            ) throws DocumentException, IOException {
        workflowBaseStepService.sendStepToEvaluation(sendEvaluationRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
