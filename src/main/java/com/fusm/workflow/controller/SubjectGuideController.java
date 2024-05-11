package com.fusm.workflow.controller;

import com.fusm.workflow.model.*;
import com.fusm.workflow.service.ISubjectGuideService;
import com.fusm.workflow.util.AppRoutes;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con las guias de asignatura
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/guide")
public class SubjectGuideController {

    @Autowired
    private ISubjectGuideService subjectGuideService;


    /**
     * Crea una guía de asignatura
     * @param subjectGuideRequest Modelo que contiene la información para crear una guía
     * @param curriculumId ID de la asignatura
     * @return OK
     */
    @PostMapping("/curriculum-id/{id}")
    private ResponseEntity<String> createSubjectGuide(
            @RequestBody SubjectGuideRequest subjectGuideRequest,
            @PathVariable("id") Integer curriculumId
            ) {
        subjectGuideService.createSubjectGuide(subjectGuideRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza una guía de asignatura
     * @param subjectGuideRequest Modelo que contiene la información a actualizar de la guía
     * @param curriculumId ID de la asignatura
     * @return OK
     */
    @PutMapping("/curriculum-id/{id}")
    private ResponseEntity<String> updateSubjectGuide(
            @RequestBody SubjectGuideRequest subjectGuideRequest,
            @PathVariable("id") Integer curriculumId
    ) {
        subjectGuideService.updateSubjectGuide(subjectGuideRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la guía de asignatura según el ID de un curriculum
     * @param curriculumId ID de la asignatura
     * @param searchTeacher Modelo que contiene la información del docente
     * @return guía de asignatura
     */
    @PostMapping("/curriculum-id/{id}/data")
    private ResponseEntity<SubjectGuideRequest> getSubjectGuide(
            @PathVariable("id") Integer curriculumId,
            @RequestBody SearchTeacher searchTeacher
    ) {
        return ResponseEntity.ok(subjectGuideService.getSubjectGuide(searchTeacher, curriculumId));
    }

    /**
     * Obtiene el PDF de la guía de asignatura
     * @param searchTeacher Modelo que contiene la información del docente
     * @param curriculumId ID de la asignatura
     * @return URL del PDF
     * @throws DocumentException
     */
    @PostMapping("/pdf/curriculum-id/{id}")
    private ResponseEntity<String> getSubjectGuidePdf(
            @RequestBody SearchTeacher searchTeacher,
            @PathVariable("id") Integer curriculumId
    ) throws DocumentException {
        return ResponseEntity.ok(subjectGuideService.getSubjectGuidePdf(searchTeacher, curriculumId));
    }

    /**
     * Evalúa una guía de asignatura
     * @param subjectGuideId ID de la guía de asignatura
     * @param evaluateGuide Modelo que permite la evaluación de la guía
     * @return OK
     */
    @PostMapping("/{id}/evaluate")
    private ResponseEntity<String> evaluateSubjectGuide(
            @PathVariable("id") Integer subjectGuideId,
            @RequestBody EvaluateGuide evaluateGuide
            ) {
        subjectGuideService.evaluateSubejctGuide(evaluateGuide, subjectGuideId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la información precargada de las guías de asignatura
     * @param curriculumId ID de la asignatura
     * @return data a precargar
     */
    @GetMapping("/pre-load/curriculum-id/{id}")
    private ResponseEntity<SubjectGuidePreLoad> getPreloadData(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(subjectGuideService.getPreloadData(curriculumId));
    }

}
