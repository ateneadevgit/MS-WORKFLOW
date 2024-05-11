package com.fusm.workflow.service;

import com.fusm.workflow.model.CompetenceRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICompetenceService {

    void createCompetence(CompetenceRequest competenceRequest);
    List<CompetenceRequest> getCompetences(Boolean isNif);
    void updateCompetence(CompetenceRequest competenceRequest, Integer competendeId);

}
