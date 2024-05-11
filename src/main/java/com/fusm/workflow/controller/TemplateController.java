package com.fusm.workflow.controller;

import com.fusm.workflow.model.TemplateRequest;
import com.fusm.workflow.service.ITemplateService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con los anexos mínimos requeridos de los pasos
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/template")
public class TemplateController {

    @Autowired
    private ITemplateService templateService;


    /**
     * Crea un anexo mínimo requerido del paso
     * @param templateRequest Modelo que contiene la información para crear un anexo mínimo
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createTemplate(
            @RequestBody TemplateRequest templateRequest
            ) {
        templateService.createTemplate(templateRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza un anexo mínimo requerido
     * @param templateRequest Modelo que contiene la información a actualizar de un anexo mínimo
     * @param templateId ID del anexo
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateTemplate(
            @RequestBody TemplateRequest templateRequest,
            @PathVariable("id") Integer templateId
    ) {
        templateService.updateTemplate(templateRequest, templateId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
