package com.fusm.workflow.service.impl;

import com.fusm.workflow.entity.History;
import com.fusm.workflow.entity.Program;
import com.fusm.workflow.entity.ProgramModule;
import com.fusm.workflow.entity.ProposalRenovation;
import com.fusm.workflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryExtendedService {

    @Autowired
    private IHistoryRepository historyRepository;

    @Autowired
    private IProgramModuleRepository programModuleRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IProposalRenovationRepository proposalRenovationRepository;


    public void createHistoric(String value, Integer moduleId, Integer programId, String createdBy, Integer type) {
        Optional<ProgramModule> programModuleOptional = programModuleRepository.findById(moduleId);
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programModuleOptional.isPresent() && programOptional.isPresent()) {
            List<History> lastVersion = (type != null) ? historyRepository.findLastVersionWithType(programId, moduleId, type) :
                    historyRepository.findLastVersion(programId, moduleId);
            double version = (!lastVersion.isEmpty()) ? lastVersion.get(0).getVersion() + 1 : 1;
            String minute = "";
            List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
            if (!proposalRenovationList.isEmpty()) minute = proposalRenovationList.get(0).getMinuteRenovation();
            historyRepository.save(
                    History.builder()
                            .value(value)
                            .version(version)
                            .createdAt(new Date())
                            .createdBy(createdBy)
                            .updatedAt(new Date())
                            .enabled(true)
                            .objectType(type)
                            .programId(programOptional.get())
                            .programModuleId(programModuleOptional.get())
                            .minute(minute)
                            .build()
            );
        }
    }

}
