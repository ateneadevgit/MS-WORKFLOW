package com.fusm.workflow.controller;

import com.fusm.workflow.model.SyllabusInformationModel;
import com.fusm.workflow.model.SyllabusModel;
import com.fusm.workflow.model.SyllabusRequest;
import com.fusm.workflow.service.ISyllabusService;
import com.fusm.workflow.util.AppRoutes;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Clase que expone los servicios relacionados con el silabos
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/syllabus")
public class SyllabusController {

    @Autowired
    private ISyllabusService syllabusService;


    /**
     * Obtiene la información precargada del silabos
     * @param curriculumId ID de la asignatura
     * @return data a precargar
     */
    @GetMapping("/preload-information/{curriculumId}")
    public ResponseEntity<SyllabusInformationModel> getPreloadInformation(
            @PathVariable("curriculumId") Integer curriculumId
    ) {
        return ResponseEntity.ok(syllabusService.getPreloadInformation(curriculumId));
    }

    /**
     * Crea el silabos
     * @param syllabusModel Modelo que contiene la información para crear un silabos
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createSyllabus(
            @RequestBody SyllabusModel syllabusModel
    ) {
        syllabusService.createSyllabus(syllabusModel);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el PDF del silabos según una asignatura
     * @param curriculumId ID de la asignatura
     * @return URL del pdf
     * @throws DocumentException
     * @throws IOException
     */
    @GetMapping("/curriculum-id/{id}")
    public ResponseEntity<String> getSyllabus(
            @PathVariable("id") Integer curriculumId
    ) throws DocumentException, IOException {
        return ResponseEntity.ok(syllabusService.syllabusPdf(curriculumId));
    }

    /**
     * Obtiene la información del silabos
     * @param curriculumId ID de la asignatura
     * @return silabos
     */
    @GetMapping("/data/curriculum-id/{id}")
    public ResponseEntity<SyllabusModel> getSyllabusData(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(syllabusService.getSyllabusByCurriculum(curriculumId));
    }

    /**
     * Obtiene si el silabos ya existe o no
     * @param curriculumId ID de la asignatura
     * @return TRUE o FALSE
     */
    @GetMapping("/exist/curriculum-id/{id}")
    public ResponseEntity<Boolean> getSyllabusExist(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(syllabusService.getSyllabusExist(curriculumId));
    }

    /**
     * Actualiza el silabos
     * @param syllabusId ID del silabos
     * @param syllabusModel Modelo que contiene la información a actualizar del silabos
     * @return OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSyllabus(
            @PathVariable("id") Integer syllabusId,
            @RequestBody SyllabusModel syllabusModel
    ) {
        syllabusService.updateSyllabus(syllabusId, syllabusModel);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza el silabos de forma masiva
     * @param syllabusRequestList Lista de modelos a actualizar
     * @return OK
     * @throws DocumentException
     * @throws IOException
     */
    @PutMapping("/massive")
    public ResponseEntity<String> updateSyllabus(
            @RequestBody List<SyllabusRequest> syllabusRequestList
            ) throws DocumentException, IOException {
        syllabusService.updateSyllabusMassive(syllabusRequestList);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
