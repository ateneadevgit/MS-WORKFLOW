package com.fusm.workflow.controller;

import com.fusm.workflow.dto.UserAssignedToProgram;
import com.fusm.workflow.service.IStepRoleUserService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las acciones que puede realizar un usuario segpun un paso
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/user")
public class StepRoleUserController {

    @Autowired
    private IStepRoleUserService stepRoleUserService;


    /**
     * Obtener los permisos de un usuario en un flujo de trabajo
     * @param userEmail Email del usuario
     * @param roleId ID del rol
     * @return lista de permisos
     */
    @GetMapping("/{email}/role-id/{id}")
    public ResponseEntity<List<Integer>> getUserWithPermissionInWorkflow(
            @PathVariable("email") String userEmail,
            @PathVariable("id") Integer roleId
    ) {
        return ResponseEntity.ok(stepRoleUserService.getUserWithPermissionInWorkflow(userEmail, roleId));
    }

    /**
     * Obtiene los programas a los que tiene acceso un rol
     * @param objectId ID del programa
     * @param roleId ID del rol
     * @return lista de usuarios asignados al programa
     */
    @GetMapping("/object-id/{objectId}/role-id/{id}")
    public ResponseEntity<List<UserAssignedToProgram>> getUserRelatedWithProgram(
            @PathVariable("objectId") Integer objectId,
            @PathVariable("id") Integer roleId
    ) {
        return ResponseEntity.ok(stepRoleUserService.getUserRelatedWithProgram(objectId, roleId));
    }

}
