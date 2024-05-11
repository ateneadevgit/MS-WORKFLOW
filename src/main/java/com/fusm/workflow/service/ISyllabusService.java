package com.fusm.workflow.service;

import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.model.SyllabusInformationModel;
import com.fusm.workflow.model.SyllabusRequest;
import com.lowagie.text.DocumentException;
import com.fusm.workflow.model.SyllabusModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISyllabusService {

    SyllabusInformationModel getPreloadInformation(Integer curriculumId);
    String syllabusPdf(Integer curriculumId) throws DocumentException;
    void createSyllabus(SyllabusModel syllabusModel);
    void createSyllabusNifs(SyllabusModel syllabusModel, Curriculum curriculum);
    Boolean getSyllabusExist(Integer curriculumId);
    SyllabusModel getSyllabusByCurriculum(Integer curriculumId);
    void updateSyllabus(Integer syllabusId, SyllabusModel syllabusModel);
    void updateSyllabusMassive(List<SyllabusRequest> syllabusRequestList) throws DocumentException;
}
