package com.fusm.workflow.controller;

import com.fusm.workflow.model.NifsModel;
import com.fusm.workflow.model.NifsRequest;
import com.fusm.workflow.service.INifsService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las NIF
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/nif")
public class NifsController {

    @Autowired
    private INifsService nifsService;


    /**
     * Obtiene todos los NIFS por el tipo
     * @param type  ID del tipo
     * @return nif
     */
    @GetMapping("/type/{type}")
    private ResponseEntity<NifsModel> getNif(
            @PathVariable("type") Integer type
    ) {
        return ResponseEntity.ok(nifsService.viewNifsData(type));
    }

    /**
     * Crea un nuevo NIF
     * @param nifsRequest Modelo que contiene la información para crear un nuevo NIF
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createNif(
            @RequestBody NifsRequest nifsRequest
            ) {
        nifsService.createNifsData(nifsRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Agrega una nueva sección
     * @param requestList Modelo que contiene las secciones a agregar
     * @param sectionId ID sección
     * @return OK
     */
    @PostMapping("/{id}")
    private ResponseEntity<String> addSection(
            @RequestBody List<NifsRequest> requestList,
            @PathVariable("id") Integer sectionId
            ) {
        nifsService.addSection(requestList, sectionId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza un NIF
     * @param nifsRequest Modelo que contiene la información a actualizar de un NIF
     * @param nifId ID del NIF
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateNif(
            @RequestBody NifsRequest nifsRequest,
            @PathVariable("id") Integer nifId
    ) {
        nifsService.updateNifsData(nifsRequest, nifId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Deshabilita una sección
     * @param nifId ID del NIF
     * @return OK
     */
    @DeleteMapping("/{id}")
    private ResponseEntity<String> disableSection(
            @PathVariable("id") Integer nifId
    ) {
        nifsService.disableSection(nifId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
