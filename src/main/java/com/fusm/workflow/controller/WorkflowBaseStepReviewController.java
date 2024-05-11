package com.fusm.workflow.controller;

import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.ReviewModel;
import com.fusm.workflow.model.ReviewRequest;
import com.fusm.workflow.service.IWorkflowBaseStepReviewService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios para crear comentarios sobre los paso del flujo de creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/step/review")
public class WorkflowBaseStepReviewController {

    @Autowired
    private IWorkflowBaseStepReviewService workflowBaseStepReviewService;


    /**
     * Crea un comentario
     * @param reviewRequest Modelo que contiene la información para crear el comentario
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createReview(
            @RequestBody ReviewRequest reviewRequest
            ) {
        workflowBaseStepReviewService.createReview(reviewRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los comentarios realizados sobre un paso
     * @param reviewListModel Modelo que contiene la información para realizar la consulta
     * @return lista de comentarios
     */
    @PostMapping("/get")
    public ResponseEntity<List<ReviewModel>> getReviews(
            @RequestBody ReviewListModel reviewListModel
    ) {
        return ResponseEntity.ok(workflowBaseStepReviewService.getReviewByStep(reviewListModel));
    }

}
