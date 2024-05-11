package com.fusm.workflow.controller;

import com.fusm.workflow.model.EvaluateStepRequest;
import com.fusm.workflow.model.EvaluateTraceability;
import com.fusm.workflow.service.IWorkflowBaseStepFeedbackService;
import com.fusm.workflow.util.AppRoutes;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Clase que expone los servicios relacionados con la evaluación de los pasos
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/step/feedback")
public class WorkflowBaseStepFeedbackController {

    @Autowired
    private IWorkflowBaseStepFeedbackService workflowBaseStepFeedbackService;


    /**
     * Evalúa un paso
     * @param evaluateStepRequest Modelo que contiene la información para evaluar un paso
     * @return OK
     * @throws DocumentException
     * @throws IOException
     */
    @PostMapping
    public ResponseEntity<String> evaluateStep(
            @RequestBody EvaluateStepRequest evaluateStepRequest
    ) throws DocumentException, IOException {
        workflowBaseStepFeedbackService.evaluateStep(evaluateStepRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evalúa el formulario final del los pasos
     * @param evaluateTraceability Modelo que contiene la información para evaluar la trazabilidad de un programa
     * @param programId ID del programa
     * @return OK
     */
    @PostMapping("/traceability/program-id/{id}")
    public ResponseEntity<String> evaluateTraceability(
            @RequestBody EvaluateTraceability evaluateTraceability,
            @PathVariable("id") Integer programId
            ) {
        workflowBaseStepFeedbackService.evaluateTraceability(evaluateTraceability, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
