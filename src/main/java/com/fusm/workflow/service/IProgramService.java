package com.fusm.workflow.service;

import com.fusm.workflow.model.EvaluateProposalModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProgramService {

    void evaluateProposal(EvaluateProposalModel evaluation, Integer proposalId);
    void declineOrDisableProgram(EvaluateProposalModel evaluation, Integer programId);
    String getProgramName(List<Integer> programIds);

}
