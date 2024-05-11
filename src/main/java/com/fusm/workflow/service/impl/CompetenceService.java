package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.Competence;
import com.fusm.workflow.model.CompetenceRequest;
import com.fusm.workflow.repository.ICompetenceRepository;
import com.fusm.workflow.service.ICompetenceService;
import com.fusm.workflow.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CompetenceService implements ICompetenceService {

    @Autowired
    private ICompetenceRepository competenceRepository;


    @Override
    public void createCompetence(CompetenceRequest competenceRequest) {
        competenceRepository.save(
                Competence.builder()
                        .categoryId(competenceRequest.getCategoryId())
                        .code(competenceRequest.getCode())
                        .description(competenceRequest.getDescription())
                        .createdBy(competenceRequest.getCreatedBy())
                        .createdAt(new Date())
                        .enabled(true)
                        .isNif(competenceRequest.getRoleId().equals(Constant.VR_ROLE))
                        .build()
        );
    }

    @Override
    public List<CompetenceRequest> getCompetences(Boolean isNif) {
        return competenceRepository.findAllCompetencesOrdered(isNif).stream().map(
                competence -> CompetenceRequest.builder()
                        .competenceId(competence.getCompetenceId())
                        .categoryId(competence.getCategoryId())
                        .code(competence.getCode())
                        .description(competence.getDescription())
                        .build()
        ).toList();
    }

    @Override
    public void updateCompetence(CompetenceRequest competenceRequest, Integer competenceId) {
        Optional<Competence> competenceOptional = competenceRepository.findById(competenceId);

        if (competenceOptional.isPresent()) {
            Competence competence = competenceOptional.get();
            competence.setCategoryId(competenceRequest.getCategoryId());
            competence.setCode(competenceRequest.getCode());
            competence.setDescription(competenceRequest.getDescription());
            competence.setUpdatedAt(new Date());
            competenceRepository.save(competence);
        }
    }

}
