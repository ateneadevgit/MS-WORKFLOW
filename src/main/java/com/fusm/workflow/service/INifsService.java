package com.fusm.workflow.service;

import com.fusm.workflow.model.NifsModel;
import com.fusm.workflow.model.NifsRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface INifsService {

    void createNifsData(NifsRequest nifsRequest);
    void updateNifsData(NifsRequest nifsRequest, Integer nifId);
    void addSection(List<NifsRequest> nifsRequest, Integer nifId);
    void disableSection(Integer nifId);
    NifsModel viewNifsData(Integer type);

}
