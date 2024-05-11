package com.fusm.workflow.service;

import com.fusm.workflow.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProblemBankService {

    void createProblemBank(ProblemBankRequest problemBankRequest);
    List<ProblemBankModel> getNifProblemBank(SearchModel searchModel);
    List<ProblemBankModel> getProblemBank(SearchModel searchModel);
    void updateProblemBank(ProblemBankRequest problemBankRequest, Integer problemBankId);
    void evaluateProblemBank(EvaluateProposalModel evaluateProposalModel, Integer problemBankId);
    void enableDisableProblemBank(Integer problemBankId, Boolean enabled, UserData userData);

}
