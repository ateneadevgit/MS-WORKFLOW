package com.fusm.workflow.controller;

import com.fusm.workflow.dto.CurriculumFatherDto;
import com.fusm.workflow.dto.ProgramSubjectDto;
import com.fusm.workflow.dto.SubjectTeacherDto;
import com.fusm.workflow.model.*;
import com.fusm.workflow.service.ICurriculumService;
import com.fusm.workflow.util.AppRoutes;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Clase que expone los servicios relacionados con los niveles de la estructura curricular de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.WORKFLOW_ROUTE + "/curriculum")
public class CurriculumController {

    @Autowired
    private ICurriculumService curriculumService;


    /**
     * Crea un nuevo nivel de la estructura curricular
     * @param curriculumListRequest Modelo que contiene la información para crear un nivel
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createCurriculum(
            @RequestBody CurriculumListRequest curriculumListRequest
            ) {
        curriculumService.createCurriculum(curriculumListRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la lista de los niveles en forma de árbol de la estructura curricular
     * @param objectId ID del programa
     * @return lista de niveles
     */
    @GetMapping("/object-id/{id}")
    public ResponseEntity<List<CurriculumModel>> getCurriculum(
            @PathVariable("id") Integer objectId
            ) {
        return ResponseEntity.ok(curriculumService.getCurriculum(objectId));
    }

