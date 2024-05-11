package com.fusm.workflow.controller;

import com.fusm.workflow.model.AssignCoordinator;
import com.fusm.workflow.service.ICurriculumCoordinatorService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios para asignar un coordinador a una asignatura
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/curriculum")
public class CurriculumCoordinatorController {

    @Autowired
    private ICurriculumCoordinatorService curriculumCoordinatorService;


    /**
     * Asigna un coordinador a una asignatura
     * @param assignCoordinator Modelo que ocntiene la información del coordinador a asignar
     * @param curriculumId ID de la asignatura
     * @return OK
     */
    @PostMapping("/{id}/coordinator")
    private ResponseEntity<String> assignCoordinator(
            @RequestBody AssignCoordinator assignCoordinator,
            @PathVariable("id") Integer curriculumId
            ) {
        curriculumCoordinatorService.assignCoordinator(assignCoordinator, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la información del coordinador asignado a una asignatura
     * @param curriculumId ID de la asignatura
     * @return coordinador
     */
    @GetMapping("/{id}/coordinator")
    private ResponseEntity<AssignCoordinator> coordinatorAssigned(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(curriculumCoordinatorService.coordinatorAssigned(curriculumId));
    }

}
