package com.fusm.workflow.controller;

import com.fusm.workflow.model.CompetenceRequest;
import com.fusm.workflow.service.ICompetenceService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las competencias del banco de problemas
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/competences")
public class CompetenceController {

    @Autowired
    private ICompetenceService competenceService;


    /**
     * Crea un competencia
     * @param competenceRequest Modelo que tiene la información para crear una nueva competencia
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createCompetence(
            @RequestBody CompetenceRequest competenceRequest
            ) {
        competenceService.createCompetence(competenceRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene una lista de competencias
     * @param isNif TRUE o FALSE si son las competencias de un NIF o no
     * @return lista de competencias
     */
    @GetMapping("/is-nif/{is-nif}")
    private ResponseEntity<List<CompetenceRequest>> getCompetences(
            @PathVariable("is-nif") Boolean isNif
    ) {
        return ResponseEntity.ok(competenceService.getCompetences(isNif));
    }

    /**
     * Actualizar competencia
     * @param competenceRequest Modelo que contiene la información para actualizar una competencia
     * @param competenceId ID de la competencia
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateCompetence(
            @RequestBody CompetenceRequest competenceRequest,
            @PathVariable("id") Integer competenceId
    ) {
        competenceService.updateCompetence(competenceRequest, competenceId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