    /**
     * Actualiza el nivel de la estructura curricular
     * @param updateCurriculumRequest Modelo que contiene la información a actualizar del nivel
     * @param curriculumId ID del nivel
     * @return OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCurriculum(
            @RequestBody UpdateCurriculumRequest updateCurriculumRequest,
            @PathVariable("id") Integer curriculumId
            ) {
        curriculumService.updateCurriculum(updateCurriculumRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualizar el nombre y descripción de un nivel
     * @param curriculumUpdateRequest Modelo que contiene la información a actualizar del nivel
     * @return OK
     */
    @PutMapping("/name-description")
    public ResponseEntity<String> updateNameAndDescriptionCurriculum(
            @RequestBody UpdateNameCurriculumRequest curriculumUpdateRequest
    ) {
        curriculumService.updateNameAndDescriptionOfCurriculum(curriculumUpdateRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Deshabilitar un nivel
     * @param curriculumId ID del nivel
     * @return OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> disableCurriculum(
            @PathVariable("id") Integer curriculumId
    ) {
        curriculumService.disableCurriculum(curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el contenido de un nivel
     * @param objectId ID del programa
     * @param curriculumType Tipo del nivel
     * @return lista de niveles
     */
    @GetMapping("/object-id/{id}/by-type/{type}")
    public ResponseEntity<List<CurriculumListModel>> getCurriculumByType(
            @PathVariable("id") Integer objectId,
            @PathVariable("type") Integer curriculumType
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumByType(objectId, curriculumType));
    }

    /**
     * Obtiene los niveles según su padre
     * @param objectId ID del nivel
     * @param curriculumFatherId ID del nivel padre
     * @return lista de niveles
     */
    @GetMapping("/object-id/{id}/by-father/{father}")
    public ResponseEntity<List<CurriculumFatherDto>> getCurriculumByFather(
            @PathVariable("id") Integer objectId,
            @PathVariable("father") Integer curriculumFatherId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumByFather(objectId, curriculumFatherId));
    }

    /**
     * Calcula el porcentaje de participación de la estructura curricular
     * @param objectId ID del programa
     * @return porcentaje de participación
     */
    @GetMapping("/object-id/{id}/calculate/percentage")
    public ResponseEntity<String> calculateParticipationPercentage(
            @PathVariable("id") Integer objectId
    ) {
        curriculumService.calculateParticipationPercentage(objectId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza el número de créditos
     * @param subjectListModelList Lista de niveles a actualizar
     * @return OK
     */
    @PutMapping("/credit")
    public ResponseEntity<String> updateCreditNumber(
            @RequestBody List<SubjectListModel> subjectListModelList
    ) {
        curriculumService.updateSubjectCredit(subjectListModelList);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la lista de asignaturas de un programa
     * @param obejctId ID del programa
     * @return lista de asignaturas
     */
    @GetMapping("/object-id/{id}/subjects")
    public ResponseEntity<List<SubjectListModel>> getSubjects(
            @PathVariable("id") Integer obejctId
    ) {
        return ResponseEntity.ok(curriculumService.getSubjects(obejctId));
    }

    /**
     * Actualiza de forma masiva los niveles
     * @param curriculumModelList Lista de modelos a actualizar
     * @return OK
     */
    @PutMapping("/massive")
    public ResponseEntity<String> updateCurriculumMassive(
            @RequestBody List<CurriculumModel> curriculumModelList
    ) {
        curriculumService.updateCurriculumMassive(curriculumModelList);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene todos los niveles de la estructura curricular sin los núcleos ni subnúcleos
     * @param objectId ID del programa
     * @return lista de niveles
     */
    @GetMapping("/object-id/{id}/no-core")
    public ResponseEntity<List<CurriculumModel>> getCurriculumWithoutCore(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumWithoutCore(objectId));
    }

    /**
     * Obtiene el nivel por ID
     * @param curriculumId ID del nivel
     * @return nivel
     */
    @GetMapping("/{id}")
    public ResponseEntity<CurriculumModel> getCurriculumById(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumById(curriculumId));
    }

    /**
     * Obnntiene los núcleos y subnúcleos de la estructura curricular
     * @param objectId ID del programa
     * @return lista de núcleos y subnúcleos
     */
    @GetMapping("/object-id/{id}/core")
    public ResponseEntity<List<CoreAndSubcoreModel>> getCoreAndSubcore(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getCoreAndSubcore(objectId));
    }

    /**
     * Actualiza de forma masiva la información de los núcleos y subnúcleos
     * @param coreAndSubcoreModels Lista de modelos que contiene la información a actualizar
     * @return OK
     */
    @PutMapping("/core-subcore")
    public ResponseEntity<String> updateCoreAndSubcoreMassive(
            @RequestBody List<CoreAndSubcoreModel> coreAndSubcoreModels
    ) {
        curriculumService.updateCoreAndSubcoreMassive(coreAndSubcoreModels);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el número total de créditos de la estructura curricular
     * @param objectId ID del programa
     * @return número total de créditos
     */
    @GetMapping("/credits/object-id/{id}")
    public ResponseEntity<Integer> getObjectNumberCredits(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumCredits(objectId));
    }

    /**
     * Obtiene las asignaturas agrupadas por semestre
     * @param objectId ID del programa
     * @return lista de asignaturas
     */
    @GetMapping("/semester/object-id/{id}")
    public ResponseEntity<CurriculumSemester> getCurriculumSemesterByProgram(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumSemesterByProgram(objectId));
    }

    /**
     * Obtiene los semestres de un programa académico
     * @param objectId
     * @return
     */
    @GetMapping("/semester-number/object-id/{id}")
    public ResponseEntity<List<SemesterModel>> getSemesterByProgram(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getSemestersByProgram(objectId));
    }

    /**
     * Obtiene los detalles de un nivel
     * @param curriculumId ID del nivel
     * @return nivel
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity<CurriculumDetail> getCurriculumDetail(
            @PathVariable("id") Integer curriculumId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumDetailById(curriculumId));
    }

    /**
     * General el PDF de la estructura curridular
     * @param programId ID del programa
     * @return URL del pdf
     * @throws DocumentException
     */
    @GetMapping("/pdf/object-id/{id}")
    public ResponseEntity<String> getCurriculumPdf(
            @PathVariable("id") Integer programId
    ) throws DocumentException {
        return ResponseEntity.ok(curriculumService.curriculumPdf(programId));
    }

    /**
     * Obtiene una lista de los NIFS
     * @return lista de NIFS
     */
    @GetMapping("/nif")
    public ResponseEntity<SubcoreNifsModel> getNifsCurriculum() {
        return ResponseEntity.ok(curriculumService.getNifsCurriculum());
    }

    /**
     * Crea un NIF
     * @param subcoreNifsRequest modelo que contiene la información para crear un nuevo NIF
     * @return OK
     */
    @PostMapping("/nif")
    public ResponseEntity<String> createNifsCurriculum(
            @RequestBody SubcoreNifsRequest subcoreNifsRequest
    ) {
        curriculumService.createNifsCurriculum(subcoreNifsRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los programas de asignatura
     * @param SearchSubject Modelo que contiene la información para filtrar la consulta
     * @param params parámetros como page y size para definir la paginación de la respuesta
     * @return lista de programas de asignatura
     */
    @PostMapping("/program-subject")
    public ResponseEntity<ResponsePage<ProgramSubjectDto>> getProgramSubject(
            @RequestBody SearchSubject SearchSubject,
            @RequestParam Map<String, Object> params
    ) {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        return ResponseEntity.ok(
                curriculumService.getProgramSubject(
                        SearchSubject,
                        PageModel.builder()
                                .page(page - 1)
                                .size(size)
                                .build())
        );
    }

    /**
     * Obtiene una lista de docentes segun una asignatura
     * @param curriculumId ID de la asignatura
     * @param userId ID del usuario
     * @param roleId ID del rol
     * @return lista de docentes
     */
    @GetMapping("/{id}/teacher/user-id/{userId}/role-id/{roleId}")
    public ResponseEntity<List<SubjectTeacherDto>> getTeachersBySubject(
            @PathVariable("id") Integer curriculumId,
            @PathVariable("userId") String userId,
            @PathVariable("roleId") Integer roleId
    ) {
        return ResponseEntity.ok(curriculumService.getTeachersBySubject(curriculumId, userId, roleId));
    }

    /**
     * Actualizar la información complementaria de los NIFS
     * @param complementaryNifs modelo que contiene la información a actualizar del NIF
     * @param curriculumId ID del NIF
     * @return OK
     */
    @PutMapping("/{id}/complementary-nif")
    public ResponseEntity<String> updateComplementaryNifs(
            @RequestBody UpdateComplementaryNifs complementaryNifs,
            @PathVariable("id") Integer curriculumId
    ) {
        curriculumService.updateComplementaryNifs(complementaryNifs, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los niveles de un programa
     * @param objectId ID del programa
     * @return lista de niveles
     */
    @GetMapping("/levels/object-id/{id}")
    private ResponseEntity<List<SelectModel>> getLevelsByProgram(
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getLevelsByProgram(objectId));
    }

    /**
     * Crea una evaluación complementaria
     * @param complementaryEvaluationRequest Modelo que contiene la informaicón necesaria para crear una evaluación
     * @param curriculumId ID del nivel
     * @return OK
     */
    @PostMapping("/{id}/complementary-evaluation")
    private ResponseEntity<String> createComplementaryEvaluation(
            @RequestBody ComplementaryEvaluationRequest complementaryEvaluationRequest,
            @PathVariable("id") Integer curriculumId
    ) {
        curriculumService.createComplementaryEvaluation(complementaryEvaluationRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza la información de la evaluación complementaria
     * @param complementaryEvaluationRequest Modelo que contiene la información para actualizar una evaluación
     * @param curriculumId ID del nivel
     * @return OK
     */
    @PutMapping("/{id}/complementary-evaluation")
    private ResponseEntity<String> updateComplementaryEvaluation(
            @RequestBody ComplementaryEvaluationRequest complementaryEvaluationRequest,
            @PathVariable("id") Integer curriculumId
    ) {
        curriculumService.updateComplementaryEvaluation(complementaryEvaluationRequest, curriculumId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtener las evaluaciones según el nivel
     * @param curriculumType ID del tipo de nivel
     * @param objectId ID del programa
     * @return lista de evaluaciones
     */
    @GetMapping("/object-id/{id}/by-type/{type}/complementary-evaluation")
    private ResponseEntity<List<CurriculumTypeEvaluation>> getCurriculumEvaluationByType(
            @PathVariable("type") Integer curriculumType,
            @PathVariable("id") Integer objectId
    ) {
        return ResponseEntity.ok(curriculumService.getCurriculumEvaluationByType(objectId, curriculumType));
    }

    /**
     * Obtener el progreso del programa
     * @param programId ID del programa
     * @param userId ID del usuario
     * @return progreso del programa
     */
    @GetMapping("/object-id/{id}/user-id/{userId}")
    private ResponseEntity<ProgramUserData> getProgramProgress(
            @PathVariable("id") Integer programId,
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(curriculumService.getProgramProgress(programId, userId));
    }

    /**
     * Obtiene el histórico de asignaturas cursadas por un usuario
     * @param programId ID del programa
     * @param userId ID del usuario
     * @return lista de asignaturas
     */
    @GetMapping("/object-id/{id}/historic/user-id/{userId}")
    private ResponseEntity<List<UserSubjectData>> getHistorySubject(
            @PathVariable("id") Integer programId,
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(curriculumService.getHistorySubject(programId, userId));
    }

    /**
     * Obtiene las asignaturas cursadas actualmente por usuario
     * @param programId ID del programa
     * @param userId ID del usuario
     * @return lista de asignaturas
     */
    @GetMapping("/object-id/{id}/current/user-id/{userId}")
    private ResponseEntity<List<UserSubjectData>> getCurrentSubject(
            @PathVariable("id") Integer programId,
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.ok(curriculumService.getCurrentSubject(programId, userId));
    }

}
