package com.fusm.workflow.service.specific;

import com.fusm.workflow.entity.ComplementaryCore;
import com.fusm.workflow.entity.ComplementarySubject;
import com.fusm.workflow.entity.Curriculum;
import com.fusm.workflow.model.CoreModel;
import com.fusm.workflow.model.CurriculumModel;
import com.fusm.workflow.model.SubjectRequest;

import java.util.*;

public class CurriculumMapper {

    public List<CurriculumModel> mapToCurriculumModel(List<Curriculum> curriculumList) {
        Map<Integer, CurriculumModel> nodeMap = new HashMap<>();

        for (Curriculum curriculum : curriculumList) {
            CurriculumModel node = CurriculumModel.builder()
                    .curriculumId(curriculum.getCurriculumId())
                    .name(curriculum.getName())
                    .type(curriculum.getCurriculumType())
                    .numberCredits(curriculum.getNumberCredits())
                    .percentageParticipation(curriculum.getPercentageParticipation())
                    .description(curriculum.getDescription())
                    .createdAt(curriculum.getCreatedAt().getTime())
                    .isUpdated(false)
                    .fatherId((curriculum.getCurrirriculumFatherId() != null) ? curriculum.getCurrirriculumFatherId().getCurriculumId() : null)
                    .subjectRequest(getComplementarySubject(curriculum.getComplementarySubject()))
                    .coreModel(getComplementaryCore(curriculum.getComplementaryCore()))
                    .childs(new ArrayList<>())
                    .isNif(curriculum.getIsNif())
                    .build();
            nodeMap.put(curriculum.getCurriculumId(), node);
        }

        for (Curriculum curriculum : curriculumList) {
            CurriculumModel currentNode = nodeMap.get(curriculum.getCurriculumId());
            Curriculum parent = curriculum.getCurrirriculumFatherId();
            if (parent != null) {
                CurriculumModel parentNode = nodeMap.get(parent.getCurriculumId());
                if (parentNode != null) {
                    parentNode.addChild(currentNode);
                }
            }
        }

        List<CurriculumModel> roots = new ArrayList<>();
        for (Curriculum curriculum : curriculumList) {
            CurriculumModel currentNode = nodeMap.get(curriculum.getCurriculumId());
            Curriculum parent = curriculum.getCurrirriculumFatherId();
            if (parent == null) {
                roots.add(currentNode);
            }
        }

        return roots;
    }

    private SubjectRequest getComplementarySubject(ComplementarySubject complementarySubject) {
        SubjectRequest subject = null;
        if (complementarySubject != null) {
            subject = SubjectRequest.builder()
                    .semester(complementarySubject.getSemester())
                    .code(complementarySubject.getCode())
                    .hoursInteractionTeacher(complementarySubject.getHoursInteractionTeacher())
                    .hourSelfWork(complementarySubject.getHourSelfWork())
                    .build();
        }
        return subject;
    }

    private CoreModel getComplementaryCore(ComplementaryCore complementaryCore) {
        CoreModel raeg = null;
        if (complementaryCore != null) raeg = CoreModel.builder()
                .raeg(complementaryCore.getRaeg()).build();
        return raeg;
    }

}