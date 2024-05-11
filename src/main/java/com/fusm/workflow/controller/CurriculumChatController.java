package com.fusm.workflow.controller;

import com.fusm.workflow.model.CurriculumChatRequest;
import com.fusm.workflow.model.GetReviewModel;
import com.fusm.workflow.model.ReviewModel;
import com.fusm.workflow.service.ICurriculumChatService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios del chat del módulo de programas de asignatura
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/curriculum/chat")
public class CurriculumChatController {

    @Autowired
    private ICurriculumChatService curriculumChatService;


    /**
     * Crea un comentario
     * @param curriculumChatRequest Modelo que contiene la información para crear un comentario
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createReview(
            @RequestBody CurriculumChatRequest curriculumChatRequest
            ) {
        curriculumChatService.createReview(curriculumChatRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la lista de comentarios
     * @param getReviewModel Modelo que contiene la información para realizar la consulta
     * @return lista de comentarios
     */
    @PostMapping("/get")
    private ResponseEntity<List<ReviewModel>> getReview(
            @RequestBody GetReviewModel getReviewModel
            ) {
        return ResponseEntity.ok(curriculumChatService.getReviews(getReviewModel));
    }

    /**
     * Lee el comentario
     * @param curriculumChats Modelo que contiene la información necesaria para realizar
     * @return OK
     */
    @PostMapping("/read")
    private ResponseEntity<String> readCurriculumChat(
            @RequestBody List<Integer> curriculumChats
    ) {
        curriculumChatService.readCurriculumChat(curriculumChats);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
