package com.fusm.workflow.controller;

import com.fusm.workflow.model.CurriculumSummaryModel;
import com.fusm.workflow.model.CurriculumSummaryRequest;
import com.fusm.workflow.model.ReviewListModel;
import com.fusm.workflow.model.UpdateCurriculumSummary;
import com.fusm.workflow.service.ICurriculumSummaryService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con los resumenes específicos de la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/curriculum/summary")
public class CurriculumSummaryController {

    @Autowired
    private ICurriculumSummaryService curriculumSummaryService;


    /**
     * Crea un resumen específico
     * @param curriculumSummaryRequest Modelo que contiene la información para crear un resumen específico
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createCurriculumSummary(
            @RequestBody CurriculumSummaryRequest curriculumSummaryRequest
            ) {
        curriculumSummaryService.createCurruculumSummary(curriculumSummaryRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Crea un resumen específico registrandolo en el histórico
     * @param curriculumSummaryRequest Modelo que contiene la información para crear un resumen específico
     * @return OK
     */
    @PostMapping("/with-history")
    public ResponseEntity<String> createCurriculumSummaryWithHistory(
            @RequestBody CurriculumSummaryRequest curriculumSummaryRequest
    ) {
        curriculumSummaryService.createCurruculumSummaryWithHistoric(curriculumSummaryRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtener el resumen específico
     * @param reviewListModel Modelo que contiene información para filtrar la consulta
     * @param curriculumType ID del tipo
     * @return resumen específico
     */
    @PostMapping("/{type}/get")
    public ResponseEntity<CurriculumSummaryModel> getCurriculumSummary(
            @RequestBody ReviewListModel reviewListModel,
            @PathVariable("type") Integer curriculumType
            ) {
        return ResponseEntity.ok(curriculumSummaryService.getCurriculumSummary(reviewListModel, curriculumType));
    }

    /**
     * Actualizar el resumen específico
     * @param updateCurriculumSummary Modelo que contiene información para actualizar el resumen específico
     * @param curriculumId ID del resumen específico
     * @return OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCurriculumSummary(
            @RequestBody UpdateCurriculumSummary updateCurriculumSummary,
            @PathVariable("id") Integer curriculumId
    ) {
        curriculumSummaryService.updateCurruculumSummary(updateCurriculumSummary, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el resumen específico según el programa y el tipo
     * @param typeId ID del tipo
     * @param objectId ID del programa
     * @return resumen específico
     */
    @GetMapping("/type/{type}/object-id/{id}")
    public ResponseEntity<CurriculumSummaryModel> getCurriculumSummaryByProgram(
            @PathVariable("type") Integer typeId,
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumSummaryService.getCurriculumSummaryByProgram(objectId, typeId));
    }

}
