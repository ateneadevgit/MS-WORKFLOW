package com.fusm.workflow.controller;

import com.fusm.workflow.entity.Action;
import com.fusm.workflow.service.IActionService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las acciones que puede hacer un rol dentro de la creación de un
 * programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/action")
public class ActionController {

    @Autowired
    private IActionService actionService;


    /**
     * Obtiene las acciones
     * @return lista de acciones
     */
    @GetMapping
    private ResponseEntity<List<Action>> getActions() {
        return ResponseEntity.ok(actionService.getActions());
    }

}
