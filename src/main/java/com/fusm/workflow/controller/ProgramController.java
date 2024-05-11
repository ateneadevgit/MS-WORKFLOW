package com.fusm.workflow.controller;

import com.fusm.workflow.model.EvaluateProposalModel;
import com.fusm.workflow.service.IProgramService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone servicios para la evaluación de una propuesta académica
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE)
public class ProgramController {

    @Autowired
    private IProgramService programService;


    /**
     * Evalúa una propuesta académica
     * @param evaluateProposalModel Modelo que contiene la información para evaluar una propuesta académica
     * @param proposalId ID propuesta
     * @return OK
     */
    @PostMapping("/proposal/{id}")
    public ResponseEntity<String> evaluateProposal(
            @RequestBody EvaluateProposalModel evaluateProposalModel,
            @PathVariable("id") Integer proposalId
            ) {
        programService.evaluateProposal(evaluateProposalModel, proposalId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Declina o desahbilita un programa
     * @param evaluateProposalModel Modelo que contiene la información para declinar o deshabilitar un programa
     * @param programId ID del programa
     * @return OK
     */
    @PutMapping("/decline-disable/program/{id}")
    public ResponseEntity<String> declineOrDisableProgram(
            @RequestBody EvaluateProposalModel evaluateProposalModel,
            @PathVariable("id") Integer programId
    ) {
        programService.declineOrDisableProgram(evaluateProposalModel, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
