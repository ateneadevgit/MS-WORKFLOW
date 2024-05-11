package com.fusm.workflow.controller;

import com.fusm.workflow.model.IntegrativeActivityRequest;
import com.fusm.workflow.service.IIntegrativeActivityService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que contiene toda la lógica relacionada con las actividades integradoras
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/activity")
public class IntegrativeActivityController {

    @Autowired
    private IIntegrativeActivityService integrativeActivityService;


    /**
     * Actualizar actividad integradora
     * @param activityRequest Modelo que contiene la información para actualizar una actividad integradora
     * @param activityId ID de la actividad
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateActivity(
            @RequestBody IntegrativeActivityRequest activityRequest,
            @PathVariable("id") Integer activityId
            ) {
        integrativeActivityService.updateActivity(activityRequest, activityId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Dehabilita una actividad integradora
     * @param activityId ID de la actividad
     * @return OK
     */
    @DeleteMapping("/{id}")
    private ResponseEntity<String> disableActivity(
            @PathVariable("id") Integer activityId
    ) {
        integrativeActivityService.disableActivity(activityId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Crea una actividad integradora
     * @param integrativeActivityRequests Modelo que contiene la información para crear una actividad integradora
     * @param curriculumId ID del nivel
     * @return OK
     */
    @PostMapping("/curriculum-id/{id}")
    private ResponseEntity<String> createActivity(
            @RequestBody List<IntegrativeActivityRequest> integrativeActivityRequests,
            @PathVariable("id") Integer curriculumId
    ) {
        integrativeActivityService.createActivity(integrativeActivityRequests, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
