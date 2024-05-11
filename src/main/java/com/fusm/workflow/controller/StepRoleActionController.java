package com.fusm.workflow.controller;

import com.fusm.workflow.model.StepRoleActionRequest;
import com.fusm.workflow.service.IStepRoleActionService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con los permisos de un rol sobre un paso en la creación de un
 * programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/step-role-action")
public class StepRoleActionController {

    @Autowired
    private IStepRoleActionService stepRoleActionService;


    /**
     * Obtiene los roles relacionados con un paso
     * @param stepId ID del paso
     * @return lista de roles
     */
    @GetMapping("/step-id/{id}/roles")
    private ResponseEntity<List<Integer>> getRolesRelatedWithStep(
            @PathVariable("id") Integer stepId
    ) {
        return ResponseEntity.ok(stepRoleActionService.getRolesRelatedWithStep(stepId));
    }

    /**
     * Elimina un rol dentro de un paso
     * @param stepId ID del paso
     * @param roleId ID del rol
     * @return OK
     */
    @DeleteMapping("/step-id/{id}/role-id/{role-id}")
    private ResponseEntity<String> deleteRoleFromStep(
            @PathVariable("id") Integer stepId,
            @PathVariable("role-id") Integer roleId
    ) {
        stepRoleActionService.deleteRoleFromStep(roleId, stepId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Agregar un rol con acciones a un paso
     * @param stepId ID del paso
     * @param stepRoleActionRequest Modelo que contiene la información a crear
     * @return OK
     */
    @PostMapping("/step-id/{id}")
    private ResponseEntity<String> addRoleActionToStep(
            @PathVariable("id") Integer stepId,
            @RequestBody StepRoleActionRequest stepRoleActionRequest
    ) {
        stepRoleActionService.addRoleActionToStep(stepId, stepRoleActionRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza las acciones de un rol sobre un paso
     * @param stepId ID del paso
     * @param stepRoleActionRequest Modelo que contiene la información de las acciones
     * @return OK
     */
    @PutMapping("/step-id/{id}")
    private ResponseEntity<String> updateActionToRole(
            @PathVariable("id") Integer stepId,
            @RequestBody StepRoleActionRequest stepRoleActionRequest
    ) {
        stepRoleActionService.updateActionToRole(stepId, stepRoleActionRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
