package com.fusm.workflow.controller;

import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.SummaryModel;
import com.fusm.workflow.model.SummaryRequest;
import com.fusm.workflow.service.ISummaryService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios de los resumenes de los pasos de la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/summary")
public class SummaryController {

    @Autowired
    private ISummaryService summaryService;


    /**
     * Crea un resumen
     * @param summaryRequest Modelo que contiene la información para crear un resumen
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createSummary(
            @RequestBody SummaryRequest summaryRequest
            ) {
        summaryService.createSummary(summaryRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el resumen de un paso
     * @param reviewListModel Modelo que ocntien la información necesaria para crear la consulta
     * @return resumen
     */
    @PostMapping("/get")
    private ResponseEntity<SummaryModel> getSummary(
            @RequestBody ReviewListModel reviewListModel
            ) {
        return ResponseEntity.ok(summaryService.getSummary(reviewListModel));
    }

    /**
     * Actualiza un resumen
     * @param summaryRequest Modelo que contiene la información para actualizar el resumen
     * @return OK
     */
    @PutMapping
    private ResponseEntity<String> updateSummary(
            @RequestBody SummaryRequest summaryRequest
    ) {
        summaryService.updateSummary(summaryRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Envía el resumen a evaluar
     * @param summaryId ID del resumen
     * @return OK
     */
    @GetMapping("/{id}")
    private ResponseEntity<String> sendSummaryToEvaluation(
            @PathVariable("id") Integer summaryId
    ) {
        summaryService.sendSummaryToEvaluation(summaryId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los componentes curriculares según un programa
     * @param objectId ID del programa
     * @param type ID del tipo
     * @return resumen
     */
    @GetMapping("/curricular/object-id/{id}/type/{type}")
    private ResponseEntity<SummaryModel> getCurricularComponentByProgram(
            @PathVariable("id") Integer objectId,
            @PathVariable("type") Integer type
    ) {
        return ResponseEntity.ok(summaryService.getSummaryByProgramAndType(objectId, type));
    }

    /**
     * Obtiene si el resumen ya fue evaluado por el rol especificado
     * @param summaryId ID del resumen
     * @param roleId ID del rol
     * @return TRUE o FALSE
     */
    @GetMapping("/{id}/is-evaluated/role-id/{roleId}")
    private ResponseEntity<Boolean> hasAlreadyEvaluated(
            @PathVariable("id") Integer summaryId,
            @PathVariable("roleId") Integer roleId
    ) {
        return ResponseEntity.ok(summaryService.hasAlreadyEvaluated(summaryId, roleId));
    }

    /**
     * Obtiene si el resumen ya fue enviado a revisión
     * @param summaryId ID del resumen
     * @return TRUE o FALSE
     */
    @GetMapping("/{id}/sended")
    private ResponseEntity<Boolean> hasAlreadySendToEvaluation(
            @PathVariable("id") Integer summaryId
    ) {
        return ResponseEntity.ok(summaryService.hasAlreadySendToEvaluation(summaryId));
    }

}
