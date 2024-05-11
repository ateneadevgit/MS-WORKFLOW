package com.fusm.workflow.controller;

import com.fusm.workflow.model.FileModel;
import com.fusm.workflow.model.LearningAssessmentModel;
import com.fusm.workflow.model.LearningAssessmentRequest;
import com.fusm.workflow.service.ILearningAssessmentService;
import com.fusm.workflow.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las evaluaciones de aprendizaje
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/learning")
public class LearningAssessmentController {

    @Autowired
    private ILearningAssessmentService learningAssessmentService;


    /**
     * Obntiene las evaluaciones de aprendizaje según un nivel de la estructura curricular
     * @param curriculumId ID del nivel
     * @return lista de evaluaciones
     */
    @GetMapping("/curriculum-id/{id}")
    private ResponseEntity<LearningAssessmentModel> getLearningAssessmentByCurriculumId(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(learningAssessmentService.getLearningAssessmentByCurriculumId(curriculumId));
    }

    /**
     * Crea una evaluación de aprendizaje
     * @param learningAssessmentRequest Modelo que contiene la información para crear una evaluación de aprendizaje
     * @param curriculumId ID del nivel
     * @return OK
     */
    @PostMapping("/curriculum-id/{id}")
    private ResponseEntity<String> createLearningAssessment(
            @RequestBody LearningAssessmentRequest<FileModel> learningAssessmentRequest,
            @PathVariable("id") Integer curriculumId
    ) {
        learningAssessmentService.createLearningAssessment(learningAssessmentRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualizar una evaluación de aprendizaje
     * @param learningAssessmentRequest Modelo que contiene la información a actualizar de la evaluación de aprendizaje
     * @param learningAssessmentId ID de la evaluación
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateLearningAssessment(
            @RequestBody LearningAssessmentRequest<FileModel> learningAssessmentRequest,
            @PathVariable("id") Integer learningAssessmentId
    ) {
        learningAssessmentService.updateLearningAssessment(learningAssessmentRequest, learningAssessmentId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Deshabilita una evaluación de aprendizaje
     * @param learningAssessmentId ID de la evaluación
     * @return OK
     */
    @DeleteMapping("/{id}")
    private ResponseEntity<String> disableLearningAssessment(
            @PathVariable("id") Integer learningAssessmentId
    ) {
        learningAssessmentService.disableLearningAssessment(learningAssessmentId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
