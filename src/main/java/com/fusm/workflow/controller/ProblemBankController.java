package com.fusm.workflow.controller;

import com.fusm.workflow.model.*;
import com.fusm.workflow.service.IProblemBankService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con el banco de problemas
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/problem")
public class ProblemBankController {

    @Autowired
    private IProblemBankService problemBankService;


    /**
     * Crea un nuevo problema en el banco de problemas
     * @param problemBankRequest Modelo que contiene la información para crear un nuevo problema
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createProblemBank(
            @RequestBody ProblemBankRequest problemBankRequest
    ) {
        problemBankService.createProblemBank(problemBankRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el banco de problemas del NIF
     * @param searchModel Modelo que contiene los parámetros de búsqueda para filtrar
     * @return lista de problemas
     */
    @PostMapping("/nif")
    private ResponseEntity<List<ProblemBankModel>> getNifProblemBank(
            @RequestBody SearchModel searchModel
    ) {
        return ResponseEntity.ok(problemBankService.getNifProblemBank(searchModel));
    }

    /**
     * Ontiene el banco de problemas
     * @param searchModel Modelo que contiene los parámetros de búsqueda para filtrar
     * @return lista de problemas
     */
    @PostMapping("/get")
    private ResponseEntity<List<ProblemBankModel>> getProblemBank(
            @RequestBody SearchModel searchModel
    ) {
        return ResponseEntity.ok(problemBankService.getProblemBank(searchModel));
    }

    /**
     * Actualiza un problema
     * @param problemBankRequest Modelo que contiene la información a actualizar
     * @param problemBankId ID del problema
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateProblemBank(
            @RequestBody ProblemBankRequest problemBankRequest,
            @PathVariable("id") Integer problemBankId
    ) {
        problemBankService.updateProblemBank(problemBankRequest, problemBankId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evalúa el problema
     * @param evaluateProposalModel Modelo que contiene la información para evaluar el problema
     * @param problemBankId ID del problema
     * @return OK
     */
    @PostMapping("/{id}/evaluate")
    private ResponseEntity<String> evaluateProblemBank(
            @RequestBody EvaluateProposalModel evaluateProposalModel,
            @PathVariable("id") Integer problemBankId
    ) {
        problemBankService.evaluateProblemBank(evaluateProposalModel, problemBankId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Habilita o deshabilita un problema
     * @param userData Contiene la información del usuario
     * @param problemBankId ID del problema
     * @param enabled TRUE o FALSE en caso que se quiera habilitar o deshabilitar el problema
     * @return OK
     */
    @PostMapping("/{id}/dis-enable/{enabled}")
    private ResponseEntity<String> disableProblemBank(
            @RequestBody UserData userData,
            @PathVariable("id") Integer problemBankId,
            @PathVariable("enabled") Boolean enabled
    ) {
        problemBankService.enableDisableProblemBank(problemBankId, enabled, userData);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
