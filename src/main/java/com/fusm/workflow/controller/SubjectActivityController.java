package com.fusm.workflow.controller;

import com.fusm.workflow.model.EvaluateGuide;
import com.fusm.workflow.model.PastActivityRequest;
import com.fusm.workflow.model.SearchTeacher;
import com.fusm.workflow.model.SubjectActivityRequest;
import com.fusm.workflow.service.ISubjectActivityService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las actividades de asignatura
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/guide")
public class SubjectActivityController {

    @Autowired
    private ISubjectActivityService subjectActivityService;


    /**
     * Crea una actividad de asignatura
     * @param subjectActivityRequests Modelo que contiene la información para creación una actividad de asignatura
     * @param subjectguideId ID de la guía de asignatura
     * @return OK
     */
    @PostMapping("/{id}/activity")
    private ResponseEntity<String> createSubjectActivity(
            @RequestBody List<SubjectActivityRequest> subjectActivityRequests,
            @PathVariable("id") Integer subjectguideId
            ) {
        subjectActivityService.createSubjectActivity(subjectActivityRequests, subjectguideId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualizar una actividad de asignatura
     * @param subjectActivityRequests Modelo que contiene la información a actualizar de una actividad de asignatura
     * @param subjectActivityId ID de la actividad
     * @return OK
     */
    @PutMapping("/activity/{id}")
    private ResponseEntity<String> updateSubjectActivity(
            @RequestBody SubjectActivityRequest subjectActivityRequests,
            @PathVariable("id") Integer subjectActivityId
    ) {
        subjectActivityService.updateSubjectActivity(subjectActivityRequests, subjectActivityId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Eliminar una actividad
     * @param subjectActivityId ID de la actividad
     * @return OK
     */
    @DeleteMapping("/activity/{id}")
    private ResponseEntity<String> deleteSubjectActivity(
            @PathVariable("id") Integer subjectActivityId
    ) {
        subjectActivityService.deleteSubjectActivity(subjectActivityId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evaluar una actividad de asignatura
     * @param evaluateGuide Modelo que contiene la información para evaluar la actividad
     * @param subjectguideId ID de la guia de asignatura
     * @return OK
     */
    @PostMapping("/{id}/activity/evaluate")
    private ResponseEntity<String> evaluateSubjectActivity(
            @RequestBody EvaluateGuide evaluateGuide,
            @PathVariable("id") Integer subjectguideId
    ) {
        subjectActivityService.evaluateSubjectActivity(evaluateGuide, subjectguideId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza de forma masiva las actividades de asignatura
     * @param subjectActivityRequests Modelo que contiene los registros a actualizar
     * @param subjectGuideId ID de la guía de asignatura
     * @return OK
     */
    @PutMapping("/{id}/massive")
    private ResponseEntity<String> updateSubjectActivityMassive(
            @RequestBody List<SubjectActivityRequest> subjectActivityRequests,
            @PathVariable("id") Integer subjectGuideId
    ) {
        subjectActivityService.updateSubjectActivityMassive(subjectActivityRequests, subjectGuideId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtener las actividades de semestres pasados según el docente
     * @param searchTeacher Modelo que contiene la información del docente
     * @param subjectGuideId ID de la guía de asignatura
     * @return lista de actividades
     */
    @PostMapping("/{id}/activity/past")
    private ResponseEntity<List<SubjectActivityRequest>> getPastActivities(
            @RequestBody SearchTeacher searchTeacher,
            @PathVariable("id") Integer subjectGuideId
            ) {
        return ResponseEntity.ok(subjectActivityService.getPastActivities(searchTeacher, subjectGuideId));
    }

    /**
     * Agregar actividad pasada al periodo actial
     * @param pastActivityRequests Lista de actividades pasadas a agregar
     * @return OK
     */
    @PostMapping("/activity/past")
    private ResponseEntity<String> addPastActivityToCurrentPeriod(
            @RequestBody List<PastActivityRequest> pastActivityRequests
    ) {
        subjectActivityService.addPastActivityToCurrentPeriod(pastActivityRequests);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Resetea las actividades
     * @return OK
     */
    @GetMapping("/reset")
    private ResponseEntity<String> resetActivities() {
        subjectActivityService.resetActivities();
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
