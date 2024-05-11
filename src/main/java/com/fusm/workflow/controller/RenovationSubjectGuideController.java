package com.fusm.workflow.controller;

import com.fusm.workflow.dto.RenovationSubjectGuideDto;
import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.RenovationSubjectGuideRequest;
import com.fusm.workflow.model.UserData;
import com.fusm.workflow.service.IRenovationSubjectGuideService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con la renovación de una guia de asignatura
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/guide")
public class RenovationSubjectGuideController {

    @Autowired
    private IRenovationSubjectGuideService renovationSubjectGuideService;


    /**
     * Crea la renovación de una guía de asignatura
     * @param renovationSubjectGuideRequest Modelo que contiene la información para crear una guia
     * @param subjectGuideId ID guía de asignatura
     * @return OK
     */
    @PostMapping("/{id}/renovation")
    private ResponseEntity<String> createRenovationSubjectGuide(
            @RequestBody RenovationSubjectGuideRequest renovationSubjectGuideRequest,
            @PathVariable("id") Integer subjectGuideId
            ) {
        renovationSubjectGuideService.createRenovationSubjectGuide(renovationSubjectGuideRequest, subjectGuideId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evaluar una guía de asignatura
     * @param evaluateGuide Modelo que permite la evaluación de la guía
     * @param renovationId ID de la renovación
     * @return OK
     */
    @PostMapping("/renovation/{id}")
    private ResponseEntity<String> evaluateRenovationSubjectGuide(
            @RequestBody EvaluateGuide evaluateGuide,
            @PathVariable("id") Integer renovationId
    ) {
        renovationSubjectGuideService.evaluateRenovationSubjectGuide(evaluateGuide, renovationId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtener las renovaciones de una guía de asignatura
     * @param userData Modelo que contiene la información de un usuario
     * @param subjectGuideId ID de la guía
     * @return lista de renovaciones
     */
    @PostMapping("/{id}/renovation/get")
    private ResponseEntity<List<RenovationSubjectGuideDto>> getRenovationsBySubjectGuide(
            @RequestBody UserData userData,
            @PathVariable("id") Integer subjectGuideId
    ) {
        return ResponseEntity.ok(renovationSubjectGuideService.getRenovationsBySubjectGuide(userData, subjectGuideId));
    }

}
